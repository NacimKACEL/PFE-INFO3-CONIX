package com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.models.Article;

public class ArticleDAOImpl implements ArticleDAO 
{
	private static final String HQL_SELECT_ALL = "FROM Article";
	private static final String HQL_SELECT_BY_LINK = "FROM Article WHERE link = :link";
	private static final String HQL_UPDATE_BY_TITLE = 		
			"UPDATE"+
					"Article"+
					"SET"+ 
					"timestamp = :timestamp,"+
					"score = :score,"+
					"posWordsCsv = :posWordsCsv,"+
					"negWordsCsv = :negWordsCsv,"+
					"WHERE title = :title";

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory)
	{ 
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<Article> listArticles() {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = null;
		List<Article> articleList = new ArrayList<Article>();
		Query requeteLister = session.createQuery(HQL_SELECT_ALL);

		try{
			transaction = session.beginTransaction();
			articleList = requeteLister.list();
		}catch(HibernateException he){
			if (transaction != null) transaction.rollback();
			he.printStackTrace();
		}finally{
			session.close();
		}

		return articleList;
	}
	
	public void persistArticle(Article article)
	{
		Session session = this.sessionFactory.openSession();
		session.merge(article);
		session.flush();
		session.close();		
	}

	public void updateArticle(Article article){
		Session session = this.sessionFactory.openSession();
		Transaction transaction = null;
		System.out.println("Entered update article");
		Query updateRequest = session.createQuery(HQL_UPDATE_BY_TITLE);
		updateRequest.setParameter("ts", article.getTimestamp());
		updateRequest.setParameter("score", article.getScore());

		try
		{
			transaction = session.beginTransaction();
			int success = updateRequest.executeUpdate();

			if(success == -1) throw new HibernateException("Hibernate: Update of "+article.getTitle()+" Failed");
		}
		catch(HibernateException he)
		{
			if(he != null) transaction.rollback();
			he.printStackTrace();			
		}
		finally
		{
			session.close();
		}
	}	

	public Article getArticleByLink(String link)
	{
		Session session = this.sessionFactory.openSession();
		Transaction transaction = null;
		Article article = null;
		Query requestSelectByLink = session.createQuery(HQL_SELECT_BY_LINK);
		requestSelectByLink.setParameter("link", link);

		try
		{
			transaction = session.beginTransaction();
			article = (Article) requestSelectByLink.uniqueResult();
			if(article == null)
				return null;
		}
		catch(HibernateException he)
		{
			if(he != null) transaction.rollback();
			he.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return article;
	}
}
