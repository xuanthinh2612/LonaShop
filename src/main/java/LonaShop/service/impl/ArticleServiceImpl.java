package LonaShop.service.impl;

import LonaShop.model.Article;
import LonaShop.repository.ArticleRepository;
import LonaShop.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void save(Article article) {
        article.setCreatedAt(new Date());
        articleRepository.save(article);
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> findAvailableList() {
        return articleRepository.findAvilableList();
    }
}
