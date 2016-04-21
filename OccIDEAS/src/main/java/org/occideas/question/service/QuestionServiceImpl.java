package org.occideas.question.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.question.dao.QuestionDao;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    @Qualifier("QuestionDao")
    private QuestionDao qdao;
    
    @Autowired
    private BaseDao dao;

    @Autowired
    private QuestionMapper mapper;

    @Autowired
    private PossibleAnswerMapper paMapper;
    
    @Autowired
    private ModuleMapper modMapper;

    @Override
    public List<QuestionVO> listAll() {
        return mapper.convertToQuestionVOList(dao.getAll(Question.class));
    }

    @Override
    public List<QuestionVO> findById(Long id) {
        Question question = dao.get(Question.class, id);
        QuestionVO questionVO = mapper.convertToQuestionVO(question);
        Long parentId = Long.valueOf(question.getParentId());
        if(parentId!=question.getTopNodeId()){
        	PossibleAnswer answer = dao.get(PossibleAnswer.class, parentId);
            questionVO.setParent(paMapper.convertToPossibleAnswerVO(answer,false));
        }
        
        
        List<QuestionVO> list = new ArrayList<QuestionVO>();
        list.add(questionVO);
        return list;
    }

    @Override
    public QuestionVO create(QuestionVO o) {
        Question question = new Question();
        question.setIdNode(dao.save(mapper.convertToQuestion(o)));
        return mapper.convertToQuestionVO(question);
    }

    @Override
    public void update(QuestionVO o) {
        dao.saveOrUpdate(mapper.convertToQuestion(o));
    }

    @Override
    public void delete(QuestionVO o) {
        dao.delete(mapper.convertToQuestion(o));
    }

	@Override
	public QuestionVO determineNextQuestionByCurrentNumber(String moduleId,String nodeNumber) {
		String number = CommonUtil.getNextQuestionByCurrentNumber(nodeNumber);
		return mapper.convertToQuestionVOReducedDetails(qdao.getQuestionByModuleIdAndNumber(moduleId, number));
	}

	@Override
	public ModuleVO getQuestionWithParentId(Long idNode) {
		return modMapper.convertToModuleVO(qdao.getModuleByParentId(idNode), false);
	}


}
