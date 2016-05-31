package org.occideas.possibleanswer.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.PossibleAnswerVO;

public interface PossibleAnswerService extends BaseService<PossibleAnswerVO>{

	List<PossibleAnswerVO> findByIdWithChildren(Long id);

}
