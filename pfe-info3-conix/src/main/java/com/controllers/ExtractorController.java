package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.services.ExtractorService; 

@RestController
public class ExtractorController{
	@Autowired
	ExtractorService extractorService;
	
	@Autowired(required=true)
	@Qualifier(value="extractorService")
	public void setExtractorService(ExtractorService es){
		this.extractorService = es;
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/extract/{firmName}/{id}", method = RequestMethod.GET, produces = "application/json")
	public String getExtractedArticleById(@PathVariable String firmName, @PathVariable int id){
		return this.extractorService.extractJsonArticleById(firmName, id);
	}
	
	@RequestMapping(value="/extract/{firmName}", method = RequestMethod.GET, produces = "application/json")
	public String getExtractedArticles(@PathVariable String firmName){
		return this.extractorService.extractJsonArticles(firmName);
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/extract/utf8/{firmName}", method = RequestMethod.GET, produces = "application/json")
	public byte[] getExtractedArticlesUTF8(@PathVariable String firmName){
		return this.extractorService.extractJsonArticlesUTF8(firmName);
	}
}



