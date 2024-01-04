package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.Article;
import LonaShop.model.Image;
import LonaShop.model.SubContent;
import LonaShop.service.ArticleService;
import LonaShop.service.ImageService;
import LonaShop.service.SubContentService;
import LonaShop.service.file.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/article" )
@SessionAttributes("article" )
public class ArticleManageController {

    @Autowired
    private Environment environment;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SubContentService subContentService;
    @Autowired
    private Helper helper;

    @ModelAttribute("article" )
    private Article initEntity(Model model) {
        return new Article();
    }

    /**
     * List
     **/

    @GetMapping("/index" )
    public String index(Model model) {


        int nonPublicArticleNum = articleService.countNonPublic();
        model.addAttribute("nonPublicArticleNum", nonPublicArticleNum);
        int publicArticleNum = articleService.countPublic();
        model.addAttribute("publicArticleNum", publicArticleNum);
        List<Article> articleList = articleService.findAll();
        model.addAttribute("articleList", articleList);

        return "admin/articleManage/index";
    }

    /**
     * New
     **/

    @GetMapping("/new" )
    public String newArticle(@ModelAttribute("article" ) Article article, Model model) {
        if (ObjectUtils.isEmpty(article) || !ObjectUtils.isEmpty(article.getId())) {
            article = new Article();
            model.addAttribute("article", article);
        }
        return "admin/articleManage/new";
    }

