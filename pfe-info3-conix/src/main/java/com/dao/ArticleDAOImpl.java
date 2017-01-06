package com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.models.Article;

public class ArticleDAOImpl implements ArticleDAO 
{
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	public void ajouter(Article a) 
	{
		Session session = this.sessionFactory.openSession();
		
		session.persist(a);
		
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<Article> lister() {
		List<Article> listeArticles = new ArrayList<Article>();
		Session session = this.sessionFactory.openSession();
		
		Query requeteLister = session.createQuery("from Article");
		listeArticles = requeteLister.list();
		
		for(Article a: listeArticles)
		{
			System.out.println(a.toString());
		}
		
		session.close();
		return listeArticles;
	}

}
