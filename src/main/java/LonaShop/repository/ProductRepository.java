package LonaShop.repository;

import LonaShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String name);

    // status = 1: public 5: sale
    @Query(value = "SELECT * FROM products WHERE name LIKE %:searchKey% AND (status = 2 OR status = 4 OR status = 5)", nativeQuery = true)
    List<Product> findByKey(@Param("searchKey") String searchKey);
}
