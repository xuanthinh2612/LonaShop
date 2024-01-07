package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.Category;
import LonaShop.model.Inquiry;
import LonaShop.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/inquiry")
public class UserInquiryController extends UserBaseController {

    @Autowired
    private InquiryService inquiryService;

    @GetMapping("/new")
    public String newInquiry(Model model) {
        Inquiry inquiry = new Inquiry();
        model.addAttribute("inquiry", inquiry);
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.INQUIRY_PAGE_MODE);
        return "/user/inquiry/new";
    }

    @PostMapping("/create")
    public String createInquiry(@ModelAttribute("inquiry") Inquiry inquiry, BindingResult result, Model model) {
        inquiryService.save(inquiry);
        return "redirect:/trang-chu";
    }
}
