package org.occideas.interview.rest;

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

import org.occideas.base.rest.BaseRestController;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewmodule.service.InterviewModuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.rule.service.RuleService;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewModuleVO;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleRuleVO;
import org.occideas.vo.NoteVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interview")
public class InterviewRestController implements BaseRestController<InterviewVO> {

	@Autowired
    private InterviewService service;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RuleService ruleService;
    
    @Autowired
    private InterviewModuleService modService;

    @GET
    @Path(value = "/getlist")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response listAll() {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    
    @GET
    @Path(value = "/getInterviewsWithoutAnswers")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response listAllInterviewsWithoutAnswers() {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.listAllInterviewsWithoutAnswers();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    
    @GET
    @Path(value = "/getAllInterviewId")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getAllInterviewId() {
    	List<Long> interviewIdlist = new ArrayList<Long>();
		try{
			interviewIdlist = service.getInterviewIdlist();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(interviewIdlist).build();
    }
    
    @GET
    @Path(value = "/getlistwithanswers")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response listAllWithAnswers() {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.listAllWithAnswers();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    @GET
    @Path(value = "/getassessments")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response listAssessments() {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.listAssessments();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    @GET
    @Path(value = "/get")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response get(@QueryParam("id") Long id) {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.findById(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    
    @GET
    @Path(value = "/getInterviewWithRules")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getInterviewWithRules(@QueryParam("id") Long id) {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.findByIdWithRules(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    @GET
    @Path(value = "/getInterviewsListWithRules")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getInterviewsWithRules() {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.listAllWithRules();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    
    @GET
    @Path(value = "/getUnprocessedQuestions")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getUnprocessedQuestions(@QueryParam("id") Long id) {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.getUnprocessedQuestions(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    
    @GET
    @Path(value = "/getbyref")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getByRef(@QueryParam("ref") String referenceNumber) {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.findByReferenceNumber(referenceNumber);
			if(list.size()>0){
				return Response.ok(list).build();
			}else{
             	return Response.status(Response.Status.NO_CONTENT).build();
             }
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		
    }
    @GET
    @Path(value = "/updatefiredrules")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response updateFiredRules(@QueryParam("id") Long id) {
    	List<InterviewVO> list = new ArrayList<InterviewVO>();
		try{
			list = service.findByIdWithRules(id);
			for(InterviewVO interviewVO:list){
				interviewVO = this.determineFiredRules(interviewVO);
         	}
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
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
        	e.printStackTrace();
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
    		if(json.getManualAssessedRules()!=null){
    			List<RuleVO> manualAssessedRules = new ArrayList<RuleVO>();
    			for(RuleVO rule:json.getManualAssessedRules()){
        			if(rule.getIdRule()==0){
        				RuleVO newAssessmentRule = ruleService.create(rule);
        				manualAssessedRules.add(newAssessmentRule);
        			}else{
        				manualAssessedRules.add(rule);
        			}
        		}
    			json.setManualAssessedRules(manualAssessedRules);
    		}  
    		NoteVO note = new NoteVO();
        	note.setInterviewId(json.getInterviewId());
        	note.setText("Interview Updated");
        	note.setType("System");
        	json.getNotes().add(note);
			service.update(json);
		}catch(Throwable e){
			e.printStackTrace();
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
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path(value = "/nextquestion")
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getNextQuestion(@QueryParam("parentId") String parentId,@QueryParam("number") String number) {
        try {
        	 QuestionVO questionVO = getNearestQuestion(parentId,number);
        	 if(questionVO!=null){
             	return Response.ok(questionVO).build();
             }else{
             	return Response.status(Response.Status.NO_CONTENT).build();
             }   
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }
    @POST
    @Path(value = "/nextquestionfromqueue")
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getNextQuestion(InterviewVO interview) {
        try {
        	QuestionVO questionVO = null;
        	for(InterviewQuestionVO historyQuestion: interview.getQuestionHistory()){
        		if(!historyQuestion.isProcessed()){
        			for(QuestionVO question: questionService.findById(historyQuestion.getId())){
        				questionVO = question;
        			}
        			break;
        		}
        	}
        	 if(questionVO!=null){
             	return Response.ok(questionVO).build();
             }else{
             	return Response.status(Response.Status.NO_CONTENT).build();
             }   
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }
    
    @POST
	@Path(value="/saveMod")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
	@Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	public Response saveMod(InterviewModuleVO vo) {
    	modService.update(vo);
		return Response.ok(vo).build();
	}
    
    @GET
    @Path(value = "/getInterview")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getInterview(@QueryParam("interviewId") long idinterview) {
        try {
        	 List<InterviewVO> vo = service.getInterview(idinterview);
        	 if(vo!=null){
             	return Response.ok(vo).build();
             }else{
             	return Response.status(Response.Status.NO_CONTENT).build();
             }   
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path(value = "/getInterviewQuestionAnswer")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getInterviewQuestionAnswer(@QueryParam("interviewId") long idinterview) {
        try {
        	 List<InterviewVO> vo = service.getInterviewQuestionAnswer(idinterview);
        	 if(vo!=null){
             	return Response.ok(vo).build();
             }else{
             	return Response.status(Response.Status.NO_CONTENT).build();
             }   
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

	private QuestionVO getNearestQuestion(String parentId,String number){
    	return questionService.determineNextQuestionByCurrentNumber(parentId,number);
    }

    private InterviewVO determineFiredRules(InterviewVO interview){
    	ArrayList<RuleVO> firedRules = new ArrayList<RuleVO>();
    	ArrayList<RuleVO> rules = new ArrayList<RuleVO>();
    	
    	List<ModuleRuleVO> moduleRules = new ArrayList<ModuleRuleVO>();
		for(InterviewAnswerVO ia: interview.getAnswerHistory()){
			//for(InterviewAnswerVO ia:iq.getAnswers()){
				moduleRules.addAll(ia.getRules());
				
			//}		
		}
    	for(ModuleRuleVO moduleRule : moduleRules){
			rules.add(moduleRule.getRule());
		}
		rules = removeDuplicates(rules);  
    	//get list of answer nodes
		List<InterviewAnswerVO> allActualAnswers = interview.getAnswerHistory();
		allActualAnswers = removeDeletedAnswers(allActualAnswers);  
    	for(RuleVO rule: rules){
    		boolean bFired = false;   		
			for(PossibleAnswerVO  pa: rule.getConditions()){
				boolean bPartialFired = false;
				for(InterviewAnswerVO ia:allActualAnswers){
					if(pa.getIdNode()==ia.getAnswerId()){
						bPartialFired = true;
						bFired = true;
	    	    		break;
	    	    	}
				}
				if(!bPartialFired){
					bFired = false;
					break;
				}
			}
			if(bFired){
				firedRules.add(rule);
			}			
		}
    	firedRules = removeDuplicates(firedRules);
    	interview.setFiredRules(firedRules);
    	NoteVO note = new NoteVO();
    	note.setInterviewId(interview.getInterviewId());
    	note.setText("Ran determineFiredRules");
    	note.setType("System");
    	interview.getNotes().add(note);
    	service.update(interview);
    	return interview;
    }
    private List<InterviewAnswerVO> removeDeletedAnswers(List<InterviewAnswerVO> allActualAnswers) {
    	ArrayList<InterviewAnswerVO> retValue = new ArrayList<InterviewAnswerVO>();
    	for(InterviewAnswerVO answer: allActualAnswers){
    		if(answer.getDeleted()==0){
    			retValue.add(answer);
    		}
    	}
    	return retValue;
	}

	private ArrayList<RuleVO> removeDuplicates(List<RuleVO> rules){
    	ArrayList<RuleVO> retValue = new ArrayList<RuleVO>();
    	for(RuleVO rule: rules){
    		if(!retValue.contains(rule)){
    			retValue.add(rule);
    		}
    	}
    	return retValue;
    }
}
