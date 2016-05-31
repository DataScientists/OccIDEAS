package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewAnswer;
import org.occideas.vo.InterviewAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewAnswerMapperImpl implements InterviewAnswerMapper{

	@Autowired
	private ModuleRuleMapper moduleRuleMapper;
	
	@Override
	public InterviewAnswerVO convertToInterviewAnswerVO(InterviewAnswer answer) {
		if(answer == null){
			return null;
		}
		InterviewAnswerVO vo = new InterviewAnswerVO();
		vo.setId(answer.getId());
		vo.setAnswerId(answer.getAnswerId());
		vo.setDeleted(answer.getDeleted());
		vo.setDescription(answer.getDescription());
		vo.setIdInterview(answer.getIdInterview());
		vo.setName(answer.getName());
		vo.setNodeClass(answer.getNodeClass());
		vo.setNumber(answer.getNumber());
		vo.setParentQuestionId(answer.getParentQuestionId());
		vo.setTopNodeId(answer.getTopNodeId());
		vo.setType(answer.getType());
		vo.setAnswerFreetext(answer.getAnswerFreetext());
		vo.setIsProcessed(answer.isProcessed());
		
		return vo;
	}
	@Override
	public InterviewAnswerVO convertToInterviewAnswerWithRulesVO(InterviewAnswer answer) {
		if(answer == null){
			return null;
		}
		InterviewAnswerVO vo = new InterviewAnswerVO();
		vo.setId(answer.getId());
		vo.setAnswerId(answer.getAnswerId());
		vo.setDeleted(answer.getDeleted());
		vo.setDescription(answer.getDescription());
		vo.setIdInterview(answer.getIdInterview());
		vo.setName(answer.getName());
		vo.setNodeClass(answer.getNodeClass());
		vo.setNumber(answer.getNumber());
		vo.setParentQuestionId(answer.getParentQuestionId());
		vo.setTopNodeId(answer.getTopNodeId());
		vo.setType(answer.getType());
		vo.setAnswerFreetext(answer.getAnswerFreetext());
		vo.setRules(moduleRuleMapper.convertToModuleRuleVOList(answer.getRules()));
		vo.setIsProcessed(answer.isProcessed());
		return vo;
	}

	@Override
	public List<InterviewAnswerVO> convertToInterviewAnswerVOList(List<InterviewAnswer> answerList) {
		   if (answerList == null) {
	            return null;
	        }
	        List<InterviewAnswerVO> list = new ArrayList<InterviewAnswerVO>();
	        for (InterviewAnswer answer : answerList) {
	            list.add(convertToInterviewAnswerVO(answer));
	        }
	        return list;
	}
	@Override
	public List<InterviewAnswerVO> convertToInterviewAnswerWithRulesVOList(List<InterviewAnswer> answerList) {
		   if (answerList == null) {
	            return null;
	        }
	        List<InterviewAnswerVO> list = new ArrayList<InterviewAnswerVO>();
	        for (InterviewAnswer answer : answerList) {
	            list.add(convertToInterviewAnswerWithRulesVO(answer));
	        }
	        return list;
	}

	@Override
	public InterviewAnswer convertToInterviewAnswer(InterviewAnswerVO answerVO) {
		if(answerVO == null){
			return null;
		}
		InterviewAnswer answer = new InterviewAnswer();
		answer.setId(answerVO.getId());
		answer.setAnswerId(answerVO.getAnswerId());
		answer.setDeleted(answerVO.getDeleted());
		answer.setDescription(answerVO.getDescription());
		answer.setIdInterview(answerVO.getIdInterview());
		answer.setName(answerVO.getName());
		answer.setNodeClass(answerVO.getNodeClass());
		answer.setNumber(answerVO.getNumber());
		answer.setParentQuestionId(answerVO.getParentQuestionId());
		answer.setTopNodeId(answerVO.getTopNodeId());
		answer.setType(answerVO.getType());
		answer.setAnswerFreetext(answerVO.getAnswerFreetext());
		answer.setProcessed(answerVO.getIsProcessed());
		return answer;
	}

	@Override
	public List<InterviewAnswer> convertToInterviewAnswerList(List<InterviewAnswerVO> answerVOList) {
		 if (answerVOList == null) {
	            return null;
	        }
	        List<InterviewAnswer> list = new ArrayList<InterviewAnswer>();
	        for (InterviewAnswerVO answer : answerVOList) {
	            list.add(convertToInterviewAnswer(answer));
	        }
	        return list;
	}

}
