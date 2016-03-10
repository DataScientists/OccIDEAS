package org.occideas.interview.rest;

import java.util.ArrayList;
import java.util.Collections;
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
import org.occideas.fragment.service.FragmentService;
import org.occideas.interview.service.InterviewService;
import org.occideas.module.service.ModuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleRuleVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/interview")
public class InterviewRestController implements BaseRestController<InterviewVO> {

	private Logger log = Logger.getLogger(this.getClass());
	private final String INTRO_MODULE = "M_IntroModule";
	
    @Autowired
    private InterviewService service;

    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private FragmentService fragmentService;
    
    @Autowired
    private ModuleService moduleService;

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
    public Response getNextQuestion(List<InterviewVO> list) {
    	
        try {
        	 QuestionVO questionVO = getInterviewQuestion(list);
        	 if(questionVO!=null){
             	return Response.ok(questionVO).build();
             }else{
             	try {
             		for(InterviewVO interviewVO:list){
                 		this.determineFiredRules(interviewVO);
                 	}
             	}catch (Throwable e) {
                 	 log.error("getNextQuestion badRequest:"+list, e);
                     return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
                 }        	
             	return Response.status(Response.Status.NO_CONTENT).build();
             }   
        } catch (Throwable e) {
        	e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

	private QuestionVO getInterviewQuestion(List<InterviewVO> list) {
		QuestionVO questionVO = null;
		for(InterviewVO interviewVO:list){
			if(interviewVO.isActive()){
				questionVO = this.getNearestQuestion(interviewVO);
			}  
			if(questionVO!=null){
				break;
			}
		}
		if(questionVO==null){
			questionVO = processNonIntroModuleInterview(list);
		}
		if(questionVO==null){
			questionVO = processIntroModuleInterview(list);   
		}
		
		 	  	
		return questionVO;
	}

	private QuestionVO processIntroModuleInterview(List<InterviewVO> list) {
		QuestionVO questionVO = null;	
		for(InterviewVO interviewVO:list){
			if(interviewVO.getModule()!=null){
				if(interviewVO.getModule().getType().equalsIgnoreCase(INTRO_MODULE)){
					questionVO = this.getNearestQuestion(interviewVO);
				}  
	    		if(questionVO!=null){
	    			questionVO.setActiveInterviewId(interviewVO.getInterviewId());
	    			break;
	    		}
			}		
    	}	
		return questionVO;
	}

	private QuestionVO processNonIntroModuleInterview(List<InterviewVO> list) {
		QuestionVO questionVO = null;		
		for(InterviewVO interviewVO:list){
    		if(interviewVO.getModule()!=null){
    			if(!interviewVO.getModule().getType().equalsIgnoreCase(INTRO_MODULE)){
    				questionVO = this.getNearestQuestion(interviewVO);
    			}          			           			
    		} 
    		if(questionVO!=null){
    			questionVO.setActiveInterviewId(interviewVO.getInterviewId());
    			break;
    		}
    	}	
		return questionVO;
	}
    
	//@TODO need to refactor below code, line per method should be max 10 for readability
	private QuestionVO getNearestQuestion(InterviewVO interviewVO){
    	QuestionVO questionVO = null;
    	if(interviewVO.getQuestionsAsked().size()>0){
    		List<InterviewQuestionAnswerVO> questionsAsked = interviewVO.getQuestionsAsked();
    		Collections.sort(questionsAsked);  	
    		for(InterviewQuestionAnswerVO iqa: questionsAsked){
    			if(iqa.getPossibleAnswer()!=null){
    				List<QuestionVO> questions = iqa.getPossibleAnswer().getChildNodes();
    				for(QuestionVO question: questions){   
        				if(!isQuesitonAnswered(question,questionsAsked)){
        					if(question.compareTo(questionVO)<0){
        						questionVO = question;       						
        					}
        				}
        			}				
    			}
    			
    		}
    		if(questionVO==null){
    			if(interviewVO.getFragment()!=null){
    				List<QuestionVO> childQuestions = interviewVO.getFragment().getChildNodes();
    				if(childQuestions.size()==0){					
    					for(FragmentVO fragment:fragmentService.findById(interviewVO.getFragment().getIdNode())){
    						childQuestions = fragment.getChildNodes();
    					}
    				}
        			for(QuestionVO question: childQuestions){     				
        				if(!isQuesitonAnswered(question,questionsAsked)){					
        					questionVO = question;	
        					break;
        				}
        			}
        		}else {
        			List<QuestionVO> childQuestions = interviewVO.getModule().getChildNodes();
    				if(childQuestions.size()==0){					
    					for(ModuleVO module:moduleService.findById(interviewVO.getModule().getIdNode())){
    						childQuestions = module.getChildNodes();
    					}
    				}
        			for(QuestionVO question: childQuestions){     				
        				if(!isQuesitonAnswered(question,questionsAsked)){
        					questionVO = question;	
        					break;
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
        		QuestionVO linkingQuestion = questionVO.clone();
        		Long linkingAjsmId = questionVO.getLink();
        		questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), linkingAjsmId);
        		questionVO.setLinkingQuestion(linkingQuestion);
            }else if("Q_linkedmodule".equalsIgnoreCase(questionVO.getType())){
            	QuestionVO linkingQuestion = questionVO.clone();
        		Long linkingmoduleId = questionVO.getLink();
        		questionVO = questionService.getNextQuestion(interviewVO.getInterviewId(), linkingmoduleId);
        		questionVO.setLinkingQuestion(linkingQuestion);
            }
    	}
    	return questionVO;
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
				for(InterviewQuestionAnswerVO iqa: interview.getQuestionsAsked()){
					if(iqa.getPossibleAnswer()!=null){
						if(pa.getIdNode()==iqa.getPossibleAnswer().getIdNode()){
	    	    			bFired = true;
	    	    			break;
	    	    		}else{
	    	    			bFired = false;
	    	    		}
					}	    		
    	    	}
			}
			if(bFired){
				firedRules.add(rule);
			}			
		}
    	firedRules = removeDuplicates(firedRules);
    	interview.setFiredRules(firedRules);
    	service.update(interview);
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
