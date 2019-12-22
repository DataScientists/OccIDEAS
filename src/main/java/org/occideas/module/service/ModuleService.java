package org.occideas.module.service;

import org.occideas.entity.Module;
import org.occideas.entity.PossibleAnswer;
import org.occideas.qsf.ApplicationQSF;
import org.occideas.vo.*;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.util.List;

public interface ModuleService {
	void merge(ModuleVO module);

	Long getMaxId();

	List<ModuleVO> findByIdForInterview(Long id);

	List<ModuleVO> findByIdNoRules(Long id);

	NodeRuleHolder copyModule(ModuleCopyVO json);

	NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO json, ModuleReportVO report);

	void copyRules(NodeRuleHolder idNodeHolder);

	NodeRuleHolder copyModule(ModuleCopyVO vo, ModuleReportVO report);

	void addNodeRules(NodeRuleHolder idNodeHolder);

	ModuleReportVO copyRulesValidateAgent(NodeRuleHolder idNodeHolder, ModuleReportVO reportVO);

	void addNodeRulesValidateAgent(NodeRuleHolder idNodeHolder, ModuleReportVO report);

	void updateMissingLinks(NodeVO nodeVO);

	NodeRuleHolder copyModuleAutoGenerateModule(ModuleCopyVO copyVo, ModuleReportVO report);

	NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo, ModuleReportVO report,
			NodeRuleHolder idNodeRuleHolder);

	void setActiveIntroModule(ModuleVO vo);

	NodeVO getModuleFilterStudyAgent(Long id);

	NodeVO getModuleFilterAgent(Long id, Long idAgent);

	List<Module> getAllModules();

	NodeVO getNodeNameById(Long idNode);

	List<PossibleAnswer> getPosAnsWithStudyAgentsByIdMod(Long theId);

	ModuleVO getStudyAgentJSON(Long id);

    @Async
    void manualBuildQSF(Long id, String user);

    Integer getModuleTranslationTotalCount(String idNode);

	Integer getModuleTranslationCurrentCount(String idNode, Long languageId);

	List<LanguageModBreakdownVO> getModuleLanguageBreakdown(Long languageId);

	Integer getTotalUntranslatedModule(Long languageId);

	Integer getTotalTranslatedNodeByLanguage(long languageId);

	Integer getModulesWithTranslationCount(long languageId);

	List<String> getFilterStudyAgent(Long id);

	long update(ModuleVO vo);

	long save(ModuleVO module);

	ModuleVO create(ModuleVO module);

	void delete(ModuleVO module);

	List<ModuleVO> findById(Long id);

	List<ModuleVO> listAll();

	File convertToApplicationQSF(Long id, String user);
}
