package org.occideas.base.service;

import java.util.List;

import org.occideas.vo.BaseReportVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;

public interface IQuestionCopier {

	void populateQuestionsWithIdNode(Long idNode, List<QuestionVO> childNodes,
			NodeRuleHolder idNodeRuleHolder);
	
	void populateQuestionsWithIdNode(Long idNode, List<QuestionVO> childNodes,
			NodeRuleHolder idNodeRuleHolder,BaseReportVO reportVO);
	
	void populateQuestionsIncludeLinksWithIdNode(Long idNode, List<QuestionVO> childNodes,
			NodeRuleHolder idNodeRuleHolder,BaseReportVO report);
	
	void buildFragment(List<? extends NodeVO> childNodes, QuestionVO vo,
			NodeRuleHolder idNodeRuleHolder);
}
