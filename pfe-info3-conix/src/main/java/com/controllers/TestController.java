package com.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.models.Article;

import javax.json.*;
/*
@RestController
public class TestController {
	private  Article a1 = new Article(0, "Google's bankrupt", "Very bad");
	private  Article a2 = new Article(1, "BNP & AXA's merge", "Dangerous");
	private  Article a3 = new Article(2, "Carrefour's new stores", "Good");
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public JsonArray testArray() {		
		return Json.createArrayBuilder().
				add(createJSONArticle(a1)).
				add(createJSONArticle(a2)).
				add(createJSONArticle(a3)).
				build();
	}
	
	public JsonObject createJSONArticle(Article a) {	
		return Json.createObjectBuilder().
			add("id", a.getId()).
			add("titre", a.getTitre()).
			add("tendence", a.getTendence()).
			build();
	}
	
	@RequestMapping(value = "/test2", method = RequestMethod.GET, produces = "application/json")
	public Article getArticleAsJson(){
		return a1;
	}
}*/