package LonaShop.service;

import java.util.List;

public interface CommonService<T> {
    void save(T t);
    T findById(Long id);
    void deleteById(Long id);
    List<T> findAll();

}
