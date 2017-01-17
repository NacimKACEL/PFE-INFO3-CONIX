package com.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.datumbox.opensource.classifiers.NaiveBayes;
import com.datumbox.opensource.dataobjects.NaiveBayesKnowledgeBase;


public class NaiveBayesClassifier implements TextScorer{
	ClassLoader classLoader = getClass().getClassLoader();
	
	private NaiveBayes model = null;
	public NaiveBayesClassifier()
	{
		Map<String, URL> trainingFiles = new HashMap<>();
		
        trainingFiles.put("Positif", classLoader.getResource("posCorpus.csv"));
        trainingFiles.put("Negatif", classLoader.getResource("negCorpus.csv"));
        
        //Chargement dans la mémoire
        Map<String, String[]> trainingExamples = new HashMap<>();
        for(Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
            try {
				trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
			} catch (IOException e) {
				System.out.println("IOException dans NaiveBayesClassifier");
//				e.printStackTrace();
			}
        }
        //Entrainement du modèle
        this.model = new NaiveBayes();
        this.model.setChisquareCriticalValue(0.01); //0.01 pvalue, 6.63
        this.model.train(trainingExamples);
        
        //récupération de la base de connaissance entrainée
        NaiveBayesKnowledgeBase knowledgeBase = model.getKnowledgeBase();
        
        this.model = null;
        trainingExamples = null;
        //Updating classifier
        this.model = new NaiveBayes(knowledgeBase);
	}
	
	public static String[] readLines(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
	public String score(ArrayList<String> phrase) 
	{
		
        String classe = model.predict(String.join(" ",phrase));
        Map<String, Double> mpPos = new HashMap<String, Double>();
        Map<String, Double> mpNeg = new HashMap<String, Double>();
        double maxi = -100;
        String sMaxi ="";
        for(String w: phrase){
        	if(model.getKnowledgeBase().logLikelihoods.containsKey(w))
        	{	
        		//System.out.println("hey " + model.getKnowledgeBase().logLikelihoods.get(w));
        		if(
        				(model.getKnowledgeBase().logLikelihoods.get(w).get("Positif") - 
        						model.getKnowledgeBase().logLikelihoods.get(w).get("Negatif") > maxi)
        				|| (model.getKnowledgeBase().logLikelihoods.get(w).get("Negatif") - 
        						model.getKnowledgeBase().logLikelihoods.get(w).get("Positif") > maxi)
        				)
        		{
        			maxi = model.getKnowledgeBase().logLikelihoods.get(w).get("Positif");
        			sMaxi = w;
        		}
        		mpPos.put(w, model.getKnowledgeBase().logLikelihoods.get(w).get("Positif"));
        		mpNeg.put(w, model.getKnowledgeBase().logLikelihoods.get(w).get("Negatif"));
        	}
        }
        Stream<Map.Entry<String,Double>> sorted = mpPos.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
        sorted = sorted.limit(3);
        sorted.forEach(s -> {System.out.println("hey Po " + s.getKey() + " " + s.getValue());});
//        System.out.println("En fait..." + sMaxi + " avec " +maxi);
 
        
//        System.out.format("La phrase \"%s\" a été classée comme \n\"%s\".%n", phrase, classe);
        return classe;        

	}

}
