package org.occideas.question.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.NodesAgent;
import org.occideas.entity.Question;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;

import java.util.List;

public interface QuestionService extends BaseService<QuestionVO> {

  QuestionVO determineNextQuestionByCurrentNumber(String moduleId, String nodeNumber);

  ModuleVO getQuestionWithParentId(Long idNode);

  List<QuestionVO> getQuestionsWithSingleChildLevel(Long Id);

  List<QuestionVO> getQuestionsWithParentId(String parentId);

  List<QuestionVO> getAllMultipleQuestions();

  void updateWithIndependentTransaction(QuestionVO o);

  Question findMultipleQuestion(long questionId);

  NodeVO getTopModuleByTopNodeId(long topNodeId);

  List<NodesAgent> getNodesWithAgent(long agentId);

  QuestionVO findByIdExcludeChildren(long idNode);

  QuestionVO getQuestionByLinkIdAndTopId(long idNode, long idNode1);
}
