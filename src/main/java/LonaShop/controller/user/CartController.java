package LonaShop.controller.user;

import LonaShop.controller.BaseController;
import LonaShop.dto.CartItemUpdateRequest;
import LonaShop.dto.CartItemUpdateResponse;
import LonaShop.model.*;
import LonaShop.service.CartService;
import LonaShop.service.ProductService;
import LonaShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@CrossOrigin("*")
@RequestMapping("/cart")
public class CartController extends BaseController {
    private final MessageSource messageSource;

    public CartController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("")
    public String myCart(RedirectAttributes attributes, Model model) {

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", "Bạn vui lòng đăng nhập để sử dụng chức năng giỏ hàng.");
            return "redirect:/login";
        }

        if (ObjectUtils.isEmpty(currentUser.getCart())) {
            Cart cart = new Cart();
            cart.setCartItems(new ArrayList<CartItem>());
            currentUser.setCart(cart);
            userService.updateUser(currentUser);
        }

        model.addAttribute("cart", currentUser.getCart());

        return "/user/cart/show";
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<CartItemUpdateResponse> addToCart(@RequestBody CartItemUpdateRequest request, Locale locale) {
        Long productId = request.getProductId();
        Long quantity = request.getQuantity();
        CartItemUpdateResponse cartItemResponse = new CartItemUpdateResponse();

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            cartItemResponse.setErrorMessage( messageSource.getMessage("cart.auth.error", null, locale));
            cartItemResponse.setOk(false);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
        }

        Product product = productService.findById(productId);
        if (ObjectUtils.isEmpty(product)) {
            cartItemResponse.setErrorMessage( messageSource.getMessage("product.error", null, locale));
            cartItemResponse.setOk(false);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
        }

        Cart myCart = currentUser.getCart();
        if (ObjectUtils.isEmpty(myCart)) {
            Cart cart = new Cart();
            cart.setCartItems(new ArrayList<>());
            currentUser.setCart(cart);
            userService.updateUser(currentUser);
        }

        CartItem existCartItem = cartService.findCartItemByCartAndProduct(myCart, product);
        if (!ObjectUtils.isEmpty(existCartItem)) {
            cartItemResponse.setOk(false);
            cartItemResponse.setErrorMessage("Sản phẩm đã trong giỏ hàng.");
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
        }

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

        cartItemResponse.setOk(true);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<CartItemUpdateResponse> deleteCartItemFromCart(@RequestBody CartItemUpdateRequest request,
                                                                         RedirectAttributes attributes, Model model) {
        Long cartItemId = request.getCartItemId();
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
//            attributes.addFlashAttribute("warningMsg", "Lỗi không thể xóa sản phẩm khỏi giỏ hàng.");
        }

        myCart.getCartItems().remove(cartItem);
        calculateCartAmount(myCart);
        cartService.save(myCart);

        cartItemResponse.setOk(true);

        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    @PutMapping("")
    @ResponseBody
    public ResponseEntity<CartItemUpdateResponse> UpdateCart(@RequestBody CartItemUpdateRequest request) {
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
