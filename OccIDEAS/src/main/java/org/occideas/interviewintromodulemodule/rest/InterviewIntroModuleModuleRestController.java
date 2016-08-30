package org.occideas.interviewintromodulemodule.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.interviewintromodulemodule.service.InterviewIntroModuleModuleService;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewintromodule")
public class InterviewIntroModuleModuleRestController {
	
	@Autowired
	private InterviewIntroModuleModuleService service;

	@GET
	@Path(value = "/findInterviewByModuleId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response findInterviewByModuleId(@QueryParam("id") Long id) {
		List<InterviewIntroModuleModuleVO> list = null;
		try {
			list = service.findInterviewByModuleId(Long.valueOf(id));
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value = "/findModulesByInterviewId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response findModulesByInterviewId(@QueryParam("id") Long id) {
		List<InterviewIntroModuleModuleVO> list = null;
		try {
			list = service.findModulesByInterviewId(Long.valueOf(id));
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	
}
