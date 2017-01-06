package com.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.models.Article;
import com.services.ArticleService;
import com.dao.ArticleDAO;

@Service
public class ArticleServiceImpl implements ArticleService {
	private ArticleDAO articleDAO;

	public void setArticleDAO(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}
	
	@Transactional
	public void ajouter(Article a) {
		this.articleDAO.ajouter(a);
	}

	@Transactional
	public List<Article> lister() {
		return this.articleDAO.lister();
	}
}
