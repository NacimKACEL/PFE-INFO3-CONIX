package com.services;

import java.util.ArrayList;
import com.models.Article;

public interface ExtractorService {
	ArrayList<Article> extract(String entrepriseName);
	// Pour le débuggage
	/*
	String extractJsonArticles(String entrepriseName);
	String extractJsonArticleById(String entrepriseName, int id);
	*/
	byte[] extractJsonArticlesUTF8(String entrepriseName);		

}