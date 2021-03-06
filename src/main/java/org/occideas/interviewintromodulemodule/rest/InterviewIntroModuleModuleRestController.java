package org.occideas.interviewintromodulemodule.rest;

import org.occideas.interviewintromodulemodule.service.InterviewIntroModuleModuleService;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

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

  @GET
  @Path(value = "/findInterviewIdByModuleId")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response findInterviewIdByModuleId(@QueryParam("id") Long id) {
    List<InterviewIntroModuleModuleVO> list = null;
    try {
      list = service.findInterviewIdByModuleId(Long.valueOf(id));
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/findNonIntroById")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response findNonIntroById(@QueryParam("id") Long id) {
    List<InterviewIntroModuleModuleVO> list = null;
    try {
      list = service.findNonIntroById(Long.valueOf(id));
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getDistinctModules")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getDistinctModules() {
    List<InterviewIntroModuleModuleVO> list = null;
    try {
      list = service.getDistinctModules();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

}
