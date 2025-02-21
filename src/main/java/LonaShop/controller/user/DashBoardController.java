package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.Article;
import LonaShop.model.Cover;
import LonaShop.model.Product;
import LonaShop.service.ArticleService;
import LonaShop.service.CategoryService;
import LonaShop.service.CoverService;
import LonaShop.service.ProductService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("")
public class DashBoardController extends UserBaseController {

    private static final Logger logger =  Logger.getLogger(DashBoardController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CoverService coverService;
    @Autowired
    private Helper helper;

    @GetMapping(value = {"/trang-chu", "", "/"})
    public String showDashBoard(Model model, RedirectAttributes attributes) {
//        logger.debug("Debug message");
//        logger.info("Info message");
//        logger.warn("Warning message");
//        logger.error("Error message");
//        logger.fatal("Fatal message");

        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.HOME_PAGE_MODE);
        List<Cover> mainCoverList = coverService.getMainCoverList().stream().limit(3).toList();
        List<Cover> subCoverList = coverService.getSubCoverList().stream().limit(2).toList();
        List<Article> articleList = articleService.findAvailableList().stream().limit(20).toList();
        List<Product> productList = productService.findAvailableList();
        Article mainArticle = helper.getMainArticle(articleList);

        model.addAttribute("mainArticle", mainArticle);
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
        List<Cover> mainCoverList = coverService.getMainCoverList().stream().limit(3).toList();
        model.addAttribute("mainCoverList", mainCoverList);
        List<Article> articleList = articleService.findAvailableList().stream().limit(20).toList();
        model.addAttribute("articleList", articleList);
        List<Cover> subCoverList = coverService.getSubCoverList().stream().limit(2).toList();
        model.addAttribute("subCoverList", subCoverList);
        Article mainArticle = helper.getMainArticle(articleList);
        model.addAttribute("mainArticle", mainArticle);
        List<Product> productList = productService.findAvailableListByCategoryId(id);
        model.addAttribute("productList", productList);
        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.SEARCH_MODE);

        return "/user/dashboard/index";
    }


    @GetMapping("/search")
    public String searchProduct(@RequestParam("key") String key, Model model) {

        List<Cover> mainCoverList = coverService.getMainCoverList().stream().limit(3).toList();
        model.addAttribute("mainCoverList", mainCoverList);
        List<Cover> subCoverList = coverService.getSubCoverList().stream().limit(2).toList();
        model.addAttribute("subCoverList", subCoverList);
        List<Article> articleList = articleService.findAvailableList().stream().limit(20).toList();
        model.addAttribute("articleList", articleList);
        Article mainArticle = helper.getMainArticle(articleList);
        model.addAttribute("mainArticle", mainArticle);
        List<Product> productList = productService.findByKey(key);
        model.addAttribute("productList", productList);

        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.SEARCH_MODE);

        return "/user/dashboard/index";
    }
}
