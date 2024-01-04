package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.model.Category;
import LonaShop.service.CategoryService;
import LonaShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserBaseController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    protected List<Category> getListCategory() {
        return categoryService.findList();
    }

}
