package com.infoShare.calog.domain.Article;

import com.infoShare.calog.domain.BoardCategory.BoardCategory;
import com.infoShare.calog.domain.BoardCategory.BoardCategoryService;
import com.infoShare.calog.domain.DataNotFoundException;
import com.infoShare.calog.domain.Tag.Tag;
import com.infoShare.calog.domain.Tag.TagService;
import com.infoShare.calog.domain.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardCategoryService boardCategoryService;
    private final TagService tagService;

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


    public void createArticle(String title, String content, SiteUser author, BoardCategory boardCategory, String tag) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setBoardCategory(boardCategory);
        article.setAuthor(author);
        Set<Tag> tags = tagService.processTags(tag);
        article.setTags(tags);
        this.articleRepository.save(article);
    }

    public void viewUp(Article article) {
        article.setView(article.getView() + 1);
        this.articleRepository.save(article);
    }

    public Optional<Article> findById(Long id) {
        return this.articleRepository.findById(id);
    }


    public void modify(Article article, String title, String content, BoardCategory boardCategory, String tag) {
        article.setTitle(title);
        article.setContent(content);
        article.setBoardCategory(boardCategory);
        // 해시태그 처리
        Set<Tag> tags = tagService.processTags(tag);
        article.setTags(tags);

        this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }

    public void vote(Article article, SiteUser siteUser) {
        article.getVoter().add(siteUser);
        this.articleRepository.save(article);
    }

    public void cancelVote(Article article, SiteUser siteUser) {
        article.getVoter().remove(siteUser);
        this.articleRepository.save(article);
    } //추천 취소


    public Page<Article> searchArticlesOrTag(String keyword, String tag, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));

        if (tag != null && !tag.trim().isEmpty()) {
            tag = tag.startsWith("#") ? tag : "#" + tag; // 태그에 '#' 추가
            return articleRepository.findByTags_Name(tag, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        } else {
            return articleRepository.findAll(pageable); // 기본 목록
        }
    }

    public Page<Article> searchArticles(String keyword, int page, String boardName) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")));
        return articleRepository.findByTitleContainingOrContentContainingAndBoardCategoryId(keyword, keyword, pageable);
    }

    // 인기게시글 가져오기
    public List<Article> getPopularArticles(int limit) {
        return articleRepository.findTopPopularArticles(PageRequest.of(0, limit));
    }
}