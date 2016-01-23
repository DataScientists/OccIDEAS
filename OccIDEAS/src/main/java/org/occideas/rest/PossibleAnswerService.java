package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.PossibleAnswer;
import org.occideas.rest.common.NodeCommon;

@Path("/possibleAnswer")
public class PossibleAnswerService extends NodeCommon
{
	public PossibleAnswerService() 
	{
		super.childClass = PossibleAnswer.class;
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createPossibleAnswer(final PossibleAnswer possibleAnswer) throws IOException
	{
		return super.create(possibleAnswer);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getPossibleAnswer(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Node updatePossibleAnswer(final PossibleAnswer possibleAnswer) throws IOException
	{
		return super.update(possibleAnswer);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void deletePossibleAnswer(@QueryParam("id") long id)
	{
		super.delete(id);
	}
}
