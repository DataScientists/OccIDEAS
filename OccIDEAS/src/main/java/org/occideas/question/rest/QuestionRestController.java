package org.occideas.question.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.module.service.ModuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/question")
public class QuestionRestController implements BaseRestController<QuestionVO>{

	@Autowired
	private QuestionService service;
	
	@Autowired
	private ModuleService moduleService;
	
	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<QuestionVO> list = new ArrayList<QuestionVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value="/get")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response get(@QueryParam("id") Long id) {
		List<QuestionVO> list = new ArrayList<QuestionVO>();
		try{
			list = service.findById(id);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getMaxId")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getMaxId() {
		Long longVal = null;
		try{
			longVal = moduleService.getMaxId();
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(longVal).build();
	}

	@Path(value="/create")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response create(QuestionVO json) {
		try{
			service.create(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value="/update")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response update(QuestionVO json) {
		try{
			service.update(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value="/delete")
	@POST
	public Response delete(QuestionVO json) {
		try{
			service.delete(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
}
