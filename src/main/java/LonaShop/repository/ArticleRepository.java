package LonaShop.repository;

import LonaShop.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "select * from article where status = 1", nativeQuery = true)
    List<Article> findAvilableList();
}
