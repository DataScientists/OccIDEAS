package org.occideas.module.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.NodeVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.ModuleReportVO;

public interface ModuleService extends BaseService<ModuleVO>{
	public void merge(ModuleVO module);
	public Long getMaxId();
	List<ModuleVO> findByIdForInterview(Long id);
	public NodeRuleHolder copyModule(ModuleCopyVO json);
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO json,ModuleReportVO report);
	public void copyRules(NodeRuleHolder idNodeHolder);
	public NodeRuleHolder copyModule(ModuleCopyVO vo,ModuleReportVO report);
	void addNodeRules(NodeRuleHolder idNodeHolder);
	public ModuleReportVO copyRulesValidateAgent(NodeRuleHolder idNodeHolder,ModuleReportVO reportVO);
	public void addNodeRulesValidateAgent(NodeRuleHolder idNodeHolder, ModuleReportVO report);
	public void updateMissingLinks(NodeVO nodeVO);
	public NodeRuleHolder copyModuleAutoGenerateModule(ModuleCopyVO copyVo, ModuleReportVO report);
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo, ModuleReportVO report,
			NodeRuleHolder idNodeRuleHolder);
	public void setActiveIntroModule(ModuleVO vo);
	public ModuleVO getModuleFilterStudyAgent(Long id);
}
