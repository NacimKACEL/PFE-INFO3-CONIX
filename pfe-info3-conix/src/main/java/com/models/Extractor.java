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
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.xml.sax.InputSource;

import com.services.ArticleService;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class Extractor 
{
	private static final Logger logger = LoggerFactory.getLogger(Extractor.class);
	
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
	public void setArticleService(ArticleService as)
	{
		logger.info("Entered setArticleService");	
		this.articleService = as;
	}
	
	public Extractor() 
	{
		nbReached = 0;
		logger.info("Nouvelle instance");
		classifier = new NaiveBayesClassifier();
		mapEntreprises = new HashMap<String, String>();
		InputStream is = classLoader.getResourceAsStream("entreprises.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		
		/* Chargement du fichier */
		
		String line = null;
		String[] arraySplit = new String[4];
		// TODO: quand l'entreprise n'est pas référencée dans la base, trouver une API
		int i = 0;
		try 
		{
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
		}
		catch (IOException e) 
		{
			logger.error("FATAL Problème dans la lecture du fichier Entreprises");
			System.exit(-1);
		}			
	}

	public ArrayList<Article> extract(String firmName)
	{	
		Properties prop = new Properties();
		String filename = "config.properties";
		InputStream input = Extractor.class.getClassLoader().getResourceAsStream(filename);
		try
		{
			prop.load(input);
		}
		catch (IOException e2) 
		{
			logger.error("FATAL Problème dans la lecture du fichier properties");
			System.exit(-1);
		}
		
		this.nbReached = 0;
		try 
		{
			Document doc;
			firmName = firmName.toLowerCase();
			String secteur = "";
			
			if(mapEntreprises.containsKey(firmName))
			{
				secteur = mapEntreprises.get(firmName);
				logger.info("Entreprise " + firmName + " trouvée");
			}
			else
			{
				logger.info("Entreprise " + firmName + " non trouvée");
			}
			
			 /* Web Crawler */
			//	Le moteur de recherche est celui configuré dans config.properties
			String url = prop.getProperty("searchengine") 
			+ firmName + "%20"
			+ secteur + 
			prop.getProperty("options");
			
			Connection.Response cr = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1;"
                    		+ "Trident/5.0; InfoPath.2; SLCC1;"
                    		+ " .NET CLR 3.0.4506.2152;"
                    		+ " .NET CLR 3.5.30729; .NET CLR  2.0.50727)"
                    		+ " 3gpp-gba UNTRUSTED/1.0")
                    .cookie("auth", "token")
                    .timeout(5000)
                    .execute();
			doc = Jsoup.parse(cr.body(), "ISO-8859-1");
				
//			On récupère les titres h3 avec la description(ayant la classe configurée)
			Elements links = doc.select(prop.getProperty("links"));
			Elements descriptions = doc.select(prop.getProperty("class"));
			
			int nbArticles = links.size();
			ArrayList<Article> articles = new ArrayList<Article>();
			
			Pattern pattern = Pattern.compile(prop.getProperty("urlPattern"));
			
			/* Utilisation de treeTagger  ou d'un Stemmer*/
//			Tagger tagger = new Tagger();
			Stemmer stemmer = new Stemmer();
			
			logger.debug("crawler link " + url);
			logger.debug("Crawler success with : " + nbArticles + " articles");
			int index;
			for(index = 0; index <nbArticles ; index++)
			{
				
				//logger.info("URL...avant traitement " + links.get(index).attr("href"));
				Matcher matcher = pattern.matcher(links.get(index).attr("href"));
		        Article article = null;
		        if(this.nbReached == 10)
	        	{
	        		 this.nbReached = 0;
	        		 logger.info("Limite atteinte");
	        		 break;
	        	}
		        
		        if (matcher.find())
		        {
		        	String localLink = matcher.group(1);
		        	logger.info("Lien matché " + localLink);
		        	
		        	// On tente de récupérer l'article depuis la BDD 
		        	if((article = this.articleService.getArticleByLink(localLink)) != null)
		        	{
		        		logger.info("Article récupéré de la BDD et ajout de ..." + article);
		        		articles.add(article);
		        		this.nbReached++;
		        	}
		        	
		        	else 
		        	{
		        		URL urlBis;
						try 
						{
							// Tentative de connexion avec l'url
							urlBis = new URL(localLink);
							InputSource isource = new InputSource();
							isource.setEncoding("UTF-8");
							isource.setByteStream(urlBis.openStream());
							
							// On retirer la ponctuation
							String text = ArticleExtractor.INSTANCE.getText(isource)
									.toLowerCase()
									.replaceAll("[,?;.:/!<>&0-9()»«*%|\"{}]", "");
							
							
							logger.debug("Texte initial sans ponctuations etc.." + text);
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
							
				        	article = new Article(index, localLink, 
				        			links.get(index).text(), descriptions.get(index).text(),
				        			String.join(" ", str), (sc == "Positif")?1:-1, 
				        					new ArrayList<String>(lS.getPosWords(words)), 
				        					new ArrayList<String>(lS.getNegWords(words)));
				        	logger.info("ajout de l'article " + article);
				        	articles.add(article);
				        	// Enregistrement de l'article dans la BDD
				        	try
				        	{
				        		this.articleService.persistArticle(article);
				        	}
				        	catch(Exception e){
				        		logger.error("Problème enregistrement dans la BDD, mauvais caractères...");
				        	}
				        	
				        	// On incrémente le nombre d'articles trouvés
							this.nbReached++;
						}
						// Gestion des exceptions - Ce devrait plus être des warning que Erreur
						// Car le programme peut tout de même continuer de tourner
						catch (MalformedURLException e1)
						{
							logger.error("URL mal formée...");
						}
						catch (BoilerpipeProcessingException e)
						{
							//System.out.println(e.getMessage());
							logger.error("Problème accès URL avec BoilerPipe");
						}
						catch (IOException e1) {
							logger.error("IOException au niveau de Boilerpipe");
							logger.error(e1.getMessage());
						}
					}
		        }
		        else
		        {
		        	logger.error("Problème de détection de liens ou il s'agit d'un lien protégé");
		        }
//				tagger.destroy();
	        }
			return articles;
		}
		catch (IOException e) 
		{
			logger.error("FATAL IOException dans Extractor au niveau de Jsoup, "
					+ "probablement erreur de connexion internet ou bloqué par google");
			logger.error(e.getMessage());
			System.exit(-1);
		}
		return null;
	}
}
