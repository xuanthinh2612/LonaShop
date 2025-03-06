package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.*;
import LonaShop.service.CartService;
import LonaShop.service.ProductService;
import LonaShop.service.UserOrderService;
import LonaShop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/order")
public class OrderController extends UserBaseController {

    @Autowired
    ProductService productService;

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;
    @Autowired
    Helper helper;

    @GetMapping("/new")
    public String newOrder(RedirectAttributes attributes, Model model) {
        User currentUser = getCurrentLoggedInUser();

        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng thanh toán.");
            return "redirect:/login";
        }

        Cart myCart = currentUser.getCart();

        if (ObjectUtils.isEmpty(myCart)) {
            return "redirect:/cart";
        }

        UserOrder order = new UserOrder();

        order.setPaymentType(CommonConst.PAY_BY_MOMO); // set default pay by momo
        model.addAttribute("order", order);
        model.addAttribute("cart", myCart);
        return "user/order/new";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") UserOrder order,
                              BindingResult result,
                              HttpServletRequest request,
                              Model model,
                              RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            return "user/order/new";
        }

        User currentUser = getCurrentLoggedInUser();

        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng thanh toán.");
            return "redirect:/login";
        }

        Cart myCart = currentUser.getCart();

        if (ObjectUtils.isEmpty(myCart)) {
            return "redirect:/cart";
        }

        List<CartItem> cartItemList = currentUser.getCart().getCartItems();

        long totalAmount = 0;
        if (ObjectUtils.isEmpty(order.getOrderItems())) {
            order.setOrderItems(new ArrayList<>());
        }

        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            long subTotal = cartItem.getQuantity() * cartItem.getPriceAtTime();

            orderItem.setSubAmount(subTotal);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtTime(cartItem.getPriceAtTime());
            orderItem.setCreatedAt(new Date());
            orderItem.setUpdatedAt(new Date());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            totalAmount += subTotal;
        }


        String userIpAddress = helper.getClientIp(request);
        order.setTotalAmount(totalAmount);

        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setStatus(CommonConst.ORDERED_STATUS);
        order.setPaymentStatus(CommonConst.WAITING_FOR_PAY);
        order.setUserIp(userIpAddress);

        if (ObjectUtils.isEmpty(currentUser)) {
            order.setOrderCode(helper.getRandomOrderCode(CommonConst.NON_USER_ORDER));
        } else {
            order.setUser(currentUser);
            order.setOrderCode(helper.getRandomOrderCode(CommonConst.USER_ORDER));
        }

        userOrderService.save(order);

        myCart.getCartItems().clear();
        cartService.save(myCart);

        return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("order") Long orderId,
                                  @RequestParam("orderCode") String orderCode,
                                  RedirectAttributes attributes,
                                  Model model) {
        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order)) {
            attributes.addFlashAttribute("error", "Thông tin không chính xác ! vui lòng kiểm tra lại.");
            return "redirect:/";
        }
        model.addAttribute("order", order);
        return "/user/order/paymentPage";
    }

    @PostMapping(value = "/payment", params = "cancel")
    public String userCanceledOrder(@RequestParam("order") Long orderId,
                                    @RequestParam("orderCode") String orderCode,
                                    HttpServletRequest request,
                                    RedirectAttributes attributes,
                                    Model model) {
        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order)) {
            attributes.addFlashAttribute("error", "Thông tin không chính xác ! vui lòng kiểm tra lại.");
            return "redirect:/";
        }
        if (order.getStatus() == CommonConst.USER_CANCELED_STATUS
                || order.getPaymentStatus() == CommonConst.USER_CONFIRMED_PAY) {
            attributes.addFlashAttribute("error", "Không thể thực hiện hành động với đơn hàng này.");
            return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
        }

        UserDto userDto = getCurrentLoggedInUserDto();
        String userIpAddress = helper.getClientIp(request);
        if (!ObjectUtils.isEmpty(userDto) && userDto.getId().equals(order.getUser().getId())
                || Objects.equals(userIpAddress, order.getUserIp())) {
            order.setPaymentStatus(CommonConst.NOT_PAY);
            order.setStatus(CommonConst.USER_CANCELED_STATUS);
            userOrderService.save(order);
        } else {
            attributes.addFlashAttribute("error", "Bạn không thể hủy đơn hàng!.");
            return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
        }
        model.addAttribute("order", order);

        return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();

    }

    @PostMapping(value = "payment", params = "confirmPaid")
    public String userConfirmPaid(@RequestParam("order") Long orderId,
                                  @RequestParam("orderCode") String orderCode,
                                  RedirectAttributes attributes,
                                  Model model) {
        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order)) {
            attributes.addFlashAttribute("error", "Thông tin không chính xác ! vui lòng kiểm tra lại.");
            return "redirect:/";
        }
        if (order.getStatus() == CommonConst.USER_CANCELED_STATUS
                || order.getPaymentStatus() == CommonConst.USER_CONFIRMED_PAY) {
            attributes.addFlashAttribute("error", "Không thể thực hiện hành động với đơn hàng này.");
            return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
        }

        order.setPaymentStatus(CommonConst.USER_CONFIRMED_PAY);
        userOrderService.save(order);

        model.addAttribute("order", order);
        return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
    }

    @GetMapping("/histories")
    public String orderHistory(Model model) {
        // logic here after get current user
        // pending
        return "/user/order/histories";
    }

    private boolean isNotValidProduct(Product product) {
        return product.getStatus() != CommonConst.ProductStatus.available.code()
                && product.getStatus() != CommonConst.ProductStatus.sale.code();
    }

}
