package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.dto.CartItemUpdateRequest;
import LonaShop.dto.CartItemUpdateResponse;
import LonaShop.model.*;
import LonaShop.service.CartService;
import LonaShop.service.ProductService;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin("*")
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

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng giỏ hàng.");
            return "redirect:/login";
        }

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
        calculateCartAmount(myCart);
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
            attributes.addFlashAttribute("warningMsg", "Lỗi không thể xóa sản phẩm khỏi giỏ hàng.");
            return "redirect:/cart/show";

        }

        myCart.getCartItems().remove(cartItem);
        calculateCartAmount(myCart);
        cartService.deleteCartItemById(cartItemId);
        cartService.save(myCart);

        attributes.addAttribute("msg", "Xóa sảm phầm khỏi giỏ hàng thành công.");
        model.addAttribute("cart", myCart);

        return "/user/cart/show";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<CartItemUpdateResponse> UpdateCart(@RequestBody CartItemUpdateRequest request,
                                                             RedirectAttributes attributes, Model model) {
        Long cartItemId = request.getCartItemId();
        Long quantity = request.getQuantity();

        CartItemUpdateResponse cartItemResponse = new CartItemUpdateResponse();

        User currentUser = getCurrentLoggedInUser();

        if (ObjectUtils.isEmpty(currentUser)) {
            cartItemResponse.setOk(false);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.FORBIDDEN);
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
            cartItemResponse.setOk(false);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.BAD_REQUEST);
        }

        cartItem.setQuantity(quantity);
        cartItem.setSubAmount(quantity * cartItem.getPriceAtTime());
        cartService.deleteCartItemById(cartItemId);

        calculateCartAmount(myCart);
        cartService.save(myCart);

        cartItemResponse.setCartItemId(cartItemId);
        cartItemResponse.setQuantity(cartItem.getQuantity());

        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    private void calculateCartAmount(Cart myCart) {
        List<CartItem> cartItemList = myCart.getCartItems();
        long totalAmount = 0;
        for (CartItem item :
                cartItemList) {
            totalAmount += item.getPriceAtTime() * item.getQuantity();
        }
        myCart.setTotalAmount(totalAmount);
    }
}
