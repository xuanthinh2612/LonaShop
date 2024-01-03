package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.*;
import LonaShop.service.ArticleService;
import LonaShop.service.CategoryService;
import LonaShop.service.CoverService;
import LonaShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class DashBoardController extends UserBaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CoverService coverService;

    @ModelAttribute("categoryList")
    private List<Category> initCategoryList() {
        return getListCategory();
    }

    @GetMapping(value = {"/trang-chu", "", "/"})
    public String showDashBoard(Model model) {
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.HOME_PAGE_MODE);
        List<Cover> mainCoverList = coverService.findAll().stream().limit(3).toList(); // check again
        List<Cover> subCoverList = coverService.findAll().stream().limit(2).toList(); // check again

        List<Article> articleList = articleService.findAll().stream().filter(e -> e.getStatus() == CommonConst.FLAG_ON)
                .collect(Collectors.toList());
        List<Product> productList = getAvailableProduct();
        model.addAttribute("articleList", articleList);
        model.addAttribute("productList", productList);
        model.addAttribute("mainCoverList", mainCoverList);
        model.addAttribute("subCoverList", subCoverList);
        return "user/dashboard/index";
    }

    @GetMapping(value = {"/about", "/trang-chu/about"})
    public String aboutUs(Model model) {
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.ABOUT_PAGE_MODE);
        return "/user/dashboard/about";
    }

    @GetMapping("/category/{id}")
    public String searchByCategory(@PathVariable("id") Long id, Model model) {

        List<Article> articleList = articleService.findAll().stream().filter(e -> e.getStatus() == CommonConst.FLAG_ON).toList();
        List<Product> productList = getAvailableProduct().stream().filter(e -> e.getCategory().getId().equals(id)).toList();
        model.addAttribute("articleList", articleList);
        model.addAttribute("productList", productList);
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.SEARCH_MODE);
        return "/user/dashboard/index";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("key") String key, Model model) {

        List<Article> articleList = articleService.findAll().stream().filter(e -> e.getStatus() == CommonConst.FLAG_ON).toList();
        List<Product> productList = productService.findByKey(key);
        model.addAttribute("articleList", articleList);
        model.addAttribute("productList", productList);
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.SEARCH_MODE);

        return "/user/dashboard/index";
    }

}
