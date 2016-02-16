package org.occideas.question.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Module;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.QuestionMapper;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

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
        Question question = dao.get(Question.class, id);
        QuestionVO questionVO = mapper.convertToQuestionVO(question);
        List<QuestionVO> list = new ArrayList<QuestionVO>();
        list.add(questionVO);
        return list;
    }

    @Override
    public QuestionVO create(QuestionVO o) {
        Question question = dao.save(mapper.convertToQuestion(o));
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
    public QuestionVO getNextQuestion(long idNode) {
    	PossibleAnswer currentAnswer = dao.get(PossibleAnswer.class, idNode);
        Question nextQuestion = (Question) inspectNextQuestion(currentAnswer);
        return mapper.convertToQuestionVO(nextQuestion);
    }

    private Question inspectNextQuestion(PossibleAnswer node) {
        if (node.getChildNodes().isEmpty()) {
            return getNearestQuestion(node);
        } else {
            return node.getChildNodes().get(0);
        }
    }

    private Question getNearestQuestion(PossibleAnswer node) {
        Question father = dao.get(Question.class, Long.valueOf(node.getParentId()));
        	PossibleAnswer grandFather = dao.get(PossibleAnswer.class, Long.valueOf(father.getParentId()));

        	if(grandFather==null){
        		Module grandFatherModule = dao.get(Module.class, Long.valueOf(father.getParentId()));
        		for (int i = 0; i < grandFatherModule.getChildNodes().size(); i++) {
                    if (grandFatherModule.getChildNodes().get(i).getIdNode() == father.getIdNode()) {
                        if (i < grandFatherModule.getChildNodes().size() - 1) {
                            return grandFatherModule.getChildNodes().get(i + 1);
                        } else {
                            return null;
                        }
                    }
                }
        	}else{
        		for (int i = 0; i < grandFather.getChildNodes().size(); i++) {
                    if (grandFather.getChildNodes().get(i).getIdNode() == father.getIdNode()) {
                        if (i < grandFather.getChildNodes().size() - 1) {
                            return grandFather.getChildNodes().get(i + 1);
                        } else {
                            return getNearestQuestion(grandFather);
                        }
                    }
                }
        	}
            
        return null;
    }
}
