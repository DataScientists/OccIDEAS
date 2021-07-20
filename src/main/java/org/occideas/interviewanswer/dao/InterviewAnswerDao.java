package org.occideas.interviewanswer.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Constant;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.possibleanswer.dao.IPossibleAnswerDao;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class InterviewAnswerDao implements IInterviewAnswerDao {

  private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private PossibleAnswerService possibleAnswerService;

  @Autowired
  @Lazy
  private SystemPropertyService systemPropertyService;

  @Autowired
  private StudyAgentUtil studyAgentUtil;

  @Autowired
  private IPossibleAnswerDao answerDao;

  @Autowired
  private PossibleAnswerMapper answerMapper;


  @Override
  public List<InterviewAnswer> saveOrUpdate(List<InterviewAnswer> ia) {
    List<InterviewAnswer> list = new ArrayList<>();
    for (InterviewAnswer a : ia) {
      sessionFactory.getCurrentSession().saveOrUpdate(a);
      list.add(a);
    }
    return list;
  }


  @Override
  public List<InterviewAnswer> saveWithClearSession(List<InterviewAnswer> ia) {
    sessionFactory.getCurrentSession().clear();
    List<InterviewAnswer> list = new ArrayList<>();
    for (InterviewAnswer a : ia) {
      sessionFactory.getCurrentSession().saveOrUpdate(a);
      list.add(a);
    }
    return list;
  }

  @Override
  public InterviewAnswer saveOrUpdate(InterviewAnswer answer) {
    sessionFactory.getCurrentSession().saveOrUpdate(answer);
    return answer;
  }


  @Transactional(Transactional.TxType.REQUIRES_NEW)
  @Override
  public List<InterviewAnswer> saveAnswerAndQueueQuestions(List<InterviewAnswer> ia) {
    List<InterviewAnswer> list = new ArrayList<>();
    for (InterviewAnswer a : ia) {
      sessionFactory.getCurrentSession().saveOrUpdate(a);
//            SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
//            if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim()))
//            {
//                SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
//                if (introModule.getValue().equals(String.valueOf(a.getTopNodeId())))
//                {
//                    saveQueuedQuestionsForNormalInterview(a);
//                }
//                else
//                {
//                    saveQueuedQuestionsForStudyAgents(a);
//
//                }
//            }
//            else if (a.getDeleted() == 0)
//            {
//                saveQueuedQuestionsForNormalInterview(a);
//            }
      saveQueueQuestionNew(a);
      list.add(a);
    }
    return list;
  }


  private void saveQueuedQuestionsForNormalInterview(InterviewAnswer a) {
    List<PossibleAnswerVO> answerChildNodes = possibleAnswerService.findByIdWithChildren(a.getAnswerId());
    for (PossibleAnswerVO pa : answerChildNodes) {
      int intQuestionSequence = 1;
      List<QuestionVO> queueQuestions = pa.getChildNodes();
      Collections.sort(queueQuestions);
      for (QuestionVO question : queueQuestions) {
        InterviewQuestion iq = new InterviewQuestion();
        iq.setIdInterview(a.getIdInterview());
        iq.setName(question.getName());
        iq.setNodeClass(question.getNodeclass());
        iq.setNumber(question.getNumber());
        iq.setModCount(a.getModCount());
        iq.setLink(question.getLink());
        iq.setParentAnswerId(a.getAnswerId());
        iq.setQuestionId(question.getIdNode());
        iq.setType(question.getType());
        iq.setDescription(question.getDescription());
        iq.setTopNodeId(a.getTopNodeId());
        iq.setIntQuestionSequence(intQuestionSequence);
        iq.setDeleted(0);
        intQuestionSequence++;
        sessionFactory.getCurrentSession().saveOrUpdate(iq);
      }
    }
  }


  private void saveQueuedQuestionsForStudyAgents(InterviewAnswer a) {
    ModuleVO vo = null;
    NodeVO node = null;
    try {
      vo = studyAgentUtil.getStudyAgentJson(String.valueOf(a.getTopNodeId()));
      node = new StudyAgentUtil().searchNode(vo, a.getAnswerId());
      if (node == null) {
        node = new PossibleAnswerVO();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    PossibleAnswerVO ansVO = (PossibleAnswerVO) node;
    List<QuestionVO> queueQuestions = new ArrayList<>();
    queueQuestions = ansVO.getChildNodes();
    Collections.sort(queueQuestions);
    int intQuestionSequence = 1;
    for (QuestionVO question : queueQuestions) {
      InterviewQuestion iq = new InterviewQuestion();
      iq.setIdInterview(a.getIdInterview());
      iq.setName(question.getName());
      iq.setNodeClass(question.getNodeclass());
      iq.setNumber(question.getNumber());
      iq.setModCount(a.getModCount());
      iq.setLink(question.getLink());
      iq.setParentAnswerId(a.getAnswerId());
      iq.setQuestionId(question.getIdNode());
      iq.setType(question.getType());
      iq.setDescription(question.getDescription());
      iq.setTopNodeId(a.getTopNodeId());
      iq.setIntQuestionSequence(intQuestionSequence);
      iq.setDeleted(0);
      intQuestionSequence++;
      sessionFactory.getCurrentSession().saveOrUpdate(iq);
    }
  }

  private void saveQueueQuestionNew(InterviewAnswer a) {
    List<PossibleAnswerVO> answerChildNodes = possibleAnswerService.findByIdWithChildren(a.getAnswerId());
    SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
    for (PossibleAnswerVO pa : answerChildNodes) {
      int intQuestionSequence = 1;
      List<QuestionVO> queueQuestions = pa.getChildNodes();
      Collections.sort(queueQuestions);
      for (QuestionVO question : queueQuestions) {
        InterviewQuestion iq = new InterviewQuestion();
        iq.setIdInterview(a.getIdInterview());
        iq.setName(question.getName());
        iq.setNodeClass(question.getNodeclass());
        iq.setNumber(question.getNumber());
        iq.setModCount(a.getModCount());
        iq.setLink(question.getLink());
        iq.setParentAnswerId(a.getAnswerId());
        iq.setQuestionId(question.getIdNode());
        iq.setType(question.getType());
        iq.setDescription(question.getDescription());
        iq.setTopNodeId(a.getTopNodeId());
        iq.setIntQuestionSequence(intQuestionSequence);
        iq.setDeleted(0);
        intQuestionSequence++;
        if (shouldQueueQuestion(a, filterStudyAgentFlag, iq)) {
          sessionFactory.getCurrentSession().saveOrUpdate(iq);
        } else {
          System.out.println("Not queing question " + iq.getName());
        }
      }
    }
  }


  private boolean shouldQueueQuestion(InterviewAnswer ia, SystemPropertyVO filterStudyAgentFlag, InterviewQuestion iqQueue) {
    if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
      String[] listOfIdNodes;
      try {
        listOfIdNodes = studyAgentUtil.getStudyAgentCSV(String.valueOf(ia.getTopNodeId()));
        return isQuestionIdStudyAgent(iqQueue, listOfIdNodes);
      } catch (Exception e) {
        log.error("Error queuing question possible that " + ia.getTopNodeId() + " is a IntroModule", e);
      }
    }
    return true;
  }

  private boolean isQuestionIdStudyAgent(InterviewQuestion iqQueue, String[] listOfIdNodes) {
    boolean isExist = studyAgentUtil.doesIdNodeExistInArray(listOfIdNodes, String.valueOf(iqQueue.getQuestionId()));
    if (iqQueue.getLink() > 0) {
      isExist = true;
      log.info("Question id " + iqQueue.getQuestionId() + " is link so including it.");
    } else if (isExist) {
      log.info("Question id " + iqQueue.getQuestionId() + " exist so saving it in queue question.");
    } else {
//            iqQueue.setDeleted(1);
      log.info("Question id " + iqQueue.getQuestionId() + " does not exist so saving it in queue question as deleted.");
    }
    return isExist;
  }


  @Override
  public List<InterviewAnswer> findByInterviewId(Long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewAnswer.class);
    if (interviewId != null) {
      crit.add(Restrictions.eq("idInterview", interviewId));
      crit.add(Restrictions.eq("deleted", 0));
      crit.addOrder(Order.desc("id"));
    }
    List<InterviewAnswer> retValue = crit.list();
    session.clear();
    return retValue;
  }


  @Override
  public List<InterviewAnswer> findByInterviewId(Long interviewId, Long questionId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewAnswer.class);
    if (interviewId != null) {
      crit.add(Restrictions.eq("idInterview", interviewId));
      crit.add(Restrictions.eq("parentQuestionId", questionId));
      crit.addOrder(Order.desc("id"));
    }
    return crit.list();
  }


  @Transactional(Transactional.TxType.REQUIRES_NEW)
  @Override
  public List<InterviewQuestion> saveIntervewAnswersAndGetChildQuestion(List<InterviewAnswer> ia) {
    List<InterviewQuestion> list = new ArrayList<>();
    for (InterviewAnswer a : ia) {
      sessionFactory.getCurrentSession().saveOrUpdate(a);
      if (a.getDeleted() == 0) {
        for (PossibleAnswerVO pa : possibleAnswerService.findByIdWithChildren(a.getAnswerId())) {
          int intQuestionSequence = 1;
          List<QuestionVO> queueQuestions = pa.getChildNodes();
          Collections.sort(queueQuestions);
          for (QuestionVO question : queueQuestions) {
            InterviewQuestion iq = new InterviewQuestion();
            iq.setIdInterview(a.getIdInterview());
            iq.setName(question.getName());
            iq.setNodeClass(question.getNodeclass());
            iq.setNumber(question.getNumber());
            iq.setModCount(a.getModCount());
            iq.setLink(question.getLink());
            iq.setParentAnswerId(a.getAnswerId());
            iq.setQuestionId(question.getIdNode());
            iq.setType(question.getType());
            iq.setDescription(question.getDescription());
            iq.setTopNodeId(a.getTopNodeId());
            iq.setIntQuestionSequence(intQuestionSequence);
            iq.setDeleted(0);
            intQuestionSequence++;
            sessionFactory.getCurrentSession().saveOrUpdate(iq);
            list.add(iq);
          }
        }
      }
    }
    return list;
  }


  @Override
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Interview_Answer").executeUpdate();
  }

}
