package LonaShop.service;

import LonaShop.model.Cart;
import LonaShop.model.Category;

import java.util.List;

public interface CartService {

    void save(Cart cart);

    void deleteById(Long id);

    Cart findById(Long id);
    void deleteCartItemById(Long CartItemId);

}
