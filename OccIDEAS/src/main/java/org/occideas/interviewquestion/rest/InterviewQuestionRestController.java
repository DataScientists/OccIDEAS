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

import org.apache.log4j.Logger;
import org.occideas.base.rest.BaseRestController;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewquestionanswer")
public class InterviewQuestionRestController implements BaseRestController<InterviewQuestionVO>{

	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private InterviewQuestionService service;	
	@Autowired
	private InterviewAnswerService answerService;
	
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
	public Response getByInterviewId(@QueryParam("id") Long id) {
		List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
		try{
			list = service.findByInterviewId(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	
	@GET
	@Path(value="/getIntQuestion")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getIntQuestion( @QueryParam("idInterview") Long idInterview,
			 @QueryParam("questionId") Long questionId){
		InterviewQuestionVO intQuestion = service.findIntQuestion(idInterview,questionId);
		return Response.ok(intQuestion).build();
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
		return Response.ok(service.updateIntQ(vo)).build();
	}
	
	@POST
	@Path(value="/saveLinkAndQueueQuestions")
	@Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	public Response saveLinkAndQueueQuestions(InterviewQuestionVO vo) {
		return Response.ok(service.updateInterviewLinkAndQueueQuestions(vo)).build();
	}
	
	@POST
	@Path(value="/saveAnswers")
	@Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	public Response updateAnswers(List<InterviewAnswerVO> vo) {
		return Response.ok(answerService.updateIntA(vo)).build();
	}
	@POST
	@Path(value="/saveQuestions")
	@Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	public Response updateQuestions(List<InterviewQuestionVO> vo) {
		return Response.ok(service.updateIntQs(vo)).build();
	}
	
	@POST
	@Path(value="/saveAnswersandQueueQuestions")
	@Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	public Response saveAnswersandQueueQuestions(List<InterviewAnswerVO> vo) {
		List<InterviewAnswerVO> saveIntervewAnswersAndQueueQuestions = null;
		try{
			saveIntervewAnswersAndQueueQuestions = answerService.saveIntervewAnswersAndQueueQuestions(vo);
		}catch(Throwable ex){
			log.error("Error on saveAnswersandQueueQuestions:", ex);
			return Response.serverError().build();
		}
		
		return Response.ok(saveIntervewAnswersAndQueueQuestions).build();
	}
	
	@Override
	public Response delete(InterviewQuestionVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
