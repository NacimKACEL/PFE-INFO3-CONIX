package com.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LexiqueScorer implements TextScorer {
	Map<String, String> mapPositifs = null;
	Map<String, String> mapNegatifs = null;
	ClassLoader classLoader = getClass().getClassLoader();
	private static final Logger logger = LoggerFactory.getLogger(LexiqueScorer.class);
	public LexiqueScorer()
	{
		mapPositifs = new HashMap<String, String>();
		mapNegatifs = new HashMap<String, String>();
		
		/* Chargement des fichiers, du dictionnaire */
		
		String line = null;
		InputStream is = classLoader.getResourceAsStream("positifs.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));			 
		try {
			while ((line = in.readLine()) != null) 
			{		
					mapPositifs.put(line, line);
			}
			in.close();
			is.close();
			
			is = classLoader.getResourceAsStream("negatifs.txt");
			in = new BufferedReader(new InputStreamReader(is));
			while ((line = in.readLine()) != null) 
			{		
					mapNegatifs.put(line, line);
			}			
			in.close();
			is.close();
		} catch (IOException e) 
		{
			logger.error("IOException au niveau du constructeur : " + e.getMessage());
		}
	}
	@Override
	public String score(ArrayList<String> phrase) {
		int nbP = 0;
		int nbN = 0;
		for(String word:phrase)
		{
			if (mapPositifs.get(word) != null)
			{
				nbP ++;
				logger.debug("positif " + word);
			} else if (mapNegatifs.get(word) != null){
				nbN ++;
				logger.debug("negatif " + word);
			}
		}
		return (nbP-nbN >=0)?"Positif":"Negatif";
	}
	
	public Set<String> getPosWords(String[] phrase)
	{
		Set<String> posWords = new HashSet<String>();
		for(String word:phrase)
		{
			if (mapPositifs.get(word) != null)
			{
				posWords.add(word);
			} 
		}
		return posWords;
	}
	public Set<String> getNegWords(String[] phrase)
	{
		Set<String> negWords = new HashSet<String>();
		for(String word:phrase)
		{
			if (mapNegatifs.get(word) != null)
			{
				negWords.add(word);
			} 
		}
		return negWords;
	}
	
}
