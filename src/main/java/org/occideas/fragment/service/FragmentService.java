package org.occideas.fragment.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.Fragment;
import org.occideas.vo.*;

import java.util.List;

public interface FragmentService extends BaseService<FragmentVO> {

  void createFragment(FragmentVO fragmentVO);

  void merge(FragmentVO json);

  boolean checkExists(Long id);

  List<FragmentVO> findByIdForInterview(Long id);

  NodeRuleHolder copyFragment(FragmentCopyVO vo, FragmentReportVO report);

  NodeRuleHolder deepCopyFragment(FragmentCopyVO vo,
                                  FragmentReportVO report,
                                  Long parentIdNode, Long topNodeId);

  List<QuestionVO> getLinkingNodes(Long id);

  List<FragmentVO> getFilterStudyAgents(Long id);

  List<FragmentVO> getFilterStudyAgents(Long id, Long idAgent);

  List<Fragment> getAllFragments();

  List<FragmentVO> getFragmentParents(Long id);

  Integer getFragmentTranslationTotalCount(String idNode);

  Integer getFragmentTranslationCurrentCount(String idNode, Long languageId);

  List<LanguageFragmentBreakdownVO> getFragmentLanguageBreakdown(Long languageId);

  Integer getTotalUntranslatedFragment(Long languageId);

  Integer getFragmentWithTranslationCount(long languageId);

  Integer getTotalTranslatedNodeByLanguage(long languageId);

  FragmentVO getModuleByNameLength(String moduleKey, int i);
}
