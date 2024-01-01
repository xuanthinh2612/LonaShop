package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.*;
import LonaShop.service.ArticleService;
import LonaShop.service.CategoryService;
import LonaShop.service.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class DashBoardController extends UserBaseController {

    @Autowired
    private CoverService coverService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/trang-chu", "", "/"})
    public String showDashBoard(Model model) {
//        Cover cover = coverService.findAll().get(0);
//        Image image = cover.getImages().get(0);

        List<Article> articleList = articleService.findAll().stream().filter(e -> e.getStatus() == CommonConst.FLAG_ON)
                .collect(Collectors.toList());

        List<Product> productList = getAvailableProduct();
        List<Category> categoryList = categoryService.findList();

        model.addAttribute("articleList", articleList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
//        model.addAttribute("image", image);
//        model.addAttribute("cover", cover);
        return "user/dashboard/index";
    }

    @GetMapping(value = {"/about", "/trang-chu/about"})
    public String aboutUs(Model model) {
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.ABOUT_PAGE_MODE);
        return "/user/dashboard/about";
    }

}
