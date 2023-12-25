package LonaShop.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/shop")
public class ShopManageController {

    @GetMapping("/list")
    public String getListShop(Model model){
        // get list shops here

        return "admin/shop/index";
    }
}
