package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapperImpl implements QuestionMapper {

	@Autowired
	private PossibleAnswerMapper mapper;

	@Autowired
	private ModuleRuleMapper moduleRuleMapper;

	@Override
	public QuestionVO convertToQuestionVO(Question question) {
		// TODO Auto-generated method stub
		if (question == null) {
			return null;
		}

		QuestionVO questionVO = new QuestionVO();

		questionVO.setIdNode(question.getIdNode());
		questionVO.setName(question.getName());
		questionVO.setDescription(question.getDescription());
		questionVO.setType(question.getType());
		questionVO.setSequence(question.getSequence());
		questionVO.setNumber(question.getNumber());
		questionVO.setParentId(question.getParentId());
		questionVO.setLink(question.getLink());
		questionVO.setTopNodeId(question.getTopNodeId());
		questionVO.setLastUpdated(question.getLastUpdated());
		
		List<PossibleAnswer> childNodes = question.getChildNodes();
		if (!CommonUtil.isListEmpty(childNodes)) {
			questionVO.setChildNodes(mapper.convertToPossibleAnswerVOList(childNodes,true));
		}
		
		questionVO.setOriginalId(question.getOriginalId());
		questionVO.setDeleted(question.getDeleted());
		questionVO.setNodeclass(question.getNodeclass());
		questionVO.setModuleRule(moduleRuleMapper.convertToModuleRuleVOList(question.getModuleRule()));

		return questionVO;
	}
	
	@Override
	public QuestionVO convertToInterviewQuestionVO(Question question) {
		// TODO Auto-generated method stub
		if (question == null) {
			return null;
		}

		QuestionVO questionVO = new QuestionVO();

		questionVO.setIdNode(question.getIdNode());
		questionVO.setName(question.getName());
		questionVO.setDescription(question.getDescription());
		questionVO.setType(question.getType());
		questionVO.setSequence(question.getSequence());
		questionVO.setNumber(question.getNumber());
		questionVO.setParentId(question.getParentId());
		questionVO.setLink(question.getLink());
		questionVO.setTopNodeId(question.getTopNodeId());
		questionVO.setLastUpdated(question.getLastUpdated());
		List<PossibleAnswer> childNodes = question.getChildNodes();
		if (!CommonUtil.isListEmpty(childNodes)) {
			questionVO.setChildNodes(mapper.convertToInterviewPossibleAnswerVOList(childNodes));
		}
		questionVO.setOriginalId(question.getOriginalId());
		questionVO.setDeleted(question.getDeleted());
		questionVO.setNodeclass(question.getNodeclass());
		
		return questionVO;
	}

	@Override
	public List<QuestionVO> convertToQuestionVOList(List<Question> questionEntity) {
		if ( questionEntity == null ) {
            return null;
        }

        List<QuestionVO> list = new ArrayList<QuestionVO>();
        for ( Question question : questionEntity ) {
            list.add( convertToQuestionVO( question) );
        }

        return list;
	}
	@Override
	public List<QuestionVO> convertToInterviewQuestionVOList(List<Question> questionEntity) {
		if ( questionEntity == null ) {
            return null;
        }

        List<QuestionVO> list = new ArrayList<QuestionVO>();
        for ( Question question : questionEntity ) {
            list.add( convertToInterviewQuestionVO( question) );
        }

        return list;
	}

	@Override
	public Question convertToQuestion(QuestionVO questionVO) {
		if (questionVO == null) {
			return null;
		}

		Question question = new Question();

		question.setIdNode(questionVO.getIdNode());
		question.setName(questionVO.getName());
		question.setDescription(questionVO.getDescription());
		question.setType(questionVO.getType());
		question.setSequence(questionVO.getSequence());
		question.setParentId(questionVO.getParentId());
		question.setLastUpdated(questionVO.getLastUpdated());
		List<PossibleAnswerVO> childNodes = questionVO.getChildNodes();
		if (!CommonUtil.isListEmpty(childNodes)) {
			question.setChildNodes(mapper.convertToPossibleAnswerList(childNodes));
		}
		question.setNumber(questionVO.getNumber());
		question.setLink(questionVO.getLink());
		question.setTopNodeId(questionVO.getTopNodeId());
		question.setOriginalId(questionVO.getOriginalId());
		question.setDeleted(questionVO.getDeleted());
		question.setNodeclass(questionVO.getNodeclass());

		return question;
	}

	@Override
	public List<Question> convertToQuestionList(List<QuestionVO> questionVO) {
		if (questionVO == null) {
			return null;
		}

		List<Question> list = new ArrayList<Question>();
		for (QuestionVO questionVO_ : questionVO) {
			list.add(convertToQuestion(questionVO_));
		}

		return list;

	}

}
