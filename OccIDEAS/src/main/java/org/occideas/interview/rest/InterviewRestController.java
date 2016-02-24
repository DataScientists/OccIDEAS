package org.occideas.interview.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.interview.service.InterviewService;
import org.occideas.question.service.QuestionService;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interview")
public class InterviewRestController implements BaseRestController<InterviewVO> {

    @Autowired
    private InterviewService service;

    @Autowired
    private QuestionService questionService;

    @GET
    @Path(value = "/getlist")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response listAll() {
        // TODO: Implement this in case required
        return null;
    }

    @GET
    @Path(value = "/get")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response get(@QueryParam("id") Long id) {
        // TODO: Implement this in case required
        return null;
    }

    @Path(value = "/create")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response create(InterviewVO json) {
        InterviewVO interviewVO;
        try {
            interviewVO = service.create(json);
        } catch (Throwable e) {
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(interviewVO).build();
    }

    @Path(value = "/update")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response update(InterviewVO json) {
        // TODO: Implement this in case required
        return null;
    }

    @Path(value = "/delete")
    @POST
    public Response delete(InterviewVO json) {
        try {
            service.delete(json);
        } catch (Throwable e) {
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path(value = "/saveAndNextQ")
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getNextQuestion(InterviewVO interviewVO) {
        service.saveAnswer(interviewVO);

        QuestionVO questionVO;
        try {
            if ((interviewVO.getModule()!=null) && interviewVO.getInterviewId() > 0) { //moving into module
                questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getModule().getIdNode());
            } else if ((interviewVO.getFragment() != null) && interviewVO.getInterviewId() > 0) { //moving into fragment aJSM
                questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getFragment().getIdNode());
            } else if ("multiple".equals(interviewVO.getType())) {
                questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getMultipleAnswerId().get(0));
            } else {
                questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getSingleAnswerId());
            }
        } catch (Throwable e) {
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(questionVO).build();
    }
}
