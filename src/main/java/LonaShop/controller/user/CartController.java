package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.model.*;
import LonaShop.service.CartService;
import LonaShop.service.ProductService;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/show")
    public String myCart(RedirectAttributes attributes, Model model) {

        UserDto userDto = getCurrentLoggedInUserDto();
        if (ObjectUtils.isEmpty(userDto)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng giỏ hàng.");
            return "redirect:/login";
        }

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser.getCart())) {
            Cart cart = new Cart();
            currentUser.setCart(cart);
            userService.updateUser(currentUser);
        }

        model.addAttribute("cart", currentUser.getCart());

        return "/user/cart/show";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("product") Long productId,
                            @RequestParam("inputQuantity") Long quantity,
                            RedirectAttributes attributes, Model model) {
        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng giỏ hàng.");
            return "redirect:/login";
        }

        Product product = productService.findById(productId);
        if (ObjectUtils.isEmpty(product)) {
            attributes.addFlashAttribute("warningMsg", "Sản phẩm không tồn tại! Vui lòng kiểm tra lại.");
            return "redirect:/login";
        }

        if (ObjectUtils.isEmpty(currentUser.getCart())) {
            Cart cart = new Cart();
            currentUser.setCart(cart);
            userService.updateUser(currentUser);
        }

        Cart myCart = currentUser.getCart();
        CartItem cartItem = new CartItem();
        cartItem.setCart(myCart);
        cartItem.setProduct(product);
        cartItem.setPriceAtTime(product.getCurrentPrice());
        cartItem.setQuantity(quantity);
        cartItem.setSubAmount(product.getCurrentPrice() * quantity);
        cartItem.setCreatedAt(new Date());
        cartItem.setUpdatedAt(new Date());

        myCart.getCartItems().add(cartItem);
        cartService.save(myCart);

        model.addAttribute("cart", myCart);

        return "/user/cart/show";
    }

    @PostMapping("/delete")
    public String deleteCartItemFromCart(@RequestParam("cartItemId") Long cartItemId,
                                         RedirectAttributes attributes, Model model) {
        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng giỏ hàng.");
            return "redirect:/login";
        }

        Cart myCart = currentUser.getCart();
        CartItem cartItem = null;

        for (int i = 0; i < myCart.getCartItems().size(); i++) {
            CartItem c = myCart.getCartItems().get(i);
            if (c.getId().equals(cartItemId)) {
                cartItem = c;
                break;
            }
        }
        if (ObjectUtils.isEmpty(cartItem)) {
            model.addAttribute("cart", myCart);
            attributes.addFlashAttribute("warningMsg", "Lỗi không thể xóa sản phẩm khỏi giỏ hàng.");
            return "/user/cart/show";

        }

        myCart.getCartItems().remove(cartItem);
        cartService.deleteCartItemById(cartItemId);

        attributes.addFlashAttribute("msg", "Xóa sảm phầm khỏi giỏ hàng thành công.");
        model.addAttribute("cart", myCart);

        return "/user/cart/show";
    }
}
