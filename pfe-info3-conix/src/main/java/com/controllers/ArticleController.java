package com.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.models.Extractor;
import com.services.ArticleService;

@RestController
public class ArticleController {
 private ArticleService articleService;
 
 @Autowired(required=true)
 @Qualifier(value="articleService") 
 public void setArticleService(ArticleService as){
  this.articleService = as;
 }
   
 @RequestMapping(value="/updatedb/", method = RequestMethod.GET, produces = "application/json")
 public void updateDB()
 { 
	 Extractor ex = new Extractor();
	 ex.setArticleService(this.articleService);
	 Map<String, String> mapEntreprises = ex.getMapEntreprises();
	 mapEntreprises.forEach((firmName, sector) -> {System.out.println("Processing "+ firmName);ex.extract(firmName);});
	 System.out.println("Mise à jour effectuée");
 }
}