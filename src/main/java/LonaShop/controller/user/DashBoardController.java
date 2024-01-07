package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.Article;
import LonaShop.model.Category;
import LonaShop.model.Cover;
import LonaShop.model.Product;
import LonaShop.service.ArticleService;
import LonaShop.service.CategoryService;
import LonaShop.service.CoverService;
import LonaShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<Cover> mainCoverList = coverService.getMainCoverList().stream().limit(3).toList();
        List<Cover> subCoverList = coverService.getSubCoverList().stream().limit(2).toList();
        List<Article> articleList = articleService.findAvailableList().stream().limit(20).toList();
        List<Product> productList = productService.findAvailableList();
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

        List<Article> articleList = articleService.findAvailableList().stream().limit(20).toList();
        model.addAttribute("articleList", articleList);
        List<Product> productList = productService.findAvailableListByCategoryId(id);
        model.addAttribute("productList", productList);
        List<Cover> subCoverList = coverService.getSubCoverList().stream().limit(2).toList();
        model.addAttribute("subCoverList", subCoverList);
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.SEARCH_MODE);

        return "/user/dashboard/index";
    }


    @GetMapping("/search")
    public String searchProduct(@RequestParam("key") String key, Model model) {

        List<Article> articleList = articleService.findAvailableList().stream().limit(20).toList();
        model.addAttribute("articleList", articleList);
        List<Product> productList = productService.findByKey(key);
        model.addAttribute("productList", productList);
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.SEARCH_MODE);
        List<Cover> subCoverList = coverService.getSubCoverList().stream().limit(2).toList();
        model.addAttribute("subCoverList", subCoverList);

        return "/user/dashboard/index";
    }
}
