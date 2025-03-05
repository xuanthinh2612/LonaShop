package LonaShop.service.impl;

import LonaShop.model.Cart;
import LonaShop.model.CartItem;
import LonaShop.repository.CartItemRepository;
import LonaShop.repository.CartRepository;
import LonaShop.service.CartService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);

    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    public void deleteCartItemById(Long CartItemId) {
        cartItemRepository.deleteById(CartItemId);
    }

    @Override
    public void deleteAllCartItemList(List<CartItem> cartItemList) {
        List<CartItem> list2 = cartItemRepository.findAll();
        cartItemRepository.deleteAll(list2);
    }
}
