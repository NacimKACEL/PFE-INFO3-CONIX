package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.services.ArticleService;

@Controller
public class ArticleController {
	
	private ArticleService articleService;
	
	@Autowired(required=true)
	@Qualifier(value="articleService")
	public void setArticleService(ArticleService as){
		this.articleService = as;
	}
	
	@RequestMapping(value="/articles", method=RequestMethod.GET)
	public String listerArticles(Model model)
	{	
		model.addAttribute("listeArticles", this.articleService.lister());
		return "listeArticles";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}
}