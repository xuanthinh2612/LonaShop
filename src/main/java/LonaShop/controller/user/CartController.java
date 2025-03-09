package LonaShop.controller.user;

import LonaShop.common.CommonConst;
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
    public String myCart(RedirectAttributes attributes, Model model, Locale locale) {

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("cart.auth.error", null, locale));
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
            cartItemResponse.setErrorMessage(messageSource.getMessage("cart.auth.error", null, locale));
            cartItemResponse.setSuccess(false);
            cartItemResponse.setAuthError(true);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
        }

        Product product = productService.findById(productId);
        if (isNotValidProduct(product)) {
            cartItemResponse.setErrorMessage(messageSource.getMessage("product.error", null, locale));
            cartItemResponse.setSuccess(false);
            cartItemResponse.setProductError(true);
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
            cartItemResponse.setSuccess(false);
            cartItemResponse.setProductError(true);
            cartItemResponse.setErrorMessage(messageSource.getMessage("productExist.message", null, locale));
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

        cartItemResponse.setSuccess(true);
        cartItemResponse.setSuccessMessage(messageSource.getMessage("productAddedSuccess.message", null, locale));
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    @PostMapping(value = "", params = "buy")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") Long quantity,
                            Locale locale,
                            RedirectAttributes attributes, Model model) {
        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("cart.auth.error", null, locale));
            return "redirect:/login";
        }

        Product product = productService.findById(productId);
        if (isNotValidProduct(product)) {
            attributes.addFlashAttribute("warningMsg", messageSource.getMessage("product.error", null, locale));
            return "redirect:/";
        }

        if (ObjectUtils.isEmpty(currentUser.getCart())) {
            Cart cart = new Cart();
            cart.setCartItems(new ArrayList<CartItem>());
            currentUser.setCart(cart);
            userService.updateUser(currentUser);
        }

        Cart myCart = currentUser.getCart();
        CartItem existCartItem = cartService.findCartItemByCartAndProduct(myCart, product);
        if (!ObjectUtils.isEmpty(existCartItem)) {
            return "redirect:/cart";
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

        return "redirect:/cart";
    }


    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<CartItemUpdateResponse> deleteCartItemFromCart(@RequestBody CartItemUpdateRequest request,
                                                                         Locale locale) {
        Long cartItemId = request.getCartItemId();
        CartItemUpdateResponse cartItemResponse = new CartItemUpdateResponse();

        User currentUser = getCurrentLoggedInUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            cartItemResponse.setErrorMessage(messageSource.getMessage("cart.auth.error", null, locale));
            cartItemResponse.setSuccess(false);
            cartItemResponse.setAuthError(true);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
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
            cartItemResponse.setSuccess(false);
            cartItemResponse.setProductError(true);
            cartItemResponse.setErrorMessage(messageSource.getMessage("productDelete.error", null, locale));
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
        }

        myCart.getCartItems().remove(cartItem);
        calculateCartAmount(myCart);
        cartService.save(myCart);

        cartItemResponse.setSuccess(true);
        cartItemResponse.setSuccessMessage(messageSource.getMessage("productDelete.success", null, locale));
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }

    @PutMapping("")
    @ResponseBody
    public ResponseEntity<CartItemUpdateResponse> UpdateCart(@RequestBody CartItemUpdateRequest request, Locale locale) {
        Long cartItemId = request.getCartItemId();
        Long quantity = request.getQuantity();

        CartItemUpdateResponse cartItemResponse = new CartItemUpdateResponse();

        User currentUser = getCurrentLoggedInUser();

        if (ObjectUtils.isEmpty(currentUser)) {
            cartItemResponse.setErrorMessage(messageSource.getMessage("cart.auth.error", null, locale));
            cartItemResponse.setSuccess(false);
            cartItemResponse.setAuthError(true);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
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
        if (ObjectUtils.isEmpty(cartItem) || ObjectUtils.isEmpty(quantity)) {
            cartItemResponse.setErrorMessage(messageSource.getMessage("product.error", null, locale));
            cartItemResponse.setSuccess(false);
            cartItemResponse.setProductError(true);
            return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
        }

        cartItem.setQuantity(quantity);
        cartItem.setSubAmount(quantity * cartItem.getPriceAtTime());

        calculateCartAmount(myCart);
        cartService.save(myCart);

        cartItemResponse.setSuccess(true);
        cartItemResponse.setSuccessMessage(messageSource.getMessage("productUpdate.success", null, locale));
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

    private boolean isNotValidProduct(Product product) {
        return ObjectUtils.isEmpty(product) || product.getStatus() != CommonConst.ProductStatus.available.code()
                && product.getStatus() != CommonConst.ProductStatus.sale.code();
    }

}
