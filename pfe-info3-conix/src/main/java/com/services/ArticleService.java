package com.services;

import java.util.List;

import com.models.Article;

public interface ArticleService {
	public void ajouter(Article a);
	public List<Article> lister();
}