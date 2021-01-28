package org.occideas.question.dao;

import org.occideas.entity.JobModule;
import org.occideas.entity.Node;
import org.occideas.entity.NodesAgent;
import org.occideas.entity.Question;

import java.util.List;

public interface IQuestionDao {

  Question getQuestionByModuleIdAndNumber(String parentId, String number);

  List<Question> getQuestionsByParentId(String parentId);

  List<Question> getAllMultipleQuestions();

  JobModule getModuleByParentId(Long idNode);

  Question findMultipleQuestion(long questionId);

  Node getTopModuleByTopNodeId(long topNodeId);

  List<NodesAgent> getNodesWithAgent(long agentId);

  Question get(Class<Question> class1, Long valueOf);

  void saveOrUpdate(Question question);

  Question get(long id);

  void saveOrUpdateIgnoreFK(Question question);

  Question getQuestionByLinkIdAndTopId(long linkId, long topId);

  Question getQuestionByTopIdAndNumber(Long topNodeId, String number);

  Question getNearestQuestionByLinkIdAndTopId(long linkId, long topId);
}
