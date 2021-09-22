package org.occideas.interviewquestion.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.*;
import org.occideas.fragment.dao.IFragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.module.service.ModuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.CommonUtil;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class InterviewQuestionDao implements IInterviewQuestionDao {

    private final String UNIQUE_INT_QUESTION_SQL = "select distinct(a.question_id) as question_id,a.id,a.idinterview,"
            + "a.type,a.name,a.topNodeId, a.nodeClass,a.parentModuleId,"
            + "a.modCount,a.parentAnswerId,a.link, a.deleted,a.isProcessed,"
            + "a.description,a.number,a.intQuestionSequence,a.lastUpdated "
            + "from Interview_Question a, Interview_Question b "
            + "where a.question_id>0 and a.deleted = 0 and b.deleted = 0 and a.idinterview =b.idinterview and b.topNodeId in (:param) ";
    private final String PROCESSED_FRAGMENT = "select idNode from Node a, "
            + " (select if(parentModuleId > 0, parentModuleId, parentAnswerId) as parentModuleId"
            + " from Interview_Question where id = :id" + " and isProcessed = 1" + " ) b"
            + " where (a.topNodeId = b.parentModuleId" + " or a.parent_idNode = b.parentModuleId)"
            + " and a.link = :link";
    private Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    @Lazy
    private QuestionService questionService;
    @Autowired
    @Lazy
    private SystemPropertyService systemPropertyService;
    @Autowired
    private SystemPropertyDao systemPropertyDao;
    @Autowired
    @Lazy
    private ModuleService moduleService;
    @Autowired
    @Lazy
    private StudyAgentUtil studyAgentUtil;
    @Autowired
    private IModuleDao moduleDao;
    @Autowired
    private IFragmentDao fragmentDao;
    @Autowired
    @Lazy
    private ModuleMapper moduleMapper;
    @Autowired
    @Lazy
    private FragmentMapper fragmentMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> getUniqueInterviewQuestions(String[] filterModule) {
        System.out.println("Start getUniqueInterviewQuestions:" + new Date());
        final Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(UNIQUE_INT_QUESTION_SQL).addEntity(InterviewQuestion.class);
        sqlQuery.setParameterList("param", filterModule);
        List<InterviewQuestion> list = sqlQuery.list();
        System.out.println("Complete getUniqueInterviewQuestions:" + new Date());
        return list;
    }

    @Override
    public void updateModuleNameForInterviewId(long id, String newName) {
        Session session = sessionFactory.getCurrentSession();
        String hqlUpdate = "update InterviewQuestion iq set iq.name = :newName where iq.id = :id";
        session.createQuery(hqlUpdate).setString("newName", newName).setLong("id", id).executeUpdate();
    }

    @Override
    public InterviewQuestion save(InterviewQuestion iq) {
        sessionFactory.getCurrentSession().saveOrUpdate(iq);
        return iq;
    }

    @Override
    public void delete(InterviewQuestion iq) {
        sessionFactory.getCurrentSession().delete(iq);
    }

    @Override
    public InterviewQuestion get(Long id) {
        return (InterviewQuestion) sessionFactory.getCurrentSession().get(InterviewQuestion.class, id);
    }

    @Override
    public InterviewQuestion merge(InterviewQuestion iq) {
        return (InterviewQuestion) sessionFactory.getCurrentSession().merge(iq);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public InterviewQuestion saveOrUpdate(InterviewQuestion iq) {
        sessionFactory.getCurrentSession().saveOrUpdate(iq);
        return iq;
    }

    @Override
    public InterviewQuestion saveOrUpdateSingleTransaction(InterviewQuestion iq) {
        sessionFactory.getCurrentSession().saveOrUpdate(iq);
        return iq;
    }

    @Override
    public List<InterviewQuestion> saveOrUpdate(List<InterviewQuestion> iqs) {
        List<InterviewQuestion> list = new ArrayList<>();
        for (InterviewQuestion iq : iqs) {
            sessionFactory.getCurrentSession().saveOrUpdate(iq);
            list.add(iq);
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void preloadAllModules() {
        List<JobModule> modules = moduleDao.getAll(false);
        List<ModuleVO> voList = moduleMapper.convertToModuleVOList(modules, false);
        for (ModuleVO moduleVO : voList) {
            try {
                studyAgentUtil.createStudyAgentJson(String.valueOf(moduleVO.getIdNode()), moduleVO, true);
            } catch (Exception e) {
                log.error("Error creating study agent module json for "
                        + moduleVO.getName() + "-" + moduleVO.getIdNode(), e);
            }
        }
        List<Fragment> fragments = fragmentDao.getAll();
        List<FragmentVO> fragmentVOList = fragmentMapper.convertToFragmentVOList(fragments, false);
        for (FragmentVO fragmentVO : fragmentVOList) {
            try {
                studyAgentUtil.createStudyAgentJson(String.valueOf(fragmentVO.getIdNode()), fragmentVO, true);
            } catch (Exception e) {
                log.error("Error creating study agent fragment json for "
                        + fragmentVO.getName() + "-" + fragmentVO.getIdNode(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public SystemPropertyVO preloadActiveIntro() {
        SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
        if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
            // get intro id
            SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
            if (introModule == null) {
                log.error("no intro module set");
            } else {
                Node node = moduleDao.getNodeById(Long.valueOf(introModule.getValue()));
                System.out.println("generateCSVForChildAJSMandModule Started");
                generateCSVForChildAJSMandModule(node);
                System.out.println("generateCSVForChildAJSMandModule Ended");
            }
        }
        return filterStudyAgentFlag;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public SystemPropertyVO preloadFilterStudyAgent(Long idNode) {
        SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
        if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
            Node node = moduleDao.getNodeById(idNode);
            System.out.println("generateCSVForChildAJSMandModule Started idNode=" + idNode);
            links.add(idNode);
            generateCSVForChildAJSMandModule(node);
            System.out.println("generateCSVForChildAJSMandModule Ended idNode=" + idNode);
        }
        return filterStudyAgentFlag;
    }

    /*==============================================================================================*/
    /* @author: TD */
    /*==============================================================================================*/
    private List<Long> links = new ArrayList<>();

    private void generateCSVForChildAJSMandModule(Node node) {
        generateLinks(node);

        int i = 0;
        int iSize = links.size();
        for (Long link : links) {
            System.out.println(i + " of " + iSize);
            generateCSVFile(link);
            i++;
        }

        links = new ArrayList<>();
    }

    private void generateLinks(Node node) {
        for (Object obj : node.getChildNodes()) {
            if (obj instanceof Question) {
                Long link = ((Node) obj).getLink();
                if (link > 0) {
                    addToLinkList(link);
                    processFragmentQuestion(link);
                }
            }
            generateLinks((Node) obj);
        }
    }

    private void processFragmentQuestion(Long link) {
        Node node = moduleDao.getNodeById(link);
        if (node != null) {
            for (Object object : node.getChildNodes()) {
                if (object instanceof Question && ((Question) object).getLink() > 0) {
                    addToLinkList(((Question) object).getLink());
                }
                generateLinks((Node) object);
            }
        }
    }

    private void addToLinkList(Long val) {
        if (!links.contains(val)) {
            links.add(val);
        }
    }
    /*==============================================================================================*/

    private void generateCSVFile(Long link) {
        String moduleId = String.valueOf(link);
        List<String> listOfIdNodes = moduleService.getFilterStudyAgent(Long.valueOf(moduleId));
        try {
            if (listOfIdNodes != null && !listOfIdNodes.isEmpty()) {
                SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
                if (moduleId.equals(introModule.getValue())) {
                    return;
                }
                studyAgentUtil.createStudyAgentCSV(moduleId, listOfIdNodes, true);
            }
        } catch (IOException e) {
            log.error("error in creating file " + moduleId, e);
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public InterviewQuestion saveInterviewLinkAndQueueQuestions(InterviewQuestion iq) {
        iq.setProcessed(true);
        sessionFactory.getCurrentSession().saveOrUpdate(iq);
        int intQuestionSequence = iq.getIntQuestionSequence();
        long parentModuleId = iq.getLink();
        loopChildQuestionsAndQueue(iq, intQuestionSequence, parentModuleId);
        return iq;
    }

    private void loopChildQuestionsAndQueue(InterviewQuestion iq, int intQuestionSequence, long parentModuleId) {
        List<QuestionVO> queueQuestions = new ArrayList<>();
        queueQuestions = questionService.getQuestionsWithParentId(String.valueOf(parentModuleId));
        Collections.sort(queueQuestions);
        SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
        for (QuestionVO question : queueQuestions) {
            InterviewQuestion iqQueue = new InterviewQuestion();
            iqQueue.setIdInterview(iq.getIdInterview());
            iqQueue.setName(question.getName());
            iqQueue.setDescription(question.getDescription());
            iqQueue.setNodeClass(question.getNodeclass());
            iqQueue.setNumber(question.getNumber());
            iqQueue.setModCount(iq.getModCount());
            iqQueue.setLink(question.getLink());
            iqQueue.setType(question.getType());
            iqQueue.setParentModuleId(question.getTopNodeId());
            iqQueue.setQuestionId(question.getIdNode());
            iqQueue.setTopNodeId(question.getTopNodeId());
            iqQueue.setIntQuestionSequence(++intQuestionSequence);
            iqQueue.setDeleted(0);
            if (shouldQueueQuestion(iq, filterStudyAgentFlag, iqQueue)) {
                sessionFactory.getCurrentSession().saveOrUpdate(iqQueue);
            }
        }
    }

    private boolean shouldQueueQuestion(InterviewQuestion iq, SystemPropertyVO filterStudyAgentFlag, InterviewQuestion iqQueue) {
        if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
            SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
            if (introModule == null) {
                log.error("no intro module set");
                return false;
            }

            try {

                if (introModule.getValue().equals(String.valueOf(iq.getLink()))) {
                    return true;
                } else {
                    boolean filterAndCreateCSV = false;
                    try {
                        if (!studyAgentUtil.doesStudyAgentCSVExist(String.valueOf(iq.getLink()))) {
                            filterAndCreateCSV = true;
                        }
                    } catch (IOException e1) {
                        log.error("Unable to access file id node " + iq.getLink(), e1);
                    }
                    String[] listOfIdNodes = null;
                    if (filterAndCreateCSV) {
                        List<String> list = moduleService
                                .getFilterStudyAgent(Long.valueOf(iq.getLink()));
                        listOfIdNodes = list.toArray(new String[0]);
                        studyAgentUtil.createStudyAgentCSV(String.valueOf(iq.getLink()), list, false);
                    } else {
                        listOfIdNodes = studyAgentUtil.getStudyAgentCSV(String.valueOf(iq.getLink()));
                    }

                    if (listOfIdNodes.length < 1) {
                        emptyLinkFoundDoNotQueue(iq);
                        return false;
                    } else {
                        return isQuestionIdStudyAgent(iqQueue, listOfIdNodes);
                    }
                }
            } catch (Exception e) {
                log.error("Error on saveInterviewLinkAndQueueQuestions ", e);
                return false;
            }
        }
        return true;
    }

    private boolean isQuestionIdStudyAgent(InterviewQuestion iqQueue, String[] listOfIdNodes) {
        boolean isExist = studyAgentUtil.doesIdNodeExistInArray(listOfIdNodes, String.valueOf(iqQueue.getQuestionId()));
        if (isExist) {
            log.info("Question id " + iqQueue.getQuestionId() + " exist so saving it in queue question.");
        } else {
//            iqQueue.setDeleted(1);
            log.info("Question id " + iqQueue.getQuestionId() + " does not exist so saving it in queue question as deleted.");
        }
        return isExist;
    }

    private void emptyLinkFoundDoNotQueue(InterviewQuestion iq) {
        iq.setDeleted(1);
        sessionFactory.getCurrentSession().saveOrUpdate(iq);
    }

    private void loopChildStudyAgentAndQueue(InterviewQuestion iq, int intQuestionSequence,
                                             List<QuestionVO> queueQuestions) {
        Collections.sort(queueQuestions);
        for (QuestionVO question : queueQuestions) {
            InterviewQuestion iqQueue = new InterviewQuestion();
            iqQueue.setIdInterview(iq.getIdInterview());
            iqQueue.setName(question.getName());
            iqQueue.setDescription(question.getDescription());
            iqQueue.setNodeClass(question.getNodeclass());
            iqQueue.setNumber(question.getNumber());
            iqQueue.setModCount(iq.getModCount());
            iqQueue.setLink(question.getLink());
            iqQueue.setType(question.getType());
            iqQueue.setParentModuleId(question.getTopNodeId());
            iqQueue.setQuestionId(question.getIdNode());
            iqQueue.setTopNodeId(question.getTopNodeId());
            iqQueue.setIntQuestionSequence(++intQuestionSequence);
            iqQueue.setDeleted(0);
            sessionFactory.getCurrentSession().saveOrUpdate(iqQueue);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> getAll() {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        return crit.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> getAllActive() {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class).add(Restrictions.eq("deleted", 0));
        return crit.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> getAllDeleted() {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class).add(Restrictions.eq("deleted", 1));
        return crit.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> getAllDeleted(Long idInterview) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        crit.add(Restrictions.eq("deleted", 1));
        crit.add(Restrictions.eq("idInterview", idInterview));
        return crit.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> getAllChildInterviewQuestions(Long idAnswer, Long idInterview) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        crit.add(Restrictions.eq("idInterview", idInterview));
        crit.add(Restrictions.eq("parentAnswerId", idAnswer));
        session.clear();
        List<InterviewQuestion> iqs = crit.list();

        return iqs;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<InterviewQuestion> findByInterviewId(Long interviewId) {
    	final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<InterviewQuestion> criteria = builder.createQuery(InterviewQuestion.class);
        Root<InterviewQuestion> root = criteria.from(InterviewQuestion.class);
        criteria.select(root);
        if (interviewId != null) {         
          criteria.where(builder.and(builder.equal(root.get(InterviewQuestion_.ID_INTERVIEW), interviewId), builder.equal(root.get(InterviewQuestion_.DELETED), 0)));

        }
        return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public InterviewQuestion findIntQuestion(long idInterview, long questionId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        crit.add(Restrictions.eq("idInterview", idInterview));
        crit.add(Restrictions.eq("questionId", questionId));
        List<InterviewQuestion> list = crit.list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Long getMaxIntQuestionSequence(long idInterview) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class)
                .add(Restrictions.eq("idInterview", idInterview)).addOrder(Order.desc("intQuestionSequence"))
                .setMaxResults(1).setProjection(Projections.projectionList()
                        .add(Projections.property("intQuestionSequence"), "intQuestionSequence"));
        return (Long) crit.uniqueResult();
    }

    @Override
    public Long getUniqueInterviewQuestionCount(String[] filterModule) {

        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class).add(Restrictions.eq("deleted", 0))
                .add(Restrictions.in("topNodeId", CommonUtil.convertToLongList(filterModule)))
                .add(Restrictions.gt("questionId", 0L)).setProjection(Projections.countDistinct("idInterview"));

        return (Long) crit.uniqueResult();
    }

    @Override
    public InterviewQuestion getByQuestionId(Long questionId, Long interviewId) {

        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class)
                .add(Restrictions.eq("questionId", questionId)).add(Restrictions.eq("idInterview", interviewId));

        return (InterviewQuestion) crit.uniqueResult();
    }

    @Override
    public List<InterviewQuestion> getQuestionsByNodeId(Long questionId) {

        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        crit.add(Restrictions.eq("questionId", questionId));
        List<InterviewQuestion> list = crit.list();
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public List<InterviewQuestion> getInterviewQuestionsByNodeIdAndIntId(Long questionId, Long idInterview) {

        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        crit.add(Restrictions.eq("questionId", questionId));
        crit.add(Restrictions.eq("idInterview", idInterview));
        List<InterviewQuestion> list = crit.list();
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public InterviewAnswer getInterviewAnswerByAnsIdAndIntId(Long answerId, Long idInterview) {

        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewAnswer.class);
        crit.add(Restrictions.eq("answerId", answerId));
        crit.add(Restrictions.eq("idInterview", idInterview));
        List<InterviewAnswer> list = crit.list();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Long getIntroModuleId(Long interviewId) {

        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class)
                .add(Restrictions.eq("idInterview", interviewId)).add(Restrictions.eq("nodeClass", "M"))
                .setMaxResults(1).setProjection(Projections.projectionList().add(Projections.property("link"), "link"));

        return (Long) crit.uniqueResult();
    }

    @Override
    public Long checkFragmentProcessed(long link, long id) {

        final Session session = sessionFactory.getCurrentSession();
        Query sqlQuery = session.createSQLQuery(PROCESSED_FRAGMENT);
        sqlQuery.setParameter("id", id);
        sqlQuery.setParameter("link", link);

        Object result = sqlQuery.uniqueResult();

        return (result != null) ? ((BigInteger) result).longValue() : 0l;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAll() {
        sessionFactory.getCurrentSession().createSQLQuery("truncate table Interview_Question").executeUpdate();
    }

    @Override
    public Boolean checkIfStudyAgentPreLoaded() {
        SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
        if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
            SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);

            if (introModule == null) {
                log.error("no intro module set");
                return false;
            } else {
                Node node = moduleDao.getNodeById(Long.valueOf(introModule.getValue()));
                generateLinks(node);

                Boolean preLoaded = isPreLoaded(introModule);

                links = new ArrayList<>();
                return preLoaded;
            }
        }
        return true;
    }

    private Boolean isPreLoaded(SystemPropertyVO introModule) {
        try {
            for (Long link : links) {
                if (!introModule.getValue().equals(String.valueOf(link))) {
                    if (!studyAgentUtil.doesStudyAgentCSVExist(String.valueOf(link))) {
                        Node node = moduleDao.getNodeById(link);
                        List<PossibleAnswer> possibleAnswers = systemPropertyDao.getPosAnsWithStudyAgentsByIdMod(node.getIdNode());
                        if (!possibleAnswers.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error while reading file", e);
        }
        return true;
    }

    public List<InterviewQuestion> findByLinkId(long idNode) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<InterviewQuestion> criteria = builder.createQuery(InterviewQuestion.class);
        Root<InterviewQuestion> root = criteria.from(InterviewQuestion.class);
        criteria.select(root);
        criteria.where(builder.and(
                builder.equal(root.get(InterviewQuestion_.LINK), idNode)
        ));
        List<InterviewQuestion> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        return resultList;
    }
}
