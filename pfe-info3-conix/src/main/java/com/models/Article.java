package com.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Article")
public class Article {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "[id]")
	private int id;

	@Column(name = "[link]")
	private String link;
	
	@Column(name = "[title]")
	private String title;
	
	@Column(name = "[description]")
	private String description;
	
	@Column(name = "[fullText]")
	private String fullText;
	
	@Column(name = "[score]")
	private Integer score;
	
	@Column(name = "[ts]")
	private Date timestamp;
	
	@Column(name = "posWordsCsv")
	private String posWordsCsv;

	@Column(name = "negWordsCsv")
	private String negWordsCsv;
	
	@Transient	
	private List<String> posWords; 
	
	@Transient	
	private List<String> negWords;
	
	public Article() {}
	
	public Article(int id, String link, String title, String description, String fullText, Integer score, List<String> posWords, List<String> negWords) {
		super();
//		this.id = id;
		this.link = link;
		this.title = title;
		this.description = description;
		this.fullText = fullText;
		this.score = score;
		this.posWords = posWords;
		this.negWords = negWords; 
	}

	public Article(int id, String link, String title, String description, String fullText, Integer score) {
		super();
//		this.id = id;
		this.link = link;
		this.title = title;
		this.description = description;
		this.fullText = fullText;
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "Article [id=" + id + ", link=" + link + ", title=" + title + ", description=" + description
				+ ", fullText=" + fullText + ", score=" + score + ", timestamp=" + timestamp + ", posWords=" + posWords + ", negWords=" + negWords + "]";
	}
	//Getters & Setters
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<String> getPosWords() {
		return posWords;
	}

	public void setPosWords(List<String> posWords) {
		this.posWords = posWords;
	}

	public List<String> getNegWords() {
		return negWords;
	}

	public void setNegWords(List<String> negWords) {
		this.negWords = negWords;
	}
	
	public String getPosWordsCsv() {
		return posWordsCsv;
	}

	public void setPosWordsCsv(String posWordsCsv) {
		this.posWordsCsv = posWordsCsv;
	}

	public String getNegWordsCsv() {
		return negWordsCsv;
	}

	public void setNegWordsCsv(String negWordsCsv) {
		this.negWordsCsv = negWordsCsv;
	}
}