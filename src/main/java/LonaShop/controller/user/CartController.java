package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.model.Cart;
import LonaShop.model.User;
import LonaShop.model.UserDto;
import LonaShop.service.CartService;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @GetMapping("/show")
    public String myCart(RedirectAttributes attributes, Model model) {

        UserDto userDto = getCurrentLoggedInUserDto();
        if (ObjectUtils.isEmpty(userDto)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng giỏ hàng.");
            return "redirect:/login";
        }

        User user = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(user.getCart())) {
            Cart cart = new Cart();
            user.setCart(cart);
            userService.updateUser(user);
        }

        return "/user/cart/show";
    }
}
