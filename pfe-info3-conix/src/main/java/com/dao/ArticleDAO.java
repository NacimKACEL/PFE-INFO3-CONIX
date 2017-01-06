package com.dao;

import java.util.List;
import com.models.Article;

public interface ArticleDAO 
{
	public void ajouter(Article p);
	public List<Article> lister();
}
