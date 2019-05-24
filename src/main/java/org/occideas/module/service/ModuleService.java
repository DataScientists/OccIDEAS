package org.occideas.module.service;

import java.util.List;

import org.occideas.entity.Module;
import org.occideas.entity.PossibleAnswer;
import org.occideas.vo.LanguageModBreakdownVO;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.ModuleReportVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.NodeVO;

public interface ModuleService{
	public void merge(ModuleVO module);
	public Long getMaxId();
	List<ModuleVO> findByIdForInterview(Long id);
	List<ModuleVO> findByIdNoRules(Long id);
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
	public NodeVO getModuleFilterStudyAgent(Long id);
	public NodeVO getModuleFilterAgent(Long id, Long idAgent);
	public List<Module> getAllModules();
	public NodeVO getNodeNameById(Long idNode);
	public List<PossibleAnswer> getPosAnsWithStudyAgentsByIdMod(Long theId);
    public ModuleVO getStudyAgentJSON(Long id);
    Integer getModuleTranslationTotalCount(String idNode);
    Integer getModuleTranslationCurrentCount(String idNode, Long languageId);
    List<LanguageModBreakdownVO> getModuleLanguageBreakdown(Long languageId);
    Integer getTotalUntranslatedModule(Long languageId);
    public Integer getTotalTranslatedNodeByLanguage(long languageId);
    public Integer getModulesWithTranslationCount(long languageId);
    List<String> getFilterStudyAgent(Long id);
    public long update(ModuleVO vo);
    public long save(ModuleVO module);
    ModuleVO create(ModuleVO module);
    void delete(ModuleVO module);
    List<ModuleVO> findById(Long id);
    List<ModuleVO> listAll();
}
