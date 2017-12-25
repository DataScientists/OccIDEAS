package org.occideas.node.service;

import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;

public interface INodeService
{

    ModuleVO getModule(Long idNode);

    QuestionVO getQuestion(Long idNode);
    
    NodeVO getNode(Long idNode);
}
