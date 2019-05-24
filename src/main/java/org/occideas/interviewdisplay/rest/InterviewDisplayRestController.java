package org.occideas.interviewdisplay.rest;

import org.occideas.base.rest.BaseRestController;
import org.occideas.interviewdisplay.service.InterviewDisplayService;
import org.occideas.vo.InterviewDisplayAnswerVO;
import org.occideas.vo.InterviewDisplayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path("/interviewdisplay")
public class InterviewDisplayRestController implements BaseRestController<InterviewDisplayVO> {

  @Autowired
  private InterviewDisplayService service;

  @Override
  public Response listAll() {
    List<InterviewDisplayVO> list = new ArrayList<InterviewDisplayVO>();
    try {
      list = service.listAll();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getIntDisplay")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response get(@QueryParam("id") Long id) {
    List<InterviewDisplayVO> list = new ArrayList<InterviewDisplayVO>();
    try {
      list = service.findById(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @Override
  public Response create(InterviewDisplayVO json) {
    // TODO Auto-generated method stub
    return null;
  }

  @Path(value = "/update")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response update(InterviewDisplayVO json) {
    return Response.ok(service.create(json)).build();
  }

  @Path(value = "/updateList")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response updateList(List<InterviewDisplayVO> list) {
    return Response.ok(service.updateList(list)).build();
  }

  @Path(value = "/updateDisplayAnswerList")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response updateDisplayAnswerList(List<InterviewDisplayAnswerVO> list) {
    return Response.ok(service.updateDisplayAnswerList(list)).build();
  }

  @Override
  public Response delete(InterviewDisplayVO json) {
    // TODO Auto-generated method stub
    return null;
  }

}
