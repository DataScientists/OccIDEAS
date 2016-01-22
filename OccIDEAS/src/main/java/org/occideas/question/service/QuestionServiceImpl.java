package org.occideas.question.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Question;
import org.occideas.mapper.QuestionMapper;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService{

	@Autowired
	private BaseDao dao;
	
	@Autowired
	private QuestionMapper mapper;
	
	@Override
	public List<QuestionVO> listAll() {
		return mapper.convertToQuestionVOList(dao.getAll(Question.class));
	}

	@Override
	public List<QuestionVO> findById(Long id) {
		Question question = dao.get(Question.class,id);
		QuestionVO questionVO = mapper.convertToQuestionVO(question);
		List<QuestionVO> list = new ArrayList<QuestionVO>();
		list.add(questionVO);
		return list;
	}

	@Override
	public QuestionVO create(QuestionVO o) {
		Question question = dao.save(mapper.convertToNode(o));
		return mapper.convertToQuestionVO(question);
	}

	@Override
	public QuestionVO update(QuestionVO o) {
		Question question = dao.merge(mapper.convertToNode(o));
		return mapper.convertToQuestionVO(question);
	}

	@Override
	public void delete(QuestionVO o) {
		dao.delete(mapper.convertToNode(o));
	}

}
