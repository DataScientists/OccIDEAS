package org.occideas.interviewfiredrules.rest;

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

import org.occideas.interviewfiredrules.service.InterviewFiredRulesService;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewFiredRulesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewfiredrules")
public class InterviewFiredRulesRestController {

	@Autowired
	private InterviewFiredRulesService service;
	
	@Path(value = "/addFiredRules")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response create(InterviewFiredRulesVO json) {
    	return Response.ok(service.create(json)).build();
    }
	
	@GET
	@Path(value="/getByInterviewId")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByInterviewId(@QueryParam("id") Long id) {
		List<InterviewFiredRulesVO> list = new ArrayList<InterviewFiredRulesVO>();
		try{
			list = service.findByInterviewId(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/findNodeById")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response findNodeById(@QueryParam("id") Long id) {
		GenericNodeVO vo = null;
		try{
			vo = service.findNodeById(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(vo).build();
	}
}
