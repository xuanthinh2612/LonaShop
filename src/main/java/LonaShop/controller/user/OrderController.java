package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.Product;
import LonaShop.model.UserDto;
import LonaShop.model.UserOrder;
import LonaShop.service.ProductService;
import LonaShop.service.UserOrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/order")
public class OrderController extends UserBaseController {

    @Autowired
    ProductService productService;

    @Autowired
    UserOrderService userOrderService;
    @Autowired
    Helper helper;

    @GetMapping("/new/{id}")
    public String newOrder(@PathVariable("id") Long id, @RequestParam("inputQuantity") Long inputQuantity, Model model) {
        Product product = productService.findById(id);

        if (ObjectUtils.isEmpty(product) || isNotValidProduct(product)) {
            return "redirect:/";
        }
        UserOrder order = new UserOrder();
        order.setProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("order", order);
        model.addAttribute("inputQuantity", inputQuantity);
        return "user/order/new";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") UserOrder order, BindingResult result, HttpServletRequest request, Model model) {
        Product product = order.getProduct();
        if (ObjectUtils.isEmpty(product) || isNotValidProduct(product) || order.getQuantity() <= 0) {
            return "redirect:/";
        }

        String userIpAddress = request.getRemoteAddr();

        order.setTotalAmount(order.getQuantity() * product.getCurrentPrice());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setStatus(CommonConst.ORDERED);
        order.setPaymentStatus(CommonConst.WAITING_FOR_PAY);
        order.setUserIp(userIpAddress);
        UserDto currentUserDto = getCurrentLoggedInUserDto();
        userOrderService.save(order);

        if (ObjectUtils.isEmpty(currentUserDto)) {
            order.setOrderCode(helper.getRandomOrderCode(CommonConst.NON_USER_ORDER));
        } else {
            order.setOrderCode(helper.getRandomOrderCode(CommonConst.USER_ORDER));
        }
        userOrderService.save(order);

        return "redirect:/order/payment?order=" + order.getId() + "&code=" + order.getOrderCode() ;
    }

    @GetMapping("/payment")
    public String gotoPaymentPage(@RequestParam("order") Long orderId,
                                  @RequestParam("code") String orderCode,
                                  Model model) {
        String message;
        UserOrder order = userOrderService.findByIdAndOrderCode(orderId, orderCode);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/";
        }

        if (order.getPaymentType() == CommonConst.PAY_WHEN_RECEIVE) {
            message = "Đặt hàng thành công! shop có thể sẽ liên hệ tới bạn để xác nhận đơn hàng.";
        } else {
            message = "Bạn vui lòng chuyển khoản với nội dung mã đơn hàng để chúng tôi có thể xác nhận đơn hàng.";
        }

        model.addAttribute("message", message);
        model.addAttribute("order", order);
        return "/user/order/paymentPage";
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
