package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.Product;
import LonaShop.model.UserOrder;
import LonaShop.service.ProductService;
import LonaShop.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/order")
public class OrderController extends UserBaseController {

    @Autowired
    ProductService productService;

    @Autowired
    UserOrderService userOrderService;

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
    public String createOrder(@ModelAttribute("order") UserOrder order, BindingResult result, Model model) {
        Product product = order.getProduct();
        if (ObjectUtils.isEmpty(product) || isNotValidProduct(product) || order.getQuantity() <= 0) {
            return "redirect:/";
        }

        order.setTotalAmount(order.getQuantity() * product.getCurrentPrice());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setStatus(CommonConst.ORDERED);
        userOrderService.save(order);

        return "redirect:/trang-chu";
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
