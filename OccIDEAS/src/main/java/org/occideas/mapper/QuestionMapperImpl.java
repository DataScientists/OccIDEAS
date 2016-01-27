package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Question;
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
	public Question convertToNode(QuestionVO questionVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Question> convertToQuestionList(List<QuestionVO> questionVO) {
		// TODO Auto-generated method stub
		return null;
	}

    
    
}

