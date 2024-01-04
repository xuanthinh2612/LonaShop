package LonaShop.service;

import LonaShop.model.Article;

import java.util.List;

public interface ArticleService extends CommonService<Article> {
    List<Article> findAvailableList();
}
