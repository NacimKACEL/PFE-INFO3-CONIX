package com.services;

import java.util.List;

import com.models.Article;

public interface ArticleService {
	public List<Article> listArticles();
	public void persistArticle(Article article);
	public Article getArticleByLink(String link);
}