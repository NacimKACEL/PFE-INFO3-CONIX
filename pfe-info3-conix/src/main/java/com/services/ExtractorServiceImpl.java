package com.services;
 
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.models.Article;
import com.models.Extractor;

@Service
public class ExtractorServiceImpl implements ExtractorService 
{
	private Extractor extractor;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public void setExtractor(Extractor extractor) {
		this.extractor = extractor;
	}

	@Override
	public ArrayList<Article> extract(String entrepriseName)
	{
		System.out.println(" nom entreprise : "+entrepriseName);
		return this. extractor.extract(entrepriseName);
	}
	
	// Pour le débuggage
	/*
	@Override
	public String extractJsonArticles(String entrepriseName) {		
		ArrayList<Article> articles = this.extract(entrepriseName);
		return this.gson.toJson(articles);
	}

	@Override
	public String extractJsonArticleById(String entrepriseName, int id) {
		ArrayList<Article> articles = this.extract(entrepriseName);
		return this.gson.toJson(articles.get(id)); 
	}
	*/
	
	@Override
	public byte[] extractJsonArticlesUTF8(String entrepriseName) {		
		ArrayList<Article> articles = this.extract(entrepriseName);
		String notUTF8String;
		byte[] UTF8String;
		
		try 
		{
			notUTF8String = this.gson.toJson(articles);
			UTF8String = notUTF8String.getBytes("UTF8");
			//System.out.println(UTF8String[0]);
			return UTF8String;
		}
		catch (UnsupportedEncodingException e) 
		{
			System.out.println("--Enconding in UTF8 failed");
		}
		System.out.println("--Enconding in UTF8 failed");
		return null;

	}
}
