package com.models;

import java.io.IOException;
import java.util.ArrayList;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import static java.util.Arrays.asList;

public class Tagger {

	private ArrayList<String> lemmas = null;


	TreeTaggerWrapper<String> tt = null;
	final String OS = System.getProperty("os.name").toLowerCase();

	public Tagger() {

		tt = new TreeTaggerWrapper<String>();
		lemmas = new ArrayList<String>();
//		On configure l'environnement de travail
		if (OS.indexOf("win") >= 0)
		{
			System.setProperty("treetagger.home", "C:/Program Files/Treetagger");
		}
		else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 )
		{
			System.setProperty("treetagger.home", "/home/brahim/INFO3/treetagger");
//			System.setProperty("treetagger.home", "/usr/lib/treetagger");
		}
		else
		{
			System.setProperty("treetagger.home", "/Applications/treetagger");
		}
		
		try 
	    {
//			On donne le modèle et définit la méthode de traitement
    		tt.setModel("french-utf8.par");
			tt.setHandler(new TokenHandler<String>() 
			{
				public void token(String token, String pos, String lemma) 
				{
//						System.out.println(token+"\t"+pos+"\t"+lemma);
					lemmas.add(lemma);
				}
			});

		}
		catch (IOException e) 
		{
			System.out.println("IOException dans tagger.java, constructeur");
//			e.printStackTrace();
		}

	}
	
	public ArrayList<String> getLemmas() {
		return lemmas;
	}

	public ArrayList<String> tag(String[] words)
	{
//		On traite la liste des mots				
		try 
		{
			lemmas.clear();
			tt.process(asList(words));
		}
		catch (IOException | TreeTaggerException e) 
		{
			System.out.println("IOException ou TreeTaggerException dans la méthode tag de Tagger.java");
//			e.printStackTrace();
		}
		return lemmas;
	}
	
	public void destroy()
	{
		tt.destroy();
	}
	
}
