package org.occideas.fragment.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.entity.Fragment;
import org.occideas.vo.FragmentCopyVO;
import org.occideas.vo.FragmentReportVO;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.QuestionVO;

public interface FragmentService extends BaseService<FragmentVO> {
	
	void createFragment(FragmentVO fragmentVO);

	void merge(FragmentVO json);

	boolean checkExists(Long id);

	List<FragmentVO> findByIdForInterview(Long id);

	NodeRuleHolder copyFragment(FragmentCopyVO vo, FragmentReportVO report);

	NodeRuleHolder deepCopyFragment(FragmentCopyVO vo, 
			   FragmentReportVO report,
			   Long parentIdNode,Long topNodeId);

	List<QuestionVO> getLinkingNodes(Long id);

	List<FragmentVO> getFilterStudyAgents(Long id);

	List<FragmentVO> getFilterStudyAgents(Long id, Long idAgent);

	List<Fragment> getAllFragments();

	List<FragmentVO> getFragmentParents(Long id);
}
