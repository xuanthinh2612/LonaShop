package LonaShop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class DashBoardManagerController extends AdminBaseController{

    @RequestMapping(value = "quan-ly", method = RequestMethod.GET)
    public String dashBoard(){
        return "admin/dashboard/index";
    }
}
