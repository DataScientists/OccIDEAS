package org.occideas.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.entity.HibernateUtility;
import org.occideas.exceptions.GenericException;
import org.occideas.rest.common.AgentCommon;

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
	public Response getAgent(@QueryParam("id") long id)
	{
		AgentGroup agentGroup;
		try {
			agentGroup = super.get(id);
		} catch (GenericException e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.ok(agentGroup).build();
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


	@SuppressWarnings("unchecked")
	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Agent> getAgentList() {

		Session session = null;
		List<Agent> list = null;
		try {
			session = HibernateUtility.getSessionFactory().openSession();
			Criteria cr = session.createCriteria(Agent.class);
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 

		return list;
	}		
}
