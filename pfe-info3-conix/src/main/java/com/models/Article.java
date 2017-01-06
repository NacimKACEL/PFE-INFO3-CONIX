package com.models;

public class Article {
	int id;

	String link;
	String title;
	String description;
	String fullText;
	Integer score;
	
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Article [link=" + link + ", title=" + title + ", description=" + description + "]";
	}
}