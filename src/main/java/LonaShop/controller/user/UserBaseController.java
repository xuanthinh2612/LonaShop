package LonaShop.controller.user;

import LonaShop.common.CommonConst;
import LonaShop.controller.BaseController;
import LonaShop.model.Category;
import LonaShop.model.Product;
import LonaShop.service.CategoryService;
import LonaShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserBaseController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    protected List<Product> getAvailableProduct() {
        List<Product> productList = productService.findList(); //.stream().filter(e -> e.getImages().size() > 0).toList();
        List<Product> newList = new ArrayList<>();

        for (Product product : productList) {
            if (product.getStatus() == CommonConst.ProductStatus.available.code()
                    || product.getStatus() == CommonConst.ProductStatus.sale.code()) {
                newList.add(product);
            }
        }
        return newList;
    }

    protected List<Category> getListCategory() {
        return categoryService.findList();
    }

}
