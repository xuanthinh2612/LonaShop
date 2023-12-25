package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.Product;
import LonaShop.model.UserOrder;
import LonaShop.service.ProductService;
import LonaShop.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    ProductService productService;

    @Autowired
    UserOrderService userOrderService;

    @GetMapping("/new/{id}")
    public String newOrder(@PathVariable("id") Long id, Model model){
        UserOrder order = new UserOrder();
        Product product = productService.findById(id);
        order.setProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("order", order);
        return "user/order/new";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") UserOrder order, BindingResult result, Model model){
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setStatus(CommonConst.ORDERED);
        userOrderService.save(order);

        return "redirect:/trang-chu";
    }

    @GetMapping("/histories")
    public String orderHistory(Model model){
        // logic here after get current user
        // pending
        return "/user/order/histories";
    }

}
