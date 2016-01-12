package org.occideas.rest.common;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.occideas.AgentInfo;
import org.occideas.HibernateUtility;

import com.google.gson.Gson;


public class AgentCommon {
	//COMMON FUNCTIONS
	protected String create(AgentInfo info) throws IOException
	{
		Session session = null;
		Transaction tr = null;
		String status = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	

	   		info.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		
	   		session.save(info);
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
			AgentInfo info =  (AgentInfo) session.get(AgentInfo.class, id);
			tr.commit();
			
			if(info != null) //SUCCESSFULLY GET DATA
				resp=  "{\"status\": 1,\"data\": "+new Gson().toJson(info)+"}";
			else // INVALIDE AGENT ID
				resp=  "{\"status\": -1}";				

			System.out.println(resp);
		}
		catch(Exception e){
			System.out.println("Info Not retrieved"+e.getMessage());
			
			//SERVER ERROR
			resp = "{\"status\":0}";
		}
		finally{
			session.close();								
		}
		
		return resp;
	}
	
	protected String update(AgentInfo info) throws IOException
	{	   	
		Session session = null;
		Transaction tr = null;
		String status = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	

	   		info.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		
	   		session.update(info);
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
			AgentInfo info =  (AgentInfo) session.get(AgentInfo.class, id);
			
			session.delete(info);
			tr.commit();
			
			//SUCCESSFULLY DELETED
			resp= "{\"status\":1}";
		}
		catch(Exception e){
			System.out.println("Info Not Deleted"+e.getMessage());
			//SERVER ERROR
			resp= "{\"status\":0}";
		}
		finally{
			session.close();								
		}
		
		return resp;
	}	
}
