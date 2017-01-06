package com.services;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.models.Article;

public interface ExtractorService {
	ArrayList<Article> extract(String entrepriseName);
	String extractJsonArticles(String entrepriseName);
	String extractJsonArticleById(String entrepriseName, int id);
	byte[] extractJsonArticlesUTF8(String entrepriseName);		

}