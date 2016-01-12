package org.occideas.rest;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.occideas.AgentInfo;
import org.occideas.HibernateUtility;

import com.google.gson.Gson;

@Path("/agentinfo")
public class AgentInfoService {
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createAgentInfo(final AgentInfo info) throws IOException{
		Session session = null;
		Transaction tr = null;
		String status = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	

	   		info.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		
	   		session.save(info);
			tr.commit();
			
			status= "{\"status\":1}";
			
		} catch (Exception e) {
			System.out.println("Transaction Fail "+e.getMessage());
			tr.rollback();
			status= "{\"status\":0}";
		}
		finally{
			session.close();					
		}
		return status;
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentInfo(@QueryParam("id") long id)
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

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateAgentInfo(final AgentInfo info) throws IOException{
   	
		Session session = null;
		Transaction tr = null;
		String status = "";
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		tr = session.beginTransaction();	

	   		info.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		
	   		session.update(info);
			tr.commit();
			
			status= "{\"status\":1}";
			
		} catch (Exception e) {
			System.out.println("Transaction Fail"+e.getMessage());
			tr.rollback();
			status= "{\"status\":0}";
		}
		finally{
			session.close();					
		}
		return status;
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAgentInfo(@QueryParam("id") long id)
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
			
			resp= "{\"status\":1}";
		}
		catch(Exception e){
			System.out.println("Info Not Deleted"+e.getMessage());
			resp= "{\"status\":0}";
		}
		finally{
			session.close();								
		}
		
		return resp;
	}
}
