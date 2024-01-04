package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.*;
import LonaShop.repository.ImageRepository;
import LonaShop.service.CategoryService;
import LonaShop.service.ImageService;
import LonaShop.service.ProductService;
import LonaShop.service.SubContentService;
import LonaShop.service.file.FilesStorageService;
import jakarta.validation.Valid;
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
@RequestMapping("admin/product" )
@SessionAttributes("product" )
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

    @ModelAttribute("categoryList" )
    public List<Category> initListCategory() {
        return categoryService.findList();
    }

    @ModelAttribute("product" )
    private Product initEntity() {
        return new Product();
    }

    @Autowired
    FilesStorageService storageService;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * List
     **/

    @GetMapping("/index" )
    public String index(Model model) {
        List<Product> productList = productService.findList();
        model.addAttribute("productList", productList);
        int userViewAbleProNum = productService.countAvailable();
        model.addAttribute("userViewAbleProNum", userViewAbleProNum);
        int disabledNum = productService.countByStatus(CommonConst.ProductStatus.banded.code());
        model.addAttribute("disabledNum", disabledNum);
        int pendingNum = productService.countByStatus(CommonConst.ProductStatus.pending.code());
        model.addAttribute("pendingNum", pendingNum);
        int soldOutNum = productService.countByStatus(CommonConst.ProductStatus.soldOut.code());
        model.addAttribute("soldOutNum", soldOutNum);
        int saleProNum = productService.countByStatus(CommonConst.ProductStatus.sale.code());
        model.addAttribute("saleProNum", saleProNum);
        int normalProNum = productService.countByStatus(CommonConst.ProductStatus.available.code());
        model.addAttribute("normalProNum", normalProNum);
        return "admin/product/index";
    }

    /**
     * New
     **/

    @GetMapping("/new" )
    public String newProduct(@ModelAttribute("product" ) Product product, Model model) {
        if (ObjectUtils.isEmpty(product) || !ObjectUtils.isEmpty(product.getId())) {
            product = new Product();
            model.addAttribute("product", product);
        }
        return "admin/product/new";
    }

    @PostMapping("/initImage" )
    public String initImage(@ModelAttribute("product" ) Product product, @RequestParam("imageFiles" ) MultipartFile[] imageFiles, Model model) {
        List<SubContent> subContentList = product.getSubContentList();
        if (ObjectUtils.isEmpty(subContentList)) {
            subContentList = new ArrayList<>();
        }

        // save image file
        for (MultipartFile file : imageFiles) {

            SubContent subContent = new SubContent();
            String fileName = helper.genRandomFileName(file.getOriginalFilename());

            Image image = new Image();
            try {
                storageService.save(file, fileName);
                image.setImageName(fileName);
                image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);
                subContent.setImage(image);
                subContentList.add(subContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        product.setSubContentList(subContentList);
        return newProduct(product, model);
    }

    @PostMapping("/cancel" )
    public String cancelInitProduct(@ModelAttribute("product" ) Product product, Model model, SessionStatus status) {
        List<SubContent> subContentList = product.getSubContentList();
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
    public String removeNewImage(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            // delete image file
            deleteSubContentImageFile(subContent);
            subContent.setImage(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newProduct(product, model);
    }

    @PostMapping("/removeSubContent/{subContentIndex}" )
    public String removeSubContent(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            // delete image file
            deleteSubContentImageFile(subContent);
            product.getSubContentList().remove(subContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newProduct(product, model);
    }

    @PostMapping("/changeImage/{subContentIndex}" )
    public String changeImage(@ModelAttribute("product" ) Product product, @RequestParam("imageFile" ) MultipartFile imageFile, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            SubContent subContent = product.getSubContentList().get(subContentIndex);
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
        return newProduct(product, model);
    }

    @PostMapping("/create" )
    public String createProduct(@Valid @ModelAttribute("product" ) Product product, BindingResult result, Model model, SessionStatus status) {
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

    /**
     * Edit
     **/

    @GetMapping("/edit/{id}" )
    public String editProduct(@PathVariable("id" ) Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return index(model);
        }
        model.addAttribute("product", product);
        return "admin/product/edit";
    }

    @PostMapping("/update" )
    public String editProduct(@ModelAttribute("product" ) Product product, BindingResult result, SessionStatus sessionStatus, Model model) {
        if (result.hasErrors()) {
            return "admin/product/edit";
        }
        productService.save(product);
        sessionStatus.setComplete();
        model.addAttribute("product", product);
        return showDetail(product.getId(), model);
    }

    @PostMapping("/addImage" )
    public String addImage(@ModelAttribute("product" ) Product product, @RequestParam("imageFile" ) MultipartFile imageFile, Model model) {

        SubContent subContent = new SubContent();
        product.getSubContentList().add(subContent);

        String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
        Image image = new Image();

        try {
            storageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);
            subContent.setImage(image);
            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/replaceImage/{subContentIndex}" )
    public String replaceImage(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, @RequestParam("newImageFile" ) MultipartFile newImageFile, Model model) {

        assert product.getSubContentList() != null;
        SubContent subContent = product.getSubContentList().get(subContentIndex);

        String fileName = helper.genRandomFileName(newImageFile.getOriginalFilename());
        Image image = new Image();

        try {
            storageService.save(newImageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path" ) + "/" + fileName);
            subContent.setImage(image);
            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/deleteImage/{subContentIndex}" )
    public String deleteImage(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            assert product.getSubContentList() != null;
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            Image image = subContent.getImage();
            // delete image file
            deleteSubContentImageFile(subContent);
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

    @PostMapping("/deleteSubContent/{subContentIndex}" )
    public String deleteSubContent(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            assert product.getSubContentList() != null;
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            product.getSubContentList().remove(subContentIndex);
            productService.save(product);
            deleteSubContentImageFile(subContent);
            if (!ObjectUtils.isEmpty(subContent.getId())) {
                subContentService.deleteById(subContent.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/setImageAsAvatar/{subContentIndex}" )
    public String setImageAsAvatar(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            assert product.getSubContentList() != null;
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            Image image = subContent.getImage();
            assert image != null;
            image.setAvatar(true);
            imageService.save(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/removeImageFromAvatar/{subContentIndex}" )
    public String removeImageFromAvatar(@ModelAttribute("product" ) Product product, @PathVariable("subContentIndex" ) int subContentIndex, Model model) {
        try {
            assert product.getSubContentList() != null;
            SubContent subContent = product.getSubContentList().get(subContentIndex);
            Image image = subContent.getImage();
            assert image != null;
            image.setAvatar(false);
            imageService.save(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    /**
     * Detail
     **/

    @GetMapping("/show/{id}" )
    public String showDetail(@PathVariable("id" ) Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return index(model);
        }
        model.addAttribute("product", product);
        return "admin/product/show";
    }

    @PostMapping("/delete/{id}" )
    public String deleteProduct(@PathVariable("id" ) Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return index(model);
        }
        List<SubContent> subContentList = product.getSubContentList();
        for (SubContent subContent : subContentList) {
            // delete image file
            deleteSubContentImageFile(subContent);
        }
        productService.deleteById(id);
        return "redirect:/admin/product/index";
    }

    @PostMapping("/public/{id}" )
    public String publicProduct(@PathVariable("id" ) Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/index";
        }
        product.setStatus(CommonConst.ProductStatus.available.code());
        productService.save(product);
        return "redirect:/admin/product/index";
    }

    @PostMapping("/onSale/{id}" )
    public String onSaleProduct(@PathVariable("id" ) Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/index";
        }
        product.setStatus(CommonConst.ProductStatus.sale.code());
        productService.save(product);
        return "redirect:/admin/product/index";
    }

    @PostMapping("/offProduct/{id}" )
    public String offProduct(@PathVariable("id" ) Long id, Model mode) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/index";
        }
        product.setStatus(CommonConst.ProductStatus.banded.code());
        productService.save(product);
        return "redirect:/admin/product/index";
    }

    @PostMapping("/setSoldOut/{id}" )
    public String setSoldOut(@PathVariable("id" ) Long id, Model mode) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product/index";
        }
        product.setStatus(CommonConst.ProductStatus.soldOut.code());
        productService.save(product);
        return "redirect:/admin/product/index";
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
