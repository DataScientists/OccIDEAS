package org.occideas.question.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Node;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.QuestionMapper;
import org.occideas.vo.NodeVO;
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
        Node currentAnswer = dao.get(Node.class, idNode);
        Question nextQuestion = (Question) inspectNextQuestion(currentAnswer);
        return mapper.convertToQuestionVO(nextQuestion);
    }

    private Node inspectNextQuestion(Node node) {
        if (node.getChildNodes().isEmpty()) {
            return getNearestQuestion(node);
        } else {
            return node.getChildNodes().get(0);
        }
    }

    private Node getNearestQuestion(Node node) {
        Node father = dao.get(Node.class, Long.valueOf(node.getParentId()));
        if (father instanceof Question) {
            Node grandFather = dao.get(Node.class, Long.valueOf(father.getParentId()));

            for (int i = 0; i < grandFather.getChildNodes().size(); i++) {
                if (grandFather.getChildNodes().get(i).getIdNode() == father.getIdNode()) {
                    if (i < grandFather.getChildNodes().size() - 1) {
                        return grandFather.getChildNodes().get(i + 1);
                    } else {
                        return getNearestQuestion(grandFather);
                    }
                }
            }
        } else {
            return getNearestQuestion(father);
        }

        return null;
    }
}
