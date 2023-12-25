package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.model.Article;
import LonaShop.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/blog")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public String getListArticle(Model model) {

        List<Article> articleList = articleService.findAll().stream().filter(e -> e.getStatus() == CommonConst.FLAG_ON).collect(Collectors.toList());

        if (ObjectUtils.isEmpty(articleList)) {
            return "redirect:/trang-chu";
        }


        Article mainArticle = getMainArticle(articleList);

        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.BLOG_PAGE_MODE);
        model.addAttribute("articleList", articleList);
        model.addAttribute("mainArticle", mainArticle);
        return "user/article/index";
    }

    @GetMapping("/show/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);

        List<Article> relatedList = articleService.findAll().stream().filter(e -> e.getStatus() == CommonConst.FLAG_ON)
                .limit(10L).collect(Collectors.toList());

        relatedList.remove(article);

        model.addAttribute(CommonConst.PAGE_MODE, CommonConst.BLOG_PAGE_MODE);
        model.addAttribute("article", article);
        model.addAttribute("relatedList", relatedList);
        return "user/article/show";
    }

    private Article getMainArticle(List<Article> articleList) {

        for (Article article : articleList) {
            if (article.getOnTop() == CommonConst.FLAG_ON) {
                return article;
            }
        }
        return articleList.get(0);
    }
}
