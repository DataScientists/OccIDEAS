package org.occideas.interviewanswer.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.base.rest.BaseRestController;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.vo.InterviewAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewanswer")
public class InterviewAnswerRestController implements BaseRestController<InterviewAnswerVO> {

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private InterviewAnswerService service;

	@GET
	@Path(value = "/getlist")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<InterviewAnswerVO> list = new ArrayList<InterviewAnswerVO>();
		try {
			list = service.listAll();
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value = "/getbyinterviewid")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getByInterviewId(@QueryParam("id") Long id) {
		List<InterviewAnswerVO> list = new ArrayList<InterviewAnswerVO>();
		try {
			list = service.findByInterviewId(id);
		} catch (Throwable e) {
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
	public Response create(InterviewAnswerVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete(InterviewAnswerVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(InterviewAnswerVO json) {
		// TODO Auto-generated method stub
		return null;
	}

}
