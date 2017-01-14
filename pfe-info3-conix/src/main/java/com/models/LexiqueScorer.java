package com.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LexiqueScorer implements TextScorer {
	Map<String, String> mapPositifs = null;
	Map<String, String> mapNegatifs = null;
	ClassLoader classLoader = getClass().getClassLoader();
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
			System.out.println("IOException dans LexiqueScorer.java au niveau du constructeur");
//			e.printStackTrace();
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
//				System.out.println("positif " + word);
			} else if (mapNegatifs.get(word) != null){
				nbN ++;
//				System.out.println("negatif " + word);
			}
		}
		return (nbP-nbN >=0)?"Positif":"Negatif";
	}

}
