package LonaShop.service;

import LonaShop.model.Cart;
import LonaShop.model.CartItem;
import LonaShop.model.Product;

public interface CartService {

    void save(Cart cart);

    void deleteById(Long id);

    Cart findById(Long id);
    CartItem findCartItemByCartAndProduct(Cart cart, Product product);

}
