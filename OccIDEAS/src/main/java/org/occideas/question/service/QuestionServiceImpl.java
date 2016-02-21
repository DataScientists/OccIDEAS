package org.occideas.question.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.*;
import org.occideas.interview.dao.InterviewDao;
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

    @Autowired
    private InterviewDao interviewDao;

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
    public QuestionVO getNextQuestion(long interviewId, long idNode) {
        Question nextQuestion = null;
        Node node = dao.get(Node.class, idNode);
        if (node instanceof PossibleAnswer) {
            PossibleAnswer currentAnswer = dao.get(PossibleAnswer.class, idNode);
            nextQuestion = inspectNextQuestion(interviewId, currentAnswer);
        } else if (node instanceof Module) {// Only happen when user hit start interview
            nextQuestion = ((Module) node).getChildNodes().get(0);
        }
        return mapper.convertToQuestionVO(nextQuestion);
    }

    private Question inspectNextQuestion(long interviewId, PossibleAnswer node) {
        if (node.getChildNodes().isEmpty()) {
            return getNearestQuestion(interviewId, node);
        } else {
            return node.getChildNodes().get(0);
        }
    }

    private Question getNearestQuestion(long interviewId, PossibleAnswer node) {
        // Get question who is father of answer
        Question father = dao.get(Question.class, Long.valueOf(node.getParentId()));

        if ("Q_multiple".equals(father.getType())) {
            // Get list interview-question-answer by interview and question
            List<InterviewQuestionAnswer> iqas = interviewDao.findById(interviewId, father.getIdNode(), null);
            for (int i = 0; i < iqas.size(); i++) {
                if (node.getIdNode() == iqas.get(i).getAnswer().getIdNode()) {// If found current answer in answer list
                    if (i < iqas.size() - 1) {
                        return inspectNextQuestion(interviewId, dao.get(PossibleAnswer.class, iqas.get(i + 1).getAnswer().getIdNode()));
                    } else {
                        PossibleAnswer grandFather = dao.get(PossibleAnswer.class, Long.valueOf(father.getParentId()));
                        if (grandFather == null) {
                            Module grandFatherModule = dao.get(Module.class, Long.valueOf(father.getParentId()));
                            for (int j = 0; j < grandFatherModule.getChildNodes().size(); j++) {
                                if (grandFatherModule.getChildNodes().get(j).getIdNode() == father.getIdNode()) {
                                    if (j < grandFatherModule.getChildNodes().size() - 1) {
                                        return grandFatherModule.getChildNodes().get(j + 1);
                                    } else {
                                        return null;
                                    }
                                }
                            }
                        } else {
                            return getNearestQuestion(interviewId, grandFather);
                        }
                    }
                }
            }

        } else {
            PossibleAnswer grandFather = dao.get(PossibleAnswer.class, Long.valueOf(father.getParentId()));

            if (grandFather == null) {
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
            } else {
                for (int i = 0; i < grandFather.getChildNodes().size(); i++) {
                    if (grandFather.getChildNodes().get(i).getIdNode() == father.getIdNode()) {
                        if (i < grandFather.getChildNodes().size() - 1) {
                            return grandFather.getChildNodes().get(i + 1);
                        } else {
                            return getNearestQuestion(interviewId, grandFather);
                        }
                    }
                }
            }
        }

        return null;
    }
}
