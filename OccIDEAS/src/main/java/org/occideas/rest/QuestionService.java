package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.occideas.Node;
import org.occideas.Question;
import org.occideas.rest.common.NodeCommon;

@Path("/question")
public class QuestionService extends NodeCommon
{
	public QuestionService() 
	{
		super.childClass = Question.class;
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createQuestion(final Question question) throws IOException
	{
		return super.create(question);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getQuestion(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Node updateQuestion(final Question question) throws IOException
	{
		return super.update(question);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteQuestion(@QueryParam("id") long id)
	{
		super.delete(id);
	}
}
