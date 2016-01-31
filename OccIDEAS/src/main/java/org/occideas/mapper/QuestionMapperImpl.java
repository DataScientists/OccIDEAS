package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Question;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Autowired
    private NodeMapper nodeMapper;

	@Override
	public QuestionVO convertToQuestionVO(Question question) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuestionVO> convertToQuestionVOList(List<Question> questionList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Question convertToQuestion(QuestionVO questionVO) {
		if ( questionVO == null ) {
            return null;
        }

        Question question = new Question();

        question.setIdNode( questionVO.getIdNode() );
        question.setName( questionVO.getName() );
        question.setDescription( questionVO.getDescription() );
        question.setType( questionVO.getType() );
        question.setSequence( questionVO.getSequence() );
        question.setParentId( questionVO.getParentId());
        question.setLastUpdated( questionVO.getLastUpdated() );
        List<NodeVO> childNodes = questionVO.getChildNodes();
        if(!CommonUtil.isListEmpty(childNodes)){
        	question.setChildNodes( nodeMapper.convertToNodeList(childNodes) );
        }
        question.setNumber( questionVO.getNumber() );
        question.setLink( questionVO.getLink() );
        question.setTopNodeId( questionVO.getTopNodeId() );
        question.setOriginalId( questionVO.getOriginalId() );
        question.setDeleted( questionVO.getDeleted() );
        question.setNodeclass( questionVO.getNodeclass() );

        return question;
	}

	@Override
	public List<Question> convertToQuestionList(List<QuestionVO> nodeVO) {
//		if ( questionVOList == null ) {
//            return null;
//        }
//
//        List<Question> list_ = new ArrayList<Question>();
//        for ( QuestionVO questionVO : questionVOList ) {
//            list_.add( convertToQuestion( questionVO ) );
//        }

        return null;
	}

    
    
}

