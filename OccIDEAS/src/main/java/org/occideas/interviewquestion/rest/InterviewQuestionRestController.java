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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.base.rest.BaseRestController;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interviewquestionanswer")
public class InterviewQuestionRestController implements BaseRestController<InterviewQuestionVO> {

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private InterviewQuestionService service;
	@Autowired
	private InterviewAnswerService answerService;

	@POST
	@Path(value = "/updateModuleNameForInterviewId")
	public Response updateModuleNameForInterviewId(@QueryParam("id") long id, @QueryParam("newName") String newName) {
		try {
			service.updateModuleNameForInterviewId(id, newName);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@GET
	@Path(value = "/getlist")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
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
		List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
		try {
			list = service.findByInterviewId(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value = "/getIntQuestion")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntQuestion(@QueryParam("idInterview") Long idInterview,
			@QueryParam("questionId") Long questionId) {
		InterviewQuestionVO intQuestion = service.findIntQuestion(idInterview, questionId);
		return Response.ok(intQuestion).build();
	}

	@GET
	@Path(value = "/getInterviewQuestion")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getInterviewQuestion(@QueryParam("interviewQuestionId") Long interviewQuestionId) {
		List<InterviewQuestionVO> list = service.findById(interviewQuestionId);
		InterviewQuestionVO interviewQuestion = null;
		for (InterviewQuestionVO iq : list) {
			interviewQuestion = iq;
		}
		return Response.ok(interviewQuestion).build();
	}

	/**
	 * Used for edit question popup
	 */
	@GET
	@Path(value = "/getInterviewQuestionByQId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getInterviewQuestionByQuestionId(@QueryParam("questionId") Long questionId,
			@QueryParam("interviewId") Long interviewId, @QueryParam("parentId") Long parentQuestionId) {

		List<InterviewQuestionVO> list = service.findById(questionId, interviewId);
		InterviewQuestionVO interviewQuestion = null;
		for (InterviewQuestionVO iq : list) {
			interviewQuestion = iq;
		}
		return Response.ok(interviewQuestion).build();
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
	@Path(value = "/save")
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public Response update(InterviewQuestionVO vo) {
		return Response.ok(service.updateIntQ(vo)).build();
	}

	@POST
	@Path(value = "/saveLinkAndQueueQuestions")
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	public Response saveLinkAndQueueQuestions(InterviewQuestionVO vo) {
		return Response.ok(service.updateInterviewLinkAndQueueQuestions(vo)).build();
	}

	@POST
	@Path(value = "/saveAnswers")
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAnswers(List<InterviewAnswerVO> vo) {
		return Response.ok(answerService.updateIntA(vo)).build();
	}

	@POST
	@Path(value = "/saveQuestions")
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQuestions(List<InterviewQuestionVO> vo) {
		return Response.ok(service.updateIntQs(vo)).build();
	}

	@POST
	@Path(value = "/saveAnswersandQueueQuestions")
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAnswersandQueueQuestions(List<InterviewAnswerVO> vo) {
		List<InterviewAnswerVO> saveIntervewAnswersAndQueueQuestions = null;
		try {
			saveIntervewAnswersAndQueueQuestions = answerService.saveIntervewAnswersAndQueueQuestions(vo);
		} catch (Throwable ex) {
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

	@GET
	@Path(value = "/findQuestionsByNodeId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response findQuestionsByNodeId(@QueryParam("id") Long questionId) {
		List<InterviewQuestionVO> result = null;

		try {
			result = service.findQuestionsByNodeId(questionId);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}

	@GET
	@Path(value = "/getInterviewQuestionsByNodeIdAndIntId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getInterviewQuestionsByNodeIdAndIntId(@QueryParam("questionId") Long questionId,
			@QueryParam("idInterview") Long idInterview){
		List<InterviewQuestionVO> result = null;

		try {
			result = service.getInterviewQuestionsByNodeIdAndIntId(questionId,idInterview);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
}
