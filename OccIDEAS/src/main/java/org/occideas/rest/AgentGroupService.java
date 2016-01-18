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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.occideas.AgentGroup;
import org.occideas.HibernateUtility;
import org.occideas.exceptions.GenericException;
import org.occideas.rest.common.AgentCommon;

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
	public AgentGroup getAgentGroup(@QueryParam("id") long id) throws GenericException
	{
		Session session = null;
		AgentGroup info = null;
		try {
			session = HibernateUtility.getSessionFactory().openSession();
			info = (AgentGroup) session.get(AgentGroup.class, id);
		} catch (Exception e) {
			throw new GenericException("Database failure.",e);
		}

		return info;
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

	@SuppressWarnings("unchecked")
	@GET
	@Path("/getchildagents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChildAgents(@DefaultValue("all") @QueryParam("group_id") String group_id) {

		Session session = null;
		List<AgentGroup> list = null;
		try {
			session = HibernateUtility.getSessionFactory().openSession();
			Criteria cr = session.createCriteria(AgentGroup.class);
			if (!group_id.equals("all"))
				cr.add(Restrictions.eq("agentGroup_idAgent", Long.parseLong(group_id)));
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}

		return Response.ok(list).build();
	}	

	@SuppressWarnings("unchecked")
	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AgentGroup> getAgentGroupList() {
		Session session = null;
		List<AgentGroup> list = null;
		try {
			session = HibernateUtility.getSessionFactory().openSession();
			Criteria cr = session.createCriteria(AgentGroup.class);
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 
		return list;
	}		
}
