package org.occideas.participant.rest;

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
import org.occideas.entity.ParticipantIntMod;
import org.occideas.interview.service.InterviewService;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.vo.AssessmentFilterVO;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleRuleVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/participant")
public class ParticipantRestController implements BaseRestController<ParticipantVO> {

	//private Logger log = Logger.getLogger(this.getClass());

	@Autowired
    private ParticipantService service;
	
	@Autowired
    private InterviewService interviewService;
	
	@Autowired
    private ModuleService moduleService;

    @GET
    @Path(value = "/getlist")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response listAll() {
    	List<ParticipantVO> list = new ArrayList<ParticipantVO>();
		try{
			list = service.listAllParticipantWithInt();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    
    @POST
    @Path(value = "/getPaginatedParticipantList")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getPaginatedParticipantList(AssessmentFilterVO filterVO) {
    	PageVO<ParticipantIntMod> page = null;
		try{
			page = service.getPaginatedParticipantList(filterVO.getPageNumber(), filterVO.getSize(),filterVO);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(page).build();
    }
    
    @GET
    @Path(value = "/get")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response get(@QueryParam("id") Long id) {
    	List<ParticipantVO> list = new ArrayList<ParticipantVO>();
		try{
			list = service.findById(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
    @GET
    @Path(value = "/getinterviewparticipant")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getInterviewParticipant(@QueryParam("id") Long id) {
    	List<ParticipantVO> list = new ArrayList<ParticipantVO>();
		try{
			list = service.findByIdForInterview(id);
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
    public Response create(ParticipantVO json) {
    	ParticipantVO participantVO;
        try {
        	participantVO = service.create(json);
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(participantVO).build();
    }

    @Path(value = "/update")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response update(ParticipantVO json) {
    	try{ 		
			service.update(json);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
    }

    @Path(value = "/delete")
    @POST
    public Response delete(ParticipantVO json) {
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
    public Response getNextQuestion(ParticipantVO participant) {
    	participant = service.findByIdForInterview(participant.getIdParticipant()).get(0);
        try {
        	 QuestionVO questionVO = getNextInterviewQuestion(participant);
        	 if(questionVO!=null){
             	return Response.ok(questionVO).build();
             }else{    
            	 for(InterviewVO interviewVO:participant.getInterviews()){
     				this.determineFiredRules(interviewVO);
     				
              	} 
             	return Response.status(Response.Status.NO_CONTENT).build();
             }   
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }
    private void determineFiredRules(InterviewVO interview){
    	ArrayList<RuleVO> firedRules = new ArrayList<RuleVO>();
    	ArrayList<RuleVO> rules = new ArrayList<RuleVO>();
    	if(interview.getModule()!=null){
    		ModuleVO module=null;
    		List<ModuleVO> moduleVOList = moduleService.findById(interview.getModule().getIdNode());
    		for(ModuleVO m: moduleVOList){
    			module = m;
    		}
    		
    		for(ModuleRuleVO moduleRule : module.getModuleRule()){
    			rules.add(moduleRule.getRule());
    		}
    		rules = removeDuplicates(rules);   		
    		
    	}else if(interview.getFragment()!=null){
    		FragmentVO module = interview.getFragment();
    		for(ModuleRuleVO moduleRule : module.getModuleRule()){
    			rules.add(moduleRule.getRule());
    		}
    		//remove duplicates
    		rules = removeDuplicates(rules); 
    	}
    	for(RuleVO rule: rules){
			boolean bFired = false;
			for(PossibleAnswerVO  pa: rule.getConditions()){
//				for(InterviewQuestionAnswerVO iqa: interview.getQuestionsAsked()){
//					if(iqa.getPossibleAnswer()!=null){
//						if(pa.getIdNode()==iqa.getPossibleAnswer().getIdNode()){
//	    	    			bFired = true;
//	    	    			break;
//	    	    		}else{
//	    	    			bFired = false;
//	    	    		}
//					}	    		
//    	    	}
			}
			if(bFired){
				firedRules.add(rule);
			}			
		}
    	firedRules = removeDuplicates(firedRules);
    	interview.setFiredRules(firedRules);
    	interviewService.update(interview);
    	if(!(interview.getInterviews().isEmpty())){
    		for(InterviewVO childInterview:interview.getInterviews()){
    			this.determineFiredRules(childInterview);
    		}
    	}
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
	private QuestionVO getNextInterviewQuestion(ParticipantVO participant) {
		QuestionVO questionVO = null;
		for(InterviewVO interviewVO:participant.getInterviews()){
    		questionVO = this.getNearestQuestion(interviewVO);
		}	  	
		return questionVO;
	}
//	private QuestionVO getNextInterviewQuestionFromLinkedInterview(QuestionVO questionVO,InterviewVO interviewVO) {
//		QuestionVO retValue = null;
//		String linkedModuleName = "";
//		//create a new child interview			
//		InterviewVO	childInterviewVO = new InterviewVO();
//		childInterviewVO.setReferenceNumber(interviewVO.getParticipant().getReference());
//		childInterviewVO.setParentId(questionVO.getActiveInterviewId());
//		childInterviewVO.setParticipant(interviewVO.getParticipant());
//		if(questionVO.getType().equalsIgnoreCase("Q_linkedmodule")){
//			for(ModuleVO module:moduleService.findByIdForInterview(questionVO.getLink())){
//				childInterviewVO.setModule(module);
//				linkedModuleName = "Module:"+module.getIdNode()+":"+module.getName();
//			}
//		} else if(questionVO.getType().equalsIgnoreCase("Q_linkedajsm")){
//			for(FragmentVO fragment:fragmentService.findByIdForInterview(questionVO.getLink())){
//				childInterviewVO.setFragment(fragment);
//				linkedModuleName = "AJSM:"+fragment.getIdNode()+":"+fragment.getName();
//			}
//		} 
		//save child interview
//		childInterviewVO = interviewService.create(childInterviewVO);		
		
		//save linking question
//		InterviewQuestionAnswerVO iqa = new InterviewQuestionAnswerVO();
//		iqa.setQuestion(questionVO);
//		iqa.setIdInterview(questionVO.getActiveInterviewId());
		
//		if(childInterviewVO.getModule()!=null){
//			linkedModuleName = "Module:"+childInterviewVO.getModule().getIdNode()+":"+childInterviewVO.getModule().getName();
//		}
//		if(childInterviewVO.getFragment()!=null){
//			linkedModuleName = "Fragment:"+childInterviewVO.getFragment().getIdNode()+":"+childInterviewVO.getFragment().getName();
//		}
//		iqa.setInterviewQuestionAnswerFreetext("Linked "+linkedModuleName);
//		interviewVO.getQuestionsAsked().add(iqa);
//		interviewService.update(interviewVO);	
//		retValue = this.getNearestQuestion(childInterviewVO);
//		return retValue;
//	}
	private QuestionVO getNearestQuestion(InterviewVO interviewVO){
    	QuestionVO questionVO = null;
    	//first check child interviews
		if(!(interviewVO.getInterviews().isEmpty())){ //has child interviews
			for(InterviewVO childInterview:interviewVO.getInterviews()){
				questionVO = getNearestQuestion(childInterview);
				if(questionVO!=null){
					questionVO.setActiveInterviewId(childInterview.getInterviewId());
				}
				
			}
		}
//		List<InterviewQuestionAnswerVO> questionsAsked = new ArrayList<InterviewQuestionAnswerVO>();
//		if(questionVO==null){//not found in child interviews
//			if(interviewVO.getQuestionsAsked().size()==0){//this is the first question in the module
//	    		if(interviewVO.getModule()!=null){
//	    			questionVO = interviewVO.getModule().getChildNodes().get(0);
//	    			questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//				}else if(interviewVO.getFragment()!=null){
//					questionVO = interviewVO.getFragment().getChildNodes().get(0);
//					questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//				} 		
//	    	}else{
//	    		questionsAsked = interviewVO.getQuestionsAsked();
//	    		Collections.sort(questionsAsked);  	
//	    		for(InterviewQuestionAnswerVO iqa: questionsAsked){
//	    			if(iqa.getPossibleAnswer()!=null){
//	    				List<QuestionVO> questions = iqa.getPossibleAnswer().getChildNodes();
//	    				for(QuestionVO question: questions){   
//	        				if(!isQuesitonAnswered(question,questionsAsked)){
//	        					if(question.compareTo(questionVO)<0){
//	        						questionVO = question;    
//	        						questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//	        					}
//	        				}
//	        			}				
//	    			}
//	    		}
//	    	}
//		} 	
//    	if(questionVO==null){//if not found
    		//then check root questions
//			if(interviewVO.getFragment()!=null){//if a aJSM check in root questions list
//				List<QuestionVO> childQuestions = interviewVO.getFragment().getChildNodes();
//				if(childQuestions.size()==0){					
//					for(FragmentVO fragment:fragmentService.findById(interviewVO.getFragment().getIdNode())){
//						childQuestions = fragment.getChildNodes();
//					}
//				}
//    			for(QuestionVO question: childQuestions){     				
//    				if(!isQuesitonAnswered(question,questionsAsked)){					
//    					questionVO = question;	
//    					questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//    					break;
//    				}
//    			}
//    		}else { //must be a module check in module root questions list
//    			List<QuestionVO> childQuestions = interviewVO.getModule().getChildNodes();
//				if(childQuestions.size()==0){					
//					for(ModuleVO module:moduleService.findById(interviewVO.getModule().getIdNode())){
//						childQuestions = module.getChildNodes();
//					}
//				}
//    			for(QuestionVO question: childQuestions){     				
//    				if(!isQuesitonAnswered(question,questionsAsked)){
//    					questionVO = question;	
//    					questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//    					break;
//    				}
//    			}
//    		}	
//    	}
//    	if(questionVO!=null){//if not null check if a linking question
//			if(questionVO.getLink()>0){//this is a linking question
//				questionVO = this.getNextInterviewQuestionFromLinkedInterview(questionVO,interviewVO);				
//			}
//		}
    	return questionVO;
    }
//	private boolean isQuesitonAnswered(QuestionVO q,List<InterviewQuestionAnswerVO> questionsAsked){
//    	boolean retValue = false;
//    	for(InterviewQuestionAnswerVO iqa: questionsAsked){
//    		if(iqa.getQuestion().getIdNode()==q.getIdNode()){
//    			if((iqa.getDeleted()==0)){//not deleted
//    				retValue = true;
//    			}
//    		}
//    	}
//    	return retValue;
    }
//}
