package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ModuleRule;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PossibleAnswerMapperImpl implements PossibleAnswerMapper{

	@Autowired
	private QuestionMapper mapper;
	
	@Autowired
    private ModuleRuleMapper ruleMapper;
	
	@Override
	public PossibleAnswerVO convertToPossibleAnswerVOWithFlag(PossibleAnswer answerEntity,
			boolean includeChildNode,
			boolean includeRules) {
		if ( answerEntity == null ) {
            return null;
        }

		PossibleAnswerVO answerVO = new PossibleAnswerVO();

        answerVO.setIdNode( answerEntity.getIdNode() );
        answerVO.setName( answerEntity.getName() );
        answerVO.setDescription( answerEntity.getDescription() );
        answerVO.setType( answerEntity.getType() );
        answerVO.setSequence( answerEntity.getSequence() );
        answerVO.setNumber( answerEntity.getNumber() );
        answerVO.setParentId( answerEntity.getParentId());
        answerVO.setLink( answerEntity.getLink() );
        answerVO.setTopNodeId( answerEntity.getTopNodeId() );
        answerVO.setLastUpdated( answerEntity.getLastUpdated() );
        List<Question> childNodes = new ArrayList<Question>();
        if(includeChildNode){
        	childNodes = answerEntity.getChildNodes();
        	if(!CommonUtil.isListEmpty(childNodes)){
                answerVO.setChildNodes( mapper.convertToQuestionVOList( childNodes ) );
            }
        }
        if(includeRules){
        	List<ModuleRule> moduleRule = answerEntity.getModuleRule();
            if(!CommonUtil.isListEmpty(moduleRule)){
            	answerVO.setModuleRule(ruleMapper.convertToModuleRuleVOList(moduleRule));
            }
        }
        answerVO.setOriginalId( answerEntity.getOriginalId() );
        answerVO.setDeleted( answerEntity.getDeleted() );
        answerVO.setNodeclass( answerEntity.getNodeclass() );
        
        return answerVO;
	}
	
	@Override
	public PossibleAnswerVO convertToPossibleAnswerVO(PossibleAnswer answerEntity,boolean includeChildNode) {
		if ( answerEntity == null ) {
            return null;
        }

		PossibleAnswerVO answerVO = new PossibleAnswerVO();

        answerVO.setIdNode( answerEntity.getIdNode() );
        answerVO.setName( answerEntity.getName() );
        answerVO.setDescription( answerEntity.getDescription() );
        answerVO.setType( answerEntity.getType() );
        answerVO.setSequence( answerEntity.getSequence() );
        answerVO.setNumber( answerEntity.getNumber() );
        answerVO.setParentId( answerEntity.getParentId());
        answerVO.setLink( answerEntity.getLink() );
        answerVO.setTopNodeId( answerEntity.getTopNodeId() );
        answerVO.setLastUpdated( answerEntity.getLastUpdated() );
        List<Question> childNodes = new ArrayList<Question>();
        if(includeChildNode){
        	childNodes = answerEntity.getChildNodes();
        	if(!CommonUtil.isListEmpty(childNodes)){
                answerVO.setChildNodes( mapper.convertToQuestionVOList( childNodes ) );
            }
        	List<ModuleRule> moduleRule = answerEntity.getModuleRule();
            if(!CommonUtil.isListEmpty(moduleRule)){
            	answerVO.setModuleRule(ruleMapper.convertToModuleRuleVOList(moduleRule));
            }
        }
        answerVO.setOriginalId( answerEntity.getOriginalId() );
        answerVO.setDeleted( answerEntity.getDeleted() );
        answerVO.setNodeclass( answerEntity.getNodeclass() );
        
        return answerVO;
	}
	
	@Override
	public PossibleAnswerVO convertToPossibleAnswerVOExcQuestionAnsChild(PossibleAnswer answerEntity) {
		if ( answerEntity == null ) {
            return null;
        }

		PossibleAnswerVO answerVO = new PossibleAnswerVO();

        answerVO.setIdNode( answerEntity.getIdNode() );
        answerVO.setName( answerEntity.getName() );
        answerVO.setDescription( answerEntity.getDescription() );
        answerVO.setType( answerEntity.getType() );
        answerVO.setSequence( answerEntity.getSequence() );
        answerVO.setNumber( answerEntity.getNumber() );
        answerVO.setParentId( answerEntity.getParentId());
        answerVO.setLink( answerEntity.getLink() );
        answerVO.setTopNodeId( answerEntity.getTopNodeId() );
        answerVO.setLastUpdated( answerEntity.getLastUpdated() );
        List<Question> childNodes = new ArrayList<Question>();
        	childNodes = answerEntity.getChildNodes();
        	if(!CommonUtil.isListEmpty(childNodes)){
                answerVO.setChildNodes( mapper.convertToQuestionVOExcludeChildsList( childNodes ) );
            }
        	List<ModuleRule> moduleRule = answerEntity.getModuleRule();
            if(!CommonUtil.isListEmpty(moduleRule)){
            	answerVO.setModuleRule(ruleMapper.convertToModuleRuleVOList(moduleRule));
            }
        answerVO.setOriginalId( answerEntity.getOriginalId() );
        answerVO.setDeleted( answerEntity.getDeleted() );
        answerVO.setNodeclass( answerEntity.getNodeclass() );
        
        return answerVO;
	}
	
	@Override
	public PossibleAnswerVO convertToInterviewPossibleAnswerVO(PossibleAnswer answerEntity) {
		if ( answerEntity == null ) {
            return null;
        }

		PossibleAnswerVO answerVO = new PossibleAnswerVO();

        answerVO.setIdNode( answerEntity.getIdNode() );
        answerVO.setName( answerEntity.getName() );
        answerVO.setDescription( answerEntity.getDescription() );
        answerVO.setType( answerEntity.getType() );
        answerVO.setSequence( answerEntity.getSequence() );
        answerVO.setNumber( answerEntity.getNumber() );
        answerVO.setParentId( answerEntity.getParentId());
        answerVO.setLink( answerEntity.getLink() );
        answerVO.setTopNodeId( answerEntity.getTopNodeId() );
        answerVO.setLastUpdated( answerEntity.getLastUpdated() );
        answerVO.setOriginalId( answerEntity.getOriginalId() );
        answerVO.setDeleted( answerEntity.getDeleted() );
        answerVO.setNodeclass( answerEntity.getNodeclass() );
//        List<Question> childNodes = new ArrayList<Question>();
//        
//        childNodes = answerEntity.getChildNodes();
//        if(!CommonUtil.isListEmpty(childNodes)){
//            answerVO.setChildNodes( mapper.convertToInterviewQuestionVOList( childNodes ) );
//        }
        return answerVO;
	}

	@Override
	public List<PossibleAnswerVO> convertToPossibleAnswerVOList(List<PossibleAnswer> answerEntity,boolean includeChildNodes) {
		if ( answerEntity == null ) {
            return null;
        }

        List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
        for ( PossibleAnswer answer : answerEntity ) {
            list.add( convertToPossibleAnswerVO( answer,includeChildNodes) );
        }

        return list;
	}
	@Override
	public List<PossibleAnswerVO> convertToInterviewPossibleAnswerVOList(List<PossibleAnswer> answerEntity) {
		if ( answerEntity == null ) {
            return null;
        }

        List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
        for ( PossibleAnswer answer : answerEntity ) {
            list.add( convertToInterviewPossibleAnswerVO( answer) );
        }

        return list;
	}

	@Override
	public PossibleAnswer convertToPossibleAnswer(PossibleAnswerVO answerVO) {
		 if ( answerVO == null ) {
	            return null;
	        }

		 PossibleAnswer possibleAnswer = new PossibleAnswer();

	        possibleAnswer.setIdNode( answerVO.getIdNode() );
	        possibleAnswer.setName( answerVO.getName() );
	        possibleAnswer.setDescription( answerVO.getDescription() );
	        possibleAnswer.setType( answerVO.getType() );
	        possibleAnswer.setSequence( answerVO.getSequence() );
	        possibleAnswer.setParentId( answerVO.getParentId() );
	        possibleAnswer.setLastUpdated( answerVO.getLastUpdated() );
	        List<QuestionVO> childNodes = answerVO.getChildNodes();
	        if(!CommonUtil.isListEmpty(childNodes)){
	        	possibleAnswer.setChildNodes( mapper.convertToQuestionList(childNodes) );
	        }
	        possibleAnswer.setNumber( answerVO.getNumber() );
	        possibleAnswer.setLink( answerVO.getLink() );
	        possibleAnswer.setTopNodeId( answerVO.getTopNodeId() );
	        possibleAnswer.setOriginalId( answerVO.getOriginalId() );
	        possibleAnswer.setDeleted( answerVO.getDeleted() );
	        possibleAnswer.setNodeclass( answerVO.getNodeclass() );
	        return possibleAnswer;
	}
	@Override
	public PossibleAnswer convertToPossibleAnswerExModRule(PossibleAnswerVO answerVO) {
		 if ( answerVO == null ) {
	            return null;
	        }

		 PossibleAnswer possibleAnswer = new PossibleAnswer();

	        possibleAnswer.setIdNode( answerVO.getIdNode() );
	        possibleAnswer.setName( answerVO.getName() );
	        possibleAnswer.setDescription( answerVO.getDescription() );
	        possibleAnswer.setType( answerVO.getType() );
	        possibleAnswer.setSequence( answerVO.getSequence() );
	        possibleAnswer.setParentId( answerVO.getParentId() );
	        possibleAnswer.setLastUpdated( answerVO.getLastUpdated() );
	        
	        possibleAnswer.setNumber( answerVO.getNumber() );
	        possibleAnswer.setLink( answerVO.getLink() );
	        possibleAnswer.setTopNodeId( answerVO.getTopNodeId() );
	        possibleAnswer.setOriginalId( answerVO.getOriginalId() );
	        possibleAnswer.setDeleted( answerVO.getDeleted() );
	        possibleAnswer.setNodeclass( answerVO.getNodeclass() );
	        return possibleAnswer;
	}

	@Override
	public List<PossibleAnswer> convertToPossibleAnswerList(List<PossibleAnswerVO> answerVO) {
		if ( answerVO == null ) {
            return null;
        }

        List<PossibleAnswer> list = new ArrayList<PossibleAnswer>();
        for ( PossibleAnswerVO answer_ : answerVO ) {
            list.add( convertToPossibleAnswer( answer_ ) );
        }

        return list;
	}
	@Override
	public List<PossibleAnswer> convertToPossibleAnswerExModRuleList(List<PossibleAnswerVO> answerVO) {
		if ( answerVO == null ) {
            return null;
        }

        List<PossibleAnswer> list = new ArrayList<PossibleAnswer>();
        for ( PossibleAnswerVO answer_ : answerVO ) {
            list.add( convertToPossibleAnswerExModRule( answer_ ) );
        }

        return list;
	}

	@Override
	public PossibleAnswerVO convertToPossibleAnswerVOExModRule(PossibleAnswer answerEntity) {
		if ( answerEntity == null ) {
            return null;
        }

		PossibleAnswerVO answerVO = new PossibleAnswerVO();

        answerVO.setIdNode( answerEntity.getIdNode() );
        answerVO.setName( answerEntity.getName() );
        answerVO.setDescription( answerEntity.getDescription() );
        answerVO.setType( answerEntity.getType() );
        answerVO.setSequence( answerEntity.getSequence() );
        answerVO.setNumber( answerEntity.getNumber() );
        answerVO.setParentId( answerEntity.getParentId());
        answerVO.setLink( answerEntity.getLink() );
        answerVO.setTopNodeId( answerEntity.getTopNodeId() );
        answerVO.setLastUpdated( answerEntity.getLastUpdated() );
        answerVO.setOriginalId( answerEntity.getOriginalId() );
        answerVO.setDeleted( answerEntity.getDeleted() );
        answerVO.setNodeclass( answerEntity.getNodeclass() );
        return answerVO;
	}

	@Override
	public List<PossibleAnswerVO> convertToPossibleAnswerVOExModRuleList(List<PossibleAnswer> answerEntity) {
		if ( answerEntity == null ) {
            return null;
        }

        List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
        for ( PossibleAnswer answer : answerEntity ) {
            list.add( convertToPossibleAnswerVOExModRule(answer));
        }

        return list;
	}
	
	@Override
	public PossibleAnswerVO convertToPossibleAnswerWithModuleRuleVO(PossibleAnswer answerEntity) {
		if(answerEntity == null){
			return null;
		}
		
		PossibleAnswerVO vo = new PossibleAnswerVO();
		vo.setDeleted(answerEntity.getDeleted());
		vo.setDescription(answerEntity.getDescription());
		vo.setIdNode(answerEntity.getIdNode());
		vo.setLink(answerEntity.getLink());
		vo.setModuleRule(ruleMapper.convertToModuleRuleVOList(answerEntity.getModuleRule()));
		vo.setName(answerEntity.getName());
		vo.setNodeclass(answerEntity.getNodeclass());
		vo.setNumber(answerEntity.getNumber());
		vo.setParentId(answerEntity.getParentId());
		vo.setSequence(answerEntity.getSequence());
		vo.setTopNodeId(answerEntity.getTopNodeId());
		vo.setType(answerEntity.getType());
		return vo;
	}

	@Override
	public List<PossibleAnswerVO> convertToPossibleAnswerVOWithFlagList(List<PossibleAnswer> answerEntity,
			boolean includeChildNode, boolean includeRules) {
		if ( answerEntity == null ) {
            return null;
        }

        List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
        for ( PossibleAnswer answer : answerEntity ) {
            list.add( convertToPossibleAnswerVOWithFlag(answer, includeChildNode, includeRules) );
        }

        return list;
	}

}
