package com.models;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.xml.sax.InputSource;

import com.services.ArticleService;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class Extractor {
	TextScorer classifier = null;
	int nbReached = 0;
	ClassLoader classLoader = getClass().getClassLoader();
	ArticleService articleService;
	/* NomEntreprise, Secteur */
	Map<String, String> mapEntreprises = null;
	public Map<String, String> getMapEntreprises() {
		return mapEntreprises;
	}
	@Autowired(required=true)
	@Qualifier(value="articleService") 
	public void setArticleService(ArticleService as){
		System.out.println("Entered in set ArticleService de Extractor");	
		this.articleService = as;
	}
	public Extractor() {
		System.out.println("Nouvelle instance d'Extractor");
		classifier = new NaiveBayesClassifier();
		mapEntreprises = new HashMap<String, String>();
		InputStream is = classLoader.getResourceAsStream("entreprises.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		/* Chargement du fichier */
		
		String line = null;
		String[] arraySplit = new String[4];
		// A editer : quand l'entreprise n'est pas référencée dans la base
		int i = 0;
		try {
			while ((line = in.readLine()) != null) 
			{
				i++;
				if(i==1)
					continue;
				arraySplit = line.split(";");
				mapEntreprises.put(arraySplit[0], arraySplit[1]);
			}
			in.close();
			is.close();
		} catch (IOException e) 
		{
			System.out.println("Problème dans la lecture du fichier Entreprises");
		}			
	}

	public ArrayList<Article> extract(String firmName){		
		this.nbReached = 0;
		try 
		{
			Document doc;
			firmName = firmName.toLowerCase();
			String secteur = "";
			
			if(mapEntreprises.containsKey(firmName))
			{
				secteur = mapEntreprises.get(firmName);
			}
			
			 /* Web Crawler */
//			On utilise Google comme moteur de recherche
			String url = "http://www.google.fr/search?q=" + firmName +" "
			+ secteur +"&espv=2&source=lnms&num=20&tbm=nws&sa=X&tbs=sbd:1";
			Connection.Response cr = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1;"
                    		+ "Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR  2.0.50727) 3gpp-gba UNTRUSTED/1.0")
                    .cookie("auth", "token")
                    .timeout(5000)
                    .execute();
			doc = Jsoup.parse(cr.body(), "ISO-8859-1");
				
//			On récupère les titres h3 avec la description(ayant la classe st)
			Elements links = doc.select("h3 > a[href]");
			Elements descriptions = doc.select(".st");
			
			int nbArticles = links.size();
			ArrayList<Article> articles = new ArrayList<Article>();
			
			Pattern pattern = Pattern.compile("url=(.*?)&");
			/* Utilisation de treeTagger  ou d'un Stemmer*/
//			Tagger tagger = new Tagger();
			Stemmer stemmer = new Stemmer();
			
			int index;
			for(index = 0; index <nbArticles ; index++)
			{
				Matcher matcher = pattern.matcher(links.get(index).attr("href"));
		        Article article = null;
		        if (matcher.find())
		        {
		        	if(this.nbReached == 10)
		        	{
		        		 this.nbReached = 0;
		        		 System.out.println("Limite atteinte");
		        		 break;
		        	}
		        	
		        	if((article = this.articleService.getArticleByLink(matcher.group(1))) != null)
		        	{
		        		System.out.println("Article récupéré...");
		        		articles.add(article);
		        		this.nbReached++;
		        	}
		        	else 
		        	{
		        		URL urlBis;
						try 
						{
							urlBis = new URL(matcher.group(1));
							InputSource isource = new InputSource();
							isource.setEncoding("UTF-8");
							isource.setByteStream(urlBis.openStream());
							String text = ArticleExtractor.INSTANCE.getText(isource).toLowerCase().replaceAll("[,?;.:/!<>&0-9()»«*%|\"{}]", "");
							this.nbReached++;
//							System.out.println("Texte initial sans ponctuations etc.." + text);
							String[] words = text.split(" ");
							
						    /* Utilisation de treeTagger */
							//ArrayList<String> str = tagger.tag(words);
							/* Utilisation de Stemmer */
							ArrayList<String> str = stemmer.tag(words);
							
							/* Méthode apprentissage supervisée ou non 
							 * (si NaiveBayes alors supervisée) 
							 * (si lexique alors non supervisée)
							 * */
							String sc = classifier.score(str);
							LexiqueScorer lS = new LexiqueScorer();
//							for(String s:lS.getPosWords(words))
//							{
//								System.out.println("Pos List" + s);
//							}
//							for(String s:lS.getNegWords(words))
//							{
//								System.out.println("Neg List" + s);
//							}
//							System.out.println("Score sur ..." + index + "..."+ sc);
				        	article = new Article(index, matcher.group(1), 
				        			links.get(index).text(), descriptions.get(index).text(),
				        			String.join(" ", str), (sc == "Positif")?1:-1, 
				        					new ArrayList<String>(lS.getPosWords(words)), 
				        					new ArrayList<String>(lS.getNegWords(words)));
				        	
				        	articles.add(article);
				        	this.articleService.persistArticle(article);	
						}
						catch (MalformedURLException e1)
						{
							System.out.println("URL mal formée...");
						}
						catch (BoilerpipeProcessingException e)
						{
							//e.printStackTrace();
							System.out.println("Problème accès URL avec BoilerPipe");
						}
						catch (IOException e1) {
							System.out.println("IOException au niveau de Boilerpipe");
							System.out.println(e1.getMessage());
//							e1.printStackTrace();
						}
					}
		        }
		        else
		        {
		        	System.out.println("Problème de détection de liens ou il s'agit d'un lien protégé");
		        }
//				tagger.destroy();
	        }
			return articles;
		}
		catch (IOException e) 
		{
			System.out.println("IOException dans Extractor au niveau de Jsoup, "
					+ "probablement erreur de connexion internet ou bloqué par google");
			System.out.println(e.getMessage());
		}
		return null;
	}
}
