package org.occideas.possibleanswer.service;

import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.vo.PossibleAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PossibleAnswerServiceImpl implements PossibleAnswerService {

	@Autowired
	private BaseDao dao;

	@Autowired
	private PossibleAnswerMapper mapper;

	@Override
	public List<PossibleAnswerVO> listAll() {
		return null;
	}

	@Override
	public List<PossibleAnswerVO> findById(Long id) {
		return mapper.convertToPossibleAnswerVOList(dao.getListbyId(PossibleAnswer.class, id),false);
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

}
