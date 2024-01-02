package LonaShop.service.impl;

import LonaShop.model.Product;
import LonaShop.repository.ProductRepository;
import LonaShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> findList() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findByProductName(String name) {
        return productRepository.findAllByName(name);
    }

    @Override
    public List<Product> findByKey(String searchKey) {
        return productRepository.findByKey(searchKey);
    }
}
