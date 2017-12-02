package org.occideas.question.dao;

import java.util.List;

import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.NodesAgent;
import org.occideas.entity.Question;

public interface IQuestionDao
{

    public Question getQuestionByModuleIdAndNumber(String parentId,String number);
    public List<Question> getQuestionsByParentId(String parentId);
    public List<Question> getAllMultipleQuestions();
    public Module getModuleByParentId(Long idNode) ;
    public Question findMultipleQuestion(long questionId);
    public Node getTopModuleByTopNodeId(long topNodeId);
    public List<NodesAgent> getNodesWithAgent(long agentId);
    public Question get(Class<Question> class1, Long valueOf);

}
