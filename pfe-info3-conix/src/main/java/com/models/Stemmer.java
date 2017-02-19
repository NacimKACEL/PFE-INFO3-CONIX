package com.models;

import java.util.ArrayList;

import org.tartarus.snowball.ext.frenchStemmer;


public class Stemmer {
	public ArrayList<String> tag(String[] words)
	{
		ArrayList<String> tags = new ArrayList<>();
		frenchStemmer stemmer = new frenchStemmer();
		for(String word:words){
			String bis = word.replaceAll("alors|aucuns|aussi|autre|avant|avec|avoir|aux|au|bon|car|cela|ces|ceux|ce|chaque|ci|comment|comme|dans|des|du|dedans|dehors|depuis|devrait|doit|donc|dos|début|elle|elles|en|encore|essai|est|et|eu|fait|faites|fois|font|hors|ici|ils|il|je|juste|la|les|leur|le|là|ma|maintenant|mais|mes|mine|moins|mon|mot|même|ni|nommés|notre|nous|ou|où|par|parce|pas|peut|peu|plupart|pourquoi|pour|quand|quelles|quellle|quels|quel|que|qui|sans|sa|ses|seulement|sien|si|sont|son|sous|soyez|sujet|sur|tandis|ta|tellement|tels|tes|ton|tous|tout|trop|très|tu|voient|vont|votre|vous|vu|ça|étaient|état|étions|été|être|une|un|la|le|du|ne|se|sur|cette|cet", "");
			if(!bis.equals("") && bis !=null)
			{
				stemmer.setCurrent(word);
				if(stemmer.stem())
				{
					tags.add(stemmer.getCurrent());
				}
			}
			else
			{
//				System.out.println("Bug ...");
			}
			
		}
		return tags;
	}
}
