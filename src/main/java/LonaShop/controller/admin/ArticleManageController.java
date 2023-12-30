package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.Article;
import LonaShop.model.Image;
import LonaShop.model.Product;
import LonaShop.model.SubContent;
import LonaShop.service.ArticleService;
import LonaShop.service.ImageService;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/article")
@SessionAttributes("article")
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
    private Helper helper;

    @ModelAttribute("article")
    private Article initEntity(Model model) {
        return new Article();
    }

    /**
     * List
     **/

    @GetMapping("/list")
    public String index(Model model) {

        List<Article> articleList = articleService.findAll();
        model.addAttribute("articleList", articleList);

        return "admin/articleManage/index";
    }

    /**
     * New
     **/

    @GetMapping("/new")
    public String newArticle(@ModelAttribute("article") Article article, Model model) {
        if (ObjectUtils.isEmpty(article) || !ObjectUtils.isEmpty(article.getId())) {
            article = new Article();
            model.addAttribute("article", article);
        }
        return "admin/articleManage/new";
    }

    @PostMapping("/initImage")
    public String initImage(@ModelAttribute("article") Article article, @RequestParam("imageFiles") MultipartFile[] imageFiles, Model model) {
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
                image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
                subContent.setImage(image);
                subContentList.add(subContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        article.setSubContentList(subContentList);
        return newArticle(article, model);
    }


    @PostMapping(params = "cancel")
    public String cancelInitArticle(@ModelAttribute("article") Article article, Model model, SessionStatus status) {
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

    @PostMapping("/removeNewImage/{subContentIndex}")
    public String removeNewImage(@ModelAttribute("article") Article article, @PathVariable("subContentIndex") int subContentIndex, Model model) {
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

    @PostMapping("/removeSubContent/{subContentIndex}")
    public String removeSubContent(@ModelAttribute("article") Article article, @PathVariable("subContentIndex") int subContentIndex, Model model) {
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

    @PostMapping("/changeImage/{subContentIndex}")
    public String changeImage(@ModelAttribute("article") Article article, @RequestParam("imageFile") MultipartFile imageFile, @PathVariable("subContentIndex") int subContentIndex, Model model) {
        try {
            SubContent subContent = article.getSubContentList().get(subContentIndex);
            Image image = subContent.getImage();
            if (ObjectUtils.isEmpty(image)) {
                image = new Image();
                subContent.setImage(image);
            }
            // delete old image from upload file before set new image TODO

            String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "-" + imageFile.getOriginalFilename();

            storageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newArticle(article, model);
    }

    @PostMapping(params = "create")
    public String createArticle(@ModelAttribute("article") Article article, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "/admin/article/new";
        }
        articleService.save(article);

        return editArticle(article.getId(), model);
    }


    /**
     * Detail
     **/
    @PostMapping("/addImage")
    public String addImage(@ModelAttribute("article") Article article, @RequestParam("imageFiles") MultipartFile[] imageFiles, BindingResult result, Model model) {

        List<Image> imageList = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "-" + file.getOriginalFilename();
            Image image = new Image();
            try {
                storageService.save(file, fileName);
                image.setImageName(fileName);
                image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
                imageList.add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        if (ObjectUtils.isEmpty(article.getImageList())) {
//            article.setImageList(new ArrayList<>());
//        }
//        article.getImageList().addAll(imageList);
//        articleService.save(article);
        return editArticle(article.getId(), model);
    }

    @PostMapping("/deleteImage/{indexImage}")
    public String deleteImage(@ModelAttribute("article") Article article, @PathVariable("indexImage") int indexImage, BindingResult result, Model model) {

        try {
//            Image image = article.getImageList().get(indexImage);
//            article.getImageList().remove(image);
//            imageService.deleteById(image.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return editArticle(article.getId(), model);
    }

    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "admin/articleManage/edit";
    }

    @PostMapping("/update")
    public String updateArticle(@ModelAttribute("article") Article article, BindingResult result, SessionStatus sessionStatus, Model model) {

        if (result.hasErrors()) {
            return editArticle(article.getId(), model);
        }

        articleService.save(article);
        sessionStatus.setComplete();
        return "redirect:/admin/article/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id, Model model) {
        articleService.deleteById(id);
        return "redirect:/admin/article/list";
    }

    @PostMapping("/disable/{id}")
    public String disableArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);
        article.setStatus(CommonConst.FLAG_OFF);
        articleService.save(article);
        return "redirect:/admin/article/list";
    }

    @PostMapping("/enable/{id}")
    public String enableArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);
        article.setStatus(CommonConst.FLAG_ON);
        articleService.save(article);
        return "redirect:/admin/article/list";
    }

    @PostMapping("/show/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "/admin/article/show";
    }

    @PostMapping("/setOnTop/{id}")
    public String setOnTop(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);
        if (article.getStatus() == CommonConst.FLAG_OFF) {
            return "redirect:/admin/article/list";
        }
        article.setOnTop(CommonConst.FLAG_ON);
        articleService.save(article);
        return "redirect:/admin/article/list";
    }

    @PostMapping("/setOffTop/{id}")
    public String setOffTop(@PathVariable("id") Long id, Model model) {
        Article article = articleService.findById(id);
        article.setOnTop(CommonConst.FLAG_OFF);
        articleService.save(article);
        return "redirect:/admin/article/list";
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
