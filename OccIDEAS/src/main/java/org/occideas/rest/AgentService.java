package org.occideas.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.occideas.Agent;
import org.occideas.Agent;
import org.occideas.HibernateUtility;
import org.occideas.rest.common.AgentCommon;

import com.google.gson.Gson;

@Path("/agent")
public class AgentService extends AgentCommon{

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createAgent(final Agent agent) throws IOException
	{
		return super.create(agent);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgent(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateAgent(final Agent agent) throws IOException
	{
		return super.update(agent);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAgent(@QueryParam("id") long id)
	{
		return super.delete(id);
	}


	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentList() {

		Session session = null;
		Transaction tr = null;
		String resp = "";

		try {
			session = HibernateUtility.getSessionFactory().openSession();

			Criteria cr = session.createCriteria(Agent.class);

			tr = session.beginTransaction();

			List<Agent> list = cr.list();

			tr.commit();

			if (list != null) // SUCCESSFULLY GET DATA
				resp = "{\"status\": 1,\"data\": " + new Gson().toJson(list)
						+ "}";
			else
				// No Data
				resp = "{\"status\": -1}";
			
		} catch (Exception e) {
			e.printStackTrace(System.out);

			// SERVER ERROR
			resp = "{\"status\":0}";
		} finally {
			session.close();
		}

		return resp;
	}		
}
