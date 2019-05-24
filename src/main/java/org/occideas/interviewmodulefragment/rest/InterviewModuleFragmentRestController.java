package org.occideas.interviewmodulefragment.rest;

import org.occideas.interviewmodulefragment.service.InterviewModuleFragmentService;
import org.occideas.vo.InterviewModuleFragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/interviewmodulefragment")
public class InterviewModuleFragmentRestController {

  @Autowired
  private InterviewModuleFragmentService service;

  @GET
  @Path(value = "/findInterviewByFragmentId")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response findInterviewByFragmentId(@QueryParam("id") Long id) {
    List<InterviewModuleFragmentVO> list = null;
    try {
      list = service.findInterviewByFragmentId(Long.valueOf(id));
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/findFragmentsByInterviewId")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response findFragmentByLinterviewId(@QueryParam("id") Long id) {
    List<InterviewModuleFragmentVO> list = null;
    try {
      list = service.findFragmentByInterviewId(Long.valueOf(id));
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

}
