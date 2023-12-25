package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.model.Category;
import LonaShop.model.Image;
import LonaShop.model.Product;
import LonaShop.repository.ImageRepository;
import LonaShop.service.CategoryService;
import LonaShop.service.ImageService;
import LonaShop.service.ProductService;
import LonaShop.service.file.FilesStorageService;
import jakarta.validation.Valid;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    public String newProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "admin/product/new";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, @RequestParam("imagesFiles") MultipartFile[] imagesFiles, Model model) {
        // check valid model
        if (result.hasErrors()) {
            return "admin/product/new";
        }

        List<Image> images = new ArrayList<>();

        // save file
        for (MultipartFile file : imagesFiles) {

            Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
            String fileName = timestamp + "-" + file.getOriginalFilename();

            Image image = new Image();
            try {
                storageService.save(file, fileName);
                image.setImageName(fileName);
                image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
                image.setProduct(product);
                images.add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        product.setImages(images);
        productService.save(product);
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
        return "admin/product/show";
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

        String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "-" + imageFile.getOriginalFilename();
        Image image = new Image();

        try {
            storageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
            image.setProduct(product);
            product.getImages().add(image);
            productService.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }

    @PostMapping("/deleteImage/{imageIndex}")
    public String deleteImage(@ModelAttribute("product") Product product, @PathVariable("imageIndex") int imageIndex, Model model) {
        try {
            Image image = product.getImages().get(imageIndex);
            product.getImages().remove(image);
            imageService.deleteById(image.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return editProduct(product.getId(), model);

    }


}
