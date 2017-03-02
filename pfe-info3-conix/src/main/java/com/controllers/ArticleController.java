package com.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.models.Article;
import com.models.Extractor;
import com.services.ArticleService;

@RestController
public class ArticleController 
{
	private ArticleService articleService;
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired(required = true)
	@Qualifier(value = "articleService")
	public void setArticleService(ArticleService as) {
		this.articleService = as;
	}

	@RequestMapping(value = "/updatedb/", method = RequestMethod.GET, produces = "application/json")
	public void updateDB() 
	{
		Extractor ex = new Extractor();
		ex.setArticleService(this.articleService);
		Map<String, String> mapEntreprises = ex.getMapEntreprises();
		mapEntreprises.forEach((firmName, sector) -> 
		{
			logger.info("Processing " + firmName);
			ex.extract(firmName);
		});
		logger.info("Mise à jour effectuée");
	}
	
	/* NomEntreprise, NbArticles positifs - NbArticles negatifs */
	@RequestMapping(value = "/dashboard/", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Double> dashBoard() 
	{
		Extractor ex = new Extractor();
		ex.setArticleService(this.articleService);
		Map<String, String> mapEntreprises = ex.getMapEntreprises();
		/* NomEntreprise, NbArticles positifs - NbArticles negatifs */
		Map<String, Double> mapResults = new HashMap<String, Double>();
		
		mapEntreprises.forEach((firmName, sector) -> 
		{
			ArrayList<Article> articles;
			logger.info("Processing " + firmName);
			articles = ex.extract(firmName);
			double tendance = 0;
			for (Article article : articles) 
			{
				tendance += article.getScore();
			}
			mapResults.put(firmName, tendance);
		});
		logger.info("Dashboard prêt");
		return mapResults;
	}
	// TODO: Rajouter une méthode clean pour remettre à zéro la BDD
}