package org.occideas.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.occideas.AgentGroup;
import org.occideas.HibernateUtility;
import org.occideas.rest.common.AgentCommon;

import com.google.gson.Gson;

@Path("/agentgroup")
public class AgentGroupService extends AgentCommon{
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createAgentGroup(final AgentGroup agentGroup) throws IOException
	{
		return super.create(agentGroup);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentGroup(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateAgentGroup(final AgentGroup agentGroup) throws IOException
	{
		return super.update(agentGroup);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAgentGroup(@QueryParam("id") long id)
	{
		return super.delete(id);
	}

	@GET
	@Path("/getchildagents")
	@Produces(MediaType.APPLICATION_JSON)
	public String getChildAgents(@DefaultValue("all") @QueryParam("group_id") String group_id) {

		Session session = null;
		Transaction tr = null;
		String resp = "";

		try {
			session = HibernateUtility.getSessionFactory().openSession();

			Criteria cr = session.createCriteria(AgentGroup.class);

			if (!group_id.equals("all"))
				cr.add(Restrictions.eq("agentGroup_idAgent", Long.parseLong(group_id)));

			tr = session.beginTransaction();

			List<AgentGroup> list = cr.list();

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

	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentGroupList() {

		Session session = null;
		Transaction tr = null;
		String resp = "";

		try {
			session = HibernateUtility.getSessionFactory().openSession();

			Criteria cr = session.createCriteria(AgentGroup.class);

			tr = session.beginTransaction();

			List<AgentGroup> list = cr.list();

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
