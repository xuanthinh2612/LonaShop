package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.model.Category;
import LonaShop.model.UserDto;
import LonaShop.service.CategoryService;
import LonaShop.service.ProductService;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public class UserBaseController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    private UserDto getCurrentUserDto() {
        return getCurrentLoggedInUserDto();
    }

    protected List<Category> getListCategory() {
        return categoryService.findList();
    }

    private UserDto getCurrentLoggedInUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            userDto = userService.findUserDtoByEmail(currentUserEmail);
        }
        return userDto;
    }

}
