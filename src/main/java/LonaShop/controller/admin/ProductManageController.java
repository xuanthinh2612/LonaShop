package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.Category;
import LonaShop.model.Image;
import LonaShop.model.Product;
import LonaShop.model.SubContent;
import LonaShop.repository.ImageRepository;
import LonaShop.service.CategoryService;
import LonaShop.service.ImageService;
import LonaShop.service.ProductService;
import LonaShop.service.SubContentService;
import LonaShop.service.file.FilesStorageService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("admin/product")
@SessionAttributes("product")
public class ProductManageController extends AdminBaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Environment environment;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SubContentService subContentService;

    @Autowired
    private Helper helper;

    @ModelAttribute("categoryList")
    public List<Category> initListCategory() {
        return categoryService.findList();
    }

    @ModelAttribute("product")
    private Product initEntity() {
        return new Product();
    }

    @Autowired
    FilesStorageService storageService;
    @Autowired
    private ImageRepository imageRepository;


    @GetMapping("/index")
    public String index(Model model) {
        List<Product> productList = productService.findList();
        model.addAttribute("productList", productList);
        return "admin/product/index";
    }

    @GetMapping("/new")
    public String newProduct(@ModelAttribute("product") Product product, Model model) {
        if (ObjectUtils.isEmpty(product)) {
            product = new Product();
            model.addAttribute("product", product);
        }
        return "admin/product/new";
    }

    @PostMapping("/initImage")
    public String initImage(@ModelAttribute("product") Product product,
                            @RequestParam("imagesFiles") MultipartFile[] imagesFiles, Model model) {
        List<SubContent> subContentList = product.getSubContentList();
        if (ObjectUtils.isEmpty(subContentList)) {
            subContentList = new ArrayList<>();
        }

        // save file
        for (MultipartFile file : imagesFiles) {

            SubContent subContent = new SubContent();
            Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
            String fileName = timestamp + "-" + file.getOriginalFilename();

            Image image = new Image();
            try {
                storageService.save(file, fileName);
                image.setImageName(fileName);
                image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
                subContent.setImage(image);
                subContentList.add(subContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        product.setSubContentList(subContentList);
        return newProduct(product, model);
    }

    @PostMapping("/cancel")
    public String cancelInitProduct(@ModelAttribute("product") Product product, Model model, SessionStatus status) {
        List<SubContent> subContentList = product.getSubContentList();
        if (!ObjectUtils.isEmpty(subContentList)) {
            for (int i = 0; i < subContentList.size(); i++) {
                // delete image with file name TODO
            }
        }
        status.setComplete();
        return index(model);
    }

    @PostMapping("/removeNewImage/{subContentIndex}")
    public String removeNewImage(@ModelAttribute("product") Product product, @PathVariable("subContentIndex") int subContentIndex, Model model) {
        try {
//            Image image = product.getSubContentList().get(subContentIndex).getImage();
            product.getSubContentList().get(subContentIndex).setImage(null);
            // delete image from upload file TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newProduct(product, model);
    }

    @PostMapping("/removeSubContent/{subContentIndex}")
    public String removeSubContent(@ModelAttribute("product") Product product, @PathVariable("subContentIndex") int subContentIndex, Model model) {
        try {
//            Image image = product.getSubContentList().get(subContentIndex).getImage();
            product.getSubContentList().remove(subContentIndex);
            // delete image from upload file TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newProduct(product, model);
    }

    @PostMapping("/changeImage/{subContentIndex}")
    public String changeImage(@ModelAttribute("product") Product product, @RequestParam("imageFile") MultipartFile imageFile,
                              @PathVariable("subContentIndex") int subContentIndex, Model model) {
        try {
            SubContent subContent = product.getSubContentList().get(subContentIndex);
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
        return newProduct(product, model);
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") Product product, BindingResult result,
                                Model model, SessionStatus status) {
        // check valid model
        if (result.hasErrors()) {
            return "admin/product/new";
        }
        try {
            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        status.setComplete();
        return "redirect:/admin/product/index";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return index(model);
        }
        model.addAttribute("product", product);
        return "admin/product/edit";
    }

    @PostMapping("/update")
    public String editProduct(@ModelAttribute("product") Product product, BindingResult result, SessionStatus sessionStatus, Model model) {
        if (result.hasErrors()) {
            return "admin/product/edit";
        }
        productService.save(product);
        sessionStatus.setComplete();
        model.addAttribute("product", product);
        return showDetail(product.getId(), model);
    }

    @GetMapping("/show/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return index(model);
        }
        model.addAttribute("product", product);
        return "admin/product/show";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return index(model);
        }
        productService.deleteById(id);
        return "redirect:/admin/product/index";
    }

    @PostMapping("/public/{id}")
    public String publicProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/index";
        }
        product.setStatus(CommonConst.ProductStatus.available.code());
        productService.save(product);
        return "redirect:/admin/product/index";
    }

    @PostMapping("/onSale/{id}")
    public String onSaleProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/index";
        }
        product.setStatus(CommonConst.ProductStatus.sale.code());
        productService.save(product);
        return "redirect:/admin/product/index";
    }


    @PostMapping("/addImage")
    public String addImage(@ModelAttribute("product") Product product, @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        SubContent subContent = new SubContent();
        product.getSubContentList().add(subContent);

        String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
        Image image = new Image();

        try {
            storageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
            subContent.setImage(image);
            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/replaceImage/{subContentIndex}")
    public String replaceImage(@ModelAttribute("product") Product product,
                               @PathVariable("subContentIndex") int subContentIndex,
                               @RequestParam("newImageFile") MultipartFile newImageFile, Model model) {

        assert product.getSubContentList() != null;
        SubContent subContent = product.getSubContentList().get(subContentIndex);

        String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "-" + newImageFile.getOriginalFilename();
        Image image = new Image();

        try {
            storageService.save(newImageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
            subContent.setImage(image);
            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/deleteImage/{subContentIndex}")
    public String deleteImage(@ModelAttribute("product") Product product, @PathVariable("subContentIndex") int subContentIndex, Model model) {
        try {
            assert product.getSubContentList() != null;
            SubContent subContent = product.getSubContentList().get(subContentIndex);
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

        return editProduct(product.getId(), model);

    }

    @PostMapping("/deleteSubContent/{subContentIndex}")
    public String deleteSubContent(@ModelAttribute("product") Product product, @PathVariable("subContentIndex") int subContentIndex, Model model) {
        try {
            assert product.getSubContentList() != null;
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            product.getSubContentList().remove(subContentIndex);
            productService.save(product);
            if (!ObjectUtils.isEmpty(subContent.getId())) {
                subContentService.deleteById(subContent.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

}
