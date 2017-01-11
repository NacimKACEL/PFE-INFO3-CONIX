package com.models;

import java.util.ArrayList;

public class Article {
	int id;

	String link;
	String title;
	String description;
	String fullText;
	Integer score;
	ArrayList<String> posWords;
	ArrayList<String> negWords;
	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public Article(int id, String link, String title, String description, String fullText, Integer score) {
		super();
		this.id = id;
		this.link = link;
		this.title = title;
		this.description = description;
		this.fullText = fullText;
		this.score = score;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public ArrayList<String> getPosWords() {
		return posWords;
	}

	public void setPosWords(ArrayList<String> posWords) {
		this.posWords = posWords;
	}

	public ArrayList<String> getNegWords() {
		return negWords;
	}

	public void setNegWords(ArrayList<String> negWords) {
		this.negWords = negWords;
	}

	public Article(int id, String link, String title, String description, String fullText, Integer score,
			ArrayList<String> posWords, ArrayList<String> negWords) {
		super();
		this.id = id;
		this.link = link;
		this.title = title;
		this.description = description;
		this.fullText = fullText;
		this.score = score;
		this.posWords = posWords;
		this.negWords = negWords;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Article() {

	}

	@Override
	public String toString() {
		return "Article [link=" + link + ", title=" + title + ", description=" + description + "]";
	}
}