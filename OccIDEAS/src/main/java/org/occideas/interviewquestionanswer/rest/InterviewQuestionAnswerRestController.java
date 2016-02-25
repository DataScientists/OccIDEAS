package org.occideas.interviewquestionanswer.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.interviewquestionanswer.service.InterviewQuestionAnswerService;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewquestionanswer")
public class InterviewQuestionAnswerRestController implements BaseRestController<InterviewQuestionAnswerVO>{

	@Autowired
	private InterviewQuestionAnswerService service;	
	
	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<InterviewQuestionAnswerVO> list = new ArrayList<InterviewQuestionAnswerVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getbyinterviewid")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByModuleId(@QueryParam("id") Long id) {
		List<InterviewQuestionAnswerVO> list = new ArrayList<InterviewQuestionAnswerVO>();
		try{
			list = service.findByInterviewId(id);
		}catch(Throwable e){
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
	public Response create(InterviewQuestionAnswerVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(InterviewQuestionAnswerVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete(InterviewQuestionAnswerVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
