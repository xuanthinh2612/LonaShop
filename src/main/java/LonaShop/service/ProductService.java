package LonaShop.service;

import LonaShop.model.Product;

import java.util.List;

public interface ProductService {

    void save(Product product);

    List<Product> findList();

    Product findById(Long id);

    void deleteById(Long id);

    List<Product> findByProductName(String name);

    List<Product> findByKey(String searchKey);

    List<Product> findAvailableList();
    List<Product> findAvailableListByCategoryId(Long id);
}
