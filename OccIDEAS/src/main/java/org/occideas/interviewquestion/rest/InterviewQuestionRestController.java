package org.occideas.interviewquestion.rest;

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
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewquestionanswer")
public class InterviewQuestionRestController implements BaseRestController<InterviewQuestionVO>{

	@Autowired
	private InterviewQuestionService service;	
	
	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value="/getbyinterviewid")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByModuleId(@QueryParam("id") Long id) {
		List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
		try{
			list = service.findByInterviewId(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@Override
	public Response get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response create(InterviewQuestionVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path(value="/save")
	@Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	@Override
	public Response update(InterviewQuestionVO vo) {
		service.update(vo);
		return Response.ok().build();
	}

	@Override
	public Response delete(InterviewQuestionVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
