package LonaShop.controller.user;

import LonaShop.model.Product;
import LonaShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product" )
public class ProductController extends UserBaseController {

    @Autowired
    private ProductService productService;

    @GetMapping("/show/{id}" )
    public String showProductDetail(@PathVariable("id" ) Long id, Model model) {

        try {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
            // show related product by category
            List<Product> relatedProduct = productService.findAvailableListByCategoryId(product.getCategory().getId()).stream().limit(12L).toList(); // check again
            model.addAttribute("relatedProduct", relatedProduct);

            return "/user/product/show";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
