package org.occideas.interview.rest;

import java.util.List;

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
import org.occideas.vo.InterviewQuestionAnswerVO;
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
    	try{
			service.update(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
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
    public Response saveAndNextQuestion(InterviewVO interviewVO) {
        service.update(interviewVO);

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
    @POST
    @Path(value = "/nextquestion")
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getNextQuestion(InterviewVO interviewVO) {

        QuestionVO questionVO = null;
        try {
        	if(interviewVO.getQuestionsAsked().size()>0){
        		List<InterviewQuestionAnswerVO> questionsAsked = interviewVO.getQuestionsAsked();
        		String maxNumber = "Z";
        		for(InterviewQuestionAnswerVO iqa: questionsAsked){
        			//check for unanswered questions
        			if(iqa.getPossibleAnswer()!=null){
        				for(QuestionVO question: iqa.getPossibleAnswer().getChildNodes()){     				
            				if(!isQuesitonAnswered(question,questionsAsked)){
            					String number = question.getNumber();
            					if(number.compareTo(maxNumber)<0){
            						maxNumber = number;
            						questionVO = question;	
            					}
            				}
            			}
        				if(questionVO==null){//check for more module questions
        					for(QuestionVO question: interviewVO.getModule().getChildNodes()){     				
                				if(!isQuesitonAnswered(question,questionsAsked)){
                					String number = question.getNumber();
                					if(number.compareTo(maxNumber)<0){
                						maxNumber = number;
                						questionVO = question;	
                					}
                				}
                			}
        				}
        			}     			
        		}
        	} else if ((interviewVO.getModule()!=null) && interviewVO.getInterviewId() > 0) { //moving into module
                questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getModule().getIdNode());
                questionVO.setLink(interviewVO.getModule().getIdNode());
            } else if ((interviewVO.getFragment() != null) && interviewVO.getInterviewId() > 0) { //moving into fragment aJSM
                questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getFragment().getIdNode());
                questionVO.setLink(interviewVO.getModule().getIdNode());
            } 
        	if(questionVO!=null){
        		if("Q_linkedajsm".equalsIgnoreCase(questionVO.getType())){
            		Long linkingQuestionId = questionVO.getIdNode();
            		Long linkingAjsmId = questionVO.getLink();
            		questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), linkingAjsmId);
            		questionVO.setLink(linkingQuestionId);
                }else if("Q_linkedmodule".equalsIgnoreCase(questionVO.getType())){
                	Long linkingQuestionId = questionVO.getIdNode();
            		Long linkingmoduleId = questionVO.getLink();
            		questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), linkingmoduleId);
            		questionVO.setLink(linkingQuestionId);
                }
        	}else{
        		questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), interviewVO.getSingleAnswerId());
        	}
        	
        } catch (Throwable e) {
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        if(questionVO!=null){
        	return Response.ok(questionVO).build();
        }else{
        	return Response.status(Response.Status.NO_CONTENT).build();
        }
        
    }
    private boolean isQuesitonAnswered(QuestionVO q,List<InterviewQuestionAnswerVO> questionsAsked){
    	boolean retValue = false;
    	for(InterviewQuestionAnswerVO iqa: questionsAsked){
    		if(iqa.getQuestion().getIdNode()==q.getIdNode()){
    			retValue = true;
    		}
    	}
    	return retValue;
    }
}
