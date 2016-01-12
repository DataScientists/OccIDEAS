package org.occideas.rest.common;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.occideas.Node;
import org.occideas.HibernateUtility;

import com.google.gson.Gson;


public class NodeCommon {
	//COMMON FUNCTIONS
	protected String create(Node node) throws IOException
	{
		Session session = null;
		Transaction tr = null;
		String status = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	

	   		node.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		
	   		session.save(node);
			tr.commit();
			
			//SUCCESSFULLY CREATED			
			status= "{\"status\":1}";
			
		} catch (Exception e) {
			System.out.println("Transaction Fail "+e.getMessage());
			tr.rollback();
			//SERVER ERROR			
			status= "{\"status\":0}";
		}
		finally{
			session.close();					
		}
		return status;		
	}
	
	protected String get(long id)
	{
		Session session = null;
		Transaction tr = null;
		String resp = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	
			Node node =  (Node) session.get(Node.class, id);
			tr.commit();
			
			if(node != null) //SUCCESSFULLY GET DATA
				resp=  "{\"status\": 1,\"data\": "+new Gson().toJson(node)+"}";
			else // INVALIDE AGENT ID
				resp=  "{\"status\": -1}";				

			System.out.println(resp);
		}
		catch(Exception e){
			System.out.println("node Not retrieved"+e.getMessage());
			
			//SERVER ERROR
			resp = "{\"status\":0}";
		}
		finally{
			session.close();								
		}
		
		return resp;
	}
	
	protected String update(Node node) throws IOException
	{	   	
		Session session = null;
		Transaction tr = null;
		String status = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	

	   		node.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		
	   		session.update(node);
			tr.commit();
			
			//SUCCESSFULLY UPDATED
			status= "{\"status\":1}";
			
		} catch (Exception e) {
			System.out.println("Transaction Fail"+e.getMessage());
			tr.rollback();
			//SERVER ERROR		
			status= "{\"status\":0}";
		}
		finally{
			session.close();					
		}
		return status;
	}	

	protected String delete(long id)
	{
		Session session = null;
		Transaction tr = null;
		String resp = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	
			Node node =  (Node) session.get(Node.class, id);
			
			session.delete(node);
			tr.commit();
			
			//SUCCESSFULLY DELETED
			resp= "{\"status\":1}";
		}
		catch(Exception e){
			System.out.println("node Not Deleted"+e.getMessage());
			//SERVER ERROR
			resp= "{\"status\":0}";
		}
		finally{
			session.close();								
		}
		
		return resp;
	}	
}
