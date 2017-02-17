package com.dao;

import java.util.List;
import com.models.Article;

public interface ArticleDAO 
{
	public List<Article> listArticles();
	public void persistArticle(Article article);
	public void updateArticle(Article article);
	public Article getArticleByLink(String link);
}
