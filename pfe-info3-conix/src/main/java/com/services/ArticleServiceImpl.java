package com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.models.Article;
import com.services.ArticleService;
import com.dao.ArticleDAO;

@Service
public class ArticleServiceImpl implements ArticleService {
	private ArticleDAO articleDAO;

	@Autowired
	public void setArticleDAO(ArticleDAO articleDAO){
		this.articleDAO = articleDAO;
	}
	
	@Transactional
	public List<Article> listArticles() {
		return this.articleDAO.listArticles();
	}

	@Override
	public Article getArticleByTitle(String title) {
		return this.articleDAO.getArticleByTitle(title);
	}

	@Override
	public void persistArticle(Article article) {
		this.articleDAO.persistArticle(article);
	}

	@Override
	public void updateArticle(Article article) {
		this.articleDAO.updateArticle(article);
	}
	 @Override
	 public Article getArticleByLink(String link) {
	  return this.articleDAO.getArticleByLink(link);
	 }
}
