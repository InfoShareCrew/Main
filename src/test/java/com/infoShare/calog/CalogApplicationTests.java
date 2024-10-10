package com.infoShare.calog;

import com.infoShare.calog.domain.Article.Article;
import com.infoShare.calog.domain.Article.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CalogApplicationTests {

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void createAndRetrieveArticle() {
		// 게시글 생성
		Article article = new Article();
		article.setTitle("테스트 제목");
		article.setContent("테스트 내용");

		// 게시글 저장
		Article savedArticle = articleRepository.save(article);

	}
}