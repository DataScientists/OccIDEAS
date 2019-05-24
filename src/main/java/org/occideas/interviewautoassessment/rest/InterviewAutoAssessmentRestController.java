package org.occideas.interviewautoassessment.rest;

import org.occideas.interviewautoassessment.service.InterviewAutoAssessmentService;
import org.occideas.rule.service.RuleService;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewAutoAssessmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path("/interviewAutoAssessment")
public class InterviewAutoAssessmentRestController {

  @Autowired
  private InterviewAutoAssessmentService service;

  @Autowired
  private RuleService ruleService;

  @Path(value = "/addAutoAssessment")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response create(InterviewAutoAssessmentVO json) {

    return Response.ok(service.create(json)).build();
  }

  @Path(value = "/saveAutoAssessments")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response saveAutoAssessments(List<InterviewAutoAssessmentVO> json) {
    return Response.ok(service.updateAutoAssessments(json)).build();
  }

  @GET
  @Path(value = "/getByInterviewId")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getByInterviewId(@QueryParam("id") Long id) {
    List<InterviewAutoAssessmentVO> list = new ArrayList<InterviewAutoAssessmentVO>();
    try {
      list = service.findByInterviewId(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/findNodeById")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response findNodeById(@QueryParam("id") Long id) {
    GenericNodeVO vo = null;
    try {
      vo = service.findNodeById(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(vo).build();
  }
}
