package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.model.Category;
import LonaShop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public class UserBaseController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categoryList" )
    private List<Category> initCategoryList() {
        return getListCategory();
    }

    protected List<Category> getListCategory() {
        return categoryService.findList();
    }

}
