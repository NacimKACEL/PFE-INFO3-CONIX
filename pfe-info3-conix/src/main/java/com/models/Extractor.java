package com.models;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class Extractor {
	Map<String, String> mapPositifs = null;
	Map<String, String> mapNegatifs = null;
	ClassLoader classLoader = getClass().getClassLoader();
	public Extractor() {
		System.out.println("new Extractor");
		long startTime = System.currentTimeMillis();
		long endTime;
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

			endTime = System.currentTimeMillis();
			System.out.println("Durée de chargement constructeur Extractor " + (endTime-startTime));
			
		} catch (IOException e) {
			System.out.println("IOException dans Extractor.java au niveau du constructeur");
//			e.printStackTrace();
		}
		
	}

	public ArrayList<Article> extract(String firmName){
		long startTime;
		long endTime;
		
		try 
		{
			startTime = System.currentTimeMillis();
			Document doc;
			firmName = firmName.toLowerCase();
			
			
			InputStream is = classLoader.getResourceAsStream("entreprises.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			/* Chargement du fichier */
			
			String line = null;
			String[] arraySplit = new String[4];
			// A editer : quand l'entreprise n'est pas référencée dans la base
			String secteur = "";
			int i = 0;
			
			while ((line = in.readLine()) != null) 
			{
				i++;
				if(i==1)
					continue;
				arraySplit = line.split(";");
				if(arraySplit[0].equals(firmName))
				{
					System.out.println("entreprise TROUVE");
					secteur = arraySplit[1];
				}
			}			
			in.close();
			is.close();
			
			endTime = System.currentTimeMillis();
			System.out.println("Durée de chargement entreprises.txt " + (endTime-startTime));
			
			// Web Crawler
			// On utilise Google comme moteur de recherche
			startTime = System.currentTimeMillis();
			
			String url = "http://www.google.fr/search?q=" + firmName +" "+ secteur +"&espv=2&source=lnms&tbm=nws&sa=X";
			Connection.Response cr = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1;                   Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR  2.0.50727) 3gpp-gba UNTRUSTED/1.0")
                    .cookie("auth", "token")
                    .timeout(5000)
                    .execute();
			doc = Jsoup.parse(cr.body(), "ISO-8859-1");
			
			endTime = System.currentTimeMillis();
			System.out.println("Durée de récupération sur google " + (endTime-startTime));
			
			startTime = System.currentTimeMillis();
			
			//On récupère les titres h3 avec la description(ayant la classe st)
			Elements links = doc.select("h3 > a[href]");
			Elements descriptions = doc.select(".st");
			
			endTime = System.currentTimeMillis();
			System.out.println("Durée du select " + (endTime-startTime));
			
			startTime = System.currentTimeMillis();
			
			int nbArticles = links.size();
			ArrayList<Article> articles = new ArrayList<Article>();
			
			Pattern pattern = Pattern.compile("url=(.*?)&");
			
			Tagger tagger = new Tagger();
			
			endTime = System.currentTimeMillis();
			System.out.println("Durée du tagger et init variables " + (endTime-startTime));
			
			IntStream.range(0, nbArticles).forEach(
					index -> {
						
				        Matcher matcher = pattern.matcher(links.get(index).attr("href"));
				        
				        if (matcher.find())
				        {
				        	URL urlBis;
				        	int nbP = 0;
				        	int nbN = 0;
							try 
							{
//								System.out.println(links.get(index).attr("href")+" on se prépare vers "+ matcher.group(1) +"ok" + links.get(index).text());
								urlBis = new URL(matcher.group(1));
								long startTimeE = System.currentTimeMillis();
								InputSource isource = new InputSource();
								isource.setEncoding("UTF-8");
								isource.setByteStream(urlBis.openStream());
								String text = ArticleExtractor.INSTANCE.getText(isource).toLowerCase().replaceAll("[,?;.:/!<>&0-9()»«*|]", "");
								
								long endTimeE = System.currentTimeMillis();
								System.out.println("Durée d'une extraction " + (endTimeE-startTimeE));
								//								System.out.println("so far " + text);
								String[] words = text.split(" ");
								
								startTimeE = System.currentTimeMillis();
							    ArrayList<String> str = tagger.tag(words);
							    endTimeE = System.currentTimeMillis();
								System.out.println("Durée du tagging " + (endTimeE-startTimeE));
								
								startTimeE = System.currentTimeMillis();
								
							    for(String word:str)
								{
									if (mapPositifs.get(word) != null)
									{
										nbP ++;
//										System.out.println("positif " + word);
									} else if (mapNegatifs.get(word) != null){
										nbN ++;
//										System.out.println("negatif " + word);
									}
								}
							    endTimeE = System.currentTimeMillis();
								System.out.println("Durée du for pour un article " + (endTimeE-startTimeE));
							    
								System.out.println("Score sur ..." + index + "..."+ nbP + "..." + nbN );
					        	Article article = new Article(index, matcher.group(1), 
					        			links.get(index).text(), descriptions.get(index).text(),
					        			text, nbP - nbN);
					        	articles.add(article);
							}
							catch (MalformedURLException e1)
							{
								System.out.println("URL mal formée...");
//								e1.printStackTrace();
							}
							catch (BoilerpipeProcessingException e)
							{
//								e.printStackTrace();
								System.out.println("Problème accès URL");
							} catch (IOException e1) {
								System.out.println("Some IOException level Boilerpipe");
								//e1.printStackTrace();
							} 
				        	
				        } 
				        else 
				        {
				        	System.out.println("Problème de détection de liens ou il s'agit d'un lien protégé");
				        }
			        }
			    );
			tagger.destroy();
			return articles;
		}
		catch (IOException e) 
		{
			System.out.println("IOException dans Extractor au niveau de Jsoup");
			//e.printStackTrace();
		}
		return null;
	}
}