    @PostMapping("/initImage" )
    public String initImage(@ModelAttribute("article" ) Article article, @RequestParam("imageFiles" ) MultipartFile[] imageFiles, Model model) {
        List<SubContent> subContentList = article.getSubContentList();
        if (ObjectUtils.isEmpty(subContentList)) {
            subContentList = new ArrayList<>();
        }

        // save image file
        for (MultipartFile imageFile : imageFiles) {

            SubContent subContent = new SubContent();
            String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
            Image image = new Image();
            try {
                storageService.save(imageFile, fileName);
                image.setImageName(fileName);
                image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);
                subContent.setImage(image);
                subContentList.add(subContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        article.setSubContentList(subContentList);
        return newArticle(article, model);
    }


    @PostMapping(params = "cancel" )
    public String cancelInitArticle(@ModelAttribute("article" ) Article article, Model model, SessionStatus status) {
        List<SubContent> subContentList = article.getSubContentList();
        if (!ObjectUtils.isEmpty(subContentList)) {
            for (SubContent subContent : subContentList) {
                // delete image by file name
                deleteSubContentImageFile(subContent);
            }
        }
        status.setComplete();
        return index(model);
    }

    @PostMapping("/removeNewImage/{subContentIndex}" )
    public String removeNewImage(@ModelAttribute("article" ) Article article, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            SubContent subContent = article.getSubContentList().get(subContentIndex);
            // delete image file
            deleteSubContentImageFile(subContent);
            subContent.setImage(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newArticle(article, model);
    }

    @PostMapping("/removeSubContent/{subContentIndex}" )
    public String removeSubContent(@ModelAttribute("article" ) Article article, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            SubContent subContent = article.getSubContentList().get(subContentIndex);
            // delete image file
            deleteSubContentImageFile(subContent);
            article.getSubContentList().remove(subContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newArticle(article, model);
    }

    @PostMapping("/changeImage/{subContentIndex}" )
    public String changeImage(@ModelAttribute("article" ) Article article, @RequestParam("imageFile" ) MultipartFile imageFile, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            SubContent subContent = article.getSubContentList().get(subContentIndex);
            Image image = subContent.getImage();
            if (ObjectUtils.isEmpty(image)) {
                image = new Image();
                subContent.setImage(image);
            }

            String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
            storageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newArticle(article, model);
    }

    @PostMapping(params = "create" )
    public String createArticle(@ModelAttribute("article" ) Article article, BindingResult result, Model model, SessionStatus status) {

        if (result.hasErrors()) {
            return "/admin/articleManage/new";
        }
        articleService.save(article);
        status.setComplete();
        return showArticle(article.getId(), model);
    }

    /**
     * Detail
     **/

    @GetMapping("/show/{id}" )
    public String showArticle(@PathVariable("id" ) Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "/admin/articleManage/show";
    }

    @PostMapping("/disable/{id}" )
    public String disableArticle(@PathVariable("id" ) Long id, Model model) {
        Article article = articleService.findById(id);
        article.setStatus(CommonConst.FLAG_OFF);
        articleService.save(article);
        return showArticle(article.getId(), model);
    }

    @PostMapping("/enable/{id}" )
    public String enableArticle(@PathVariable("id" ) Long id, Model model) {
        Article article = articleService.findById(id);
        article.setStatus(CommonConst.FLAG_ON);
        articleService.save(article);
        return showArticle(article.getId(), model);
    }

    @PostMapping("/setOnTop/{id}" )
    public String setOnTop(@PathVariable("id" ) Long id, Model model) {
        Article article = articleService.findById(id);
        if (article.getOnTop() == CommonConst.FLAG_ON) {
            return showArticle(article.getId(), model);
        }
        article.setOnTop(CommonConst.FLAG_ON);
        articleService.save(article);
        return showArticle(article.getId(), model);
    }

    @PostMapping("/setOffTop/{id}" )
    public String setOffTop(@PathVariable("id" ) Long id, Model model) {
        Article article = articleService.findById(id);
        article.setOnTop(CommonConst.FLAG_OFF);
        articleService.save(article);
        return showArticle(article.getId(), model);
    }

    @PostMapping("/delete/{id}" )
    public String deleteArticle(@PathVariable("id" ) Long id, Model model) {

        Article article = articleService.findById(id);
        List<SubContent> subContentList = article.getSubContentList();
        for (SubContent subContent : subContentList) {
            // delete image file
            deleteSubContentImageFile(subContent);
        }
        articleService.deleteById(id);
        return "redirect:/admin/article/index";
    }

    /**
     * Edit
     **/

    @GetMapping("/edit/{id}" )
    public String editArticle(@PathVariable("id" ) Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "admin/articleManage/edit";
    }

    @PostMapping("/addImageAndSubContent" )
    public String addImageAndSubContent(@ModelAttribute("article" ) Article article, @RequestParam("imageFiles" ) MultipartFile[] imageFiles, Model model) {


        List<SubContent> subContentList = article.getSubContentList();
        if (ObjectUtils.isEmpty(subContentList)) {
            subContentList = new ArrayList<>();
        }

        // save image file
        for (MultipartFile imageFile : imageFiles) {

            SubContent subContent = new SubContent();
            String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
            Image image = new Image();
            try {
                storageService.save(imageFile, fileName);
                image.setImageName(fileName);
                image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);
                subContent.setImage(image);
                subContentList.add(subContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        article.setSubContentList(subContentList);
        articleService.save(article);
        return editArticle(article.getId(), model);
    }

    @PostMapping("/deleteImage/{subContentIndex}" )
    public String deleteImage(@ModelAttribute("article" ) Article article, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            assert article.getSubContentList() != null;
            SubContent subContent = article.getSubContentList().get(subContentIndex);
            // delete image file
            deleteSubContentImageFile(subContent);

            Image image = subContent.getImage();
            subContent.setImage(null);
            subContentService.save(subContent);

            assert image != null;
            if (!ObjectUtils.isEmpty(image.getId())) {
                imageService.deleteById(image.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editArticle(article.getId(), model);

    }

    @PostMapping("/addImage/{subContentIndex}" )
    public String addImage(@ModelAttribute("article" ) Article article, @PathVariable("subContentIndex" ) int subContentIndex, @RequestParam("imageFile" ) MultipartFile imageFile, Model model) {

        assert article.getSubContentList() != null;
        SubContent subContent = article.getSubContentList().get(subContentIndex);

        String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
        Image image = new Image();

        try {
            storageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);
            subContent.setImage(image);
            articleService.save(article);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editArticle(article.getId(), model);

    }


    @PostMapping("/deleteSubContent/{subContentIndex}" )
    public String deleteSubContent(@ModelAttribute("article" ) Article article, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            assert article.getSubContentList() != null;
            SubContent subContent = article.getSubContentList().get(subContentIndex);
            article.getSubContentList().remove(subContentIndex);
            articleService.save(article);
            deleteSubContentImageFile(subContent);
            if (!ObjectUtils.isEmpty(subContent.getId())) {
                subContentService.deleteById(subContent.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editArticle(article.getId(), model);

    }

    @PostMapping(params = "update" )
    public String updateArticle(@ModelAttribute("article" ) Article article, BindingResult result, SessionStatus sessionStatus, Model model) {

        if (result.hasErrors()) {
            return editArticle(article.getId(), model);
        }

        articleService.save(article);
        sessionStatus.setComplete();
        return showArticle(article.getId(), model);
    }

    @PostMapping(params = "cancelEdit" )
    public String cancelUpdateArticle(@ModelAttribute("article" ) Article article, BindingResult result, SessionStatus sessionStatus, Model model) {
        sessionStatus.setComplete();
        return showArticle(article.getId(), model);
    }

    /**
     * =================== PRIVATE =======================
     **/
    private void deleteSubContentImageFile(SubContent subContent) {
        Image image = subContent.getImage();
        if (!ObjectUtils.isEmpty(image)) {
            try {
                // delete image from upload file
                storageService.deleteByFileName(image.getImageName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
