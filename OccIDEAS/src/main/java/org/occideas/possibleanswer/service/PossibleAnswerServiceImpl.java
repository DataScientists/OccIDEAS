package org.occideas.possibleanswer.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.vo.PossibleAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PossibleAnswerServiceImpl implements PossibleAnswerService {

	@Autowired
	private BaseDao dao;

	@Autowired
	private PossibleAnswerMapper mapper;
	
	@Autowired
	private QuestionMapper questionMapper;

	@Override
	public List<PossibleAnswerVO> listAll() {
		return null;
	}

	@Override
	public List<PossibleAnswerVO> findById(Long id) {
		PossibleAnswer answer = dao.get(PossibleAnswer.class, id);
		PossibleAnswerVO paVO = mapper.convertToPossibleAnswerVO(answer,false);
		
		Question question = dao.get(Question.class, Long.valueOf(answer.getParentId()));		
		paVO.setParent(questionMapper.convertToQuestionVO(question));
       
        List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
        list.add(paVO);
        return list;
	}
	@Override
	public List<PossibleAnswerVO> findByIdWithChildren(Long id) {
		PossibleAnswer answer = dao.get(PossibleAnswer.class, id);
		PossibleAnswerVO paVO = mapper.convertToPossibleAnswerVO(answer,true);

        List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
        list.add(paVO);
        return list;
	}

	@Override
	public PossibleAnswerVO create(PossibleAnswerVO o) {
		return null;
	}

	@Override
	public void update(PossibleAnswerVO o) {
	}

	@Override
	public void delete(PossibleAnswerVO o) {
	}

	@Override
	public PossibleAnswerVO findAnswerWithRulesById(long id) {
		PossibleAnswer answer = dao.get(PossibleAnswer.class, id);
		return mapper.convertToPossibleAnswerWithModuleRuleVO(answer);
	}

}
