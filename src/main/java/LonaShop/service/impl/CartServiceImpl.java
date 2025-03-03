package LonaShop.service.impl;

import LonaShop.model.Cart;
import LonaShop.repository.CartRepository;
import LonaShop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

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
}
