package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Page<Article> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.articleRepository.findAll(pageable);
    }

    public Article getArticleById(Long id) {
        Optional<Article> article = this.articleRepository.findById(id);
        if (article.isPresent()) {
            if (article.get().getAuthor() == null) {
                throw new DataNotFoundException("Author not found for article");
            }
            return article.get();
        } else {
            throw new DataNotFoundException("Article not found");
        }
    }


    public void createArticle(String title, String content, SiteUser author) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(author);
        this.articleRepository.save(article);
    }

    public void viewUp(Article article) {
        article.setView(article.getView() + 1);
        this.articleRepository.save(article);
    }

    public Optional<Article> findById(Long id) {
        return this.articleRepository.findById(id);
    }


    public void modify(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);
        this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }

    public void vote(Article article, SiteUser siteUser) {
        article.getVoter().add(siteUser);
        this.articleRepository.save(article);
    }

    public Page<Article> searchArticles(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));
        return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }
}
