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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController extends UserBaseController {
    private final MessageSource messageSource;

    public OrderController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

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
    public String newOrder(RedirectAttributes attributes, Model model, Locale locale) {
        User currentUser = getCurrentLoggedInUser();

        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("order.auth.error", null, locale));
            return "redirect:/login";
        }

        Cart myCart = currentUser.getCart();

        if (ObjectUtils.isEmpty(myCart)) {
            return "redirect:/cart";
        }

        if (myCart.getCartItems().size() == 0) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("cart.empty", null, locale));
            return "redirect:/";
        }

        UserOrder order = new UserOrder();

        order.setPaymentType(CommonConst.PAY_BY_MOMO); // set default pay by momo
        model.addAttribute("order", order);
        model.addAttribute("cart", myCart);
        return "user/order/new";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") UserOrder order,
                              BindingResult result, Locale locale, HttpServletRequest request, Model model,
                              RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            return "user/order/new";
        }

        User currentUser = getCurrentLoggedInUser();

        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("order.auth.error", null, locale));
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

//        if (ObjectUtils.isEmpty(currentUser)) {
//            order.setOrderCode(helper.getRandomOrderCode(CommonConst.NON_USER_ORDER));
//        } else {
        order.setUser(currentUser);
        order.setOrderCode(helper.getRandomOrderCode(CommonConst.USER_ORDER));
//        }

        userOrderService.save(order);

        myCart.getCartItems().clear();
        cartService.save(myCart);

        return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("order") Long orderId, @RequestParam("orderCode") String orderCode,
                                  RedirectAttributes attributes, Model model, Locale locale) {
        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("order.auth.error", null, locale));
            return "redirect:/login";
        }

        List<UserOrder> myOrderList = currentUser.getOrders();
        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order) || !myOrderList.contains(order)) {
            attributes.addFlashAttribute("error", messageSource.getMessage("order.notFound.error", null, locale));
            return "redirect:/";
        }
        model.addAttribute("order", order);
        return "/user/order/paymentPage";
    }

    @PostMapping(value = "/payment", params = "cancel")
    public String userCanceledOrder(@RequestParam("order") Long orderId,
                                    @RequestParam("orderCode") String orderCode,
                                    HttpServletRequest request, Locale locale,
                                    RedirectAttributes attributes,
                                    Model model) {

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("order.auth.error", null, locale));
            return "redirect:/login";
        }

        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order)) {
            attributes.addFlashAttribute("error", messageSource.getMessage("order.notFound.error", null, locale));
            return "redirect:/";
        }
        if (order.getStatus() == CommonConst.USER_CANCELED_STATUS
                || order.getPaymentStatus() == CommonConst.USER_CONFIRMED_PAY) {
            attributes.addFlashAttribute("error", messageSource.getMessage("action.denied", null, locale));
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
            attributes.addFlashAttribute("error", messageSource.getMessage("action.denied", null, locale));
            return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
        }
        model.addAttribute("order", order);

        return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
    }

    @PostMapping(value = "payment", params = "confirmPaid")
    public String userConfirmPaid(@RequestParam("order") Long orderId,
                                  @RequestParam("orderCode") String orderCode,
                                  RedirectAttributes attributes, Locale locale,
                                  Model model) {

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("order.auth.error", null, locale));
            return "redirect:/login";
        }

        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order)) {
            attributes.addFlashAttribute("error", messageSource.getMessage("order.notFound.error", null, locale));
            return "redirect:/";
        }
        if (order.getStatus() == CommonConst.USER_CANCELED_STATUS
                || order.getPaymentStatus() == CommonConst.USER_CONFIRMED_PAY) {
            attributes.addFlashAttribute("error", messageSource.getMessage("action.denied", null, locale));
            return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
        }

        order.setPaymentStatus(CommonConst.USER_CONFIRMED_PAY);
        userOrderService.save(order);

        model.addAttribute("order", order);
        return "redirect:/order/payment?order=" + order.getId() + "&orderCode=" + order.getOrderCode();
    }

    @GetMapping("/histories")
    public String orderHistory(RedirectAttributes attributes, Locale locale, Model model) {
        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("cart.auth.error", null, locale));
            return "redirect:/login";
        }
        List<UserOrder> orderedList = currentUser.getOrders();
        // pending
        return "/user/order/histories";
    }

}
