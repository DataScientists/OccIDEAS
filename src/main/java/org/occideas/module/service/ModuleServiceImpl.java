package org.occideas.module.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.agent.dao.IAgentDao;
import org.occideas.base.service.IQuestionCopier;
import org.occideas.config.QualtricsConfig;
import org.occideas.entity.*;
import org.occideas.fragment.dao.IFragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.NodeRuleMapper;
import org.occideas.mapper.RuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.nodelanguage.dao.INodeLanguageDao;
import org.occideas.noderule.dao.INodeRuleDao;
import org.occideas.qsf.service.QSFConversionService;
import org.occideas.question.service.QuestionService;
import org.occideas.reporthistory.service.ReportHistoryService;
import org.occideas.rule.dao.IRuleDao;
import org.occideas.security.handler.TokenManager;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.CommonUtil;
import org.occideas.utilities.ReportsEnum;
import org.occideas.utilities.ReportsStatusEnum;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private IModuleDao dao;
	@Autowired
	private FragmentMapper fragmentMapper;
	@Autowired
	private IFragmentDao fragmentDao;
	@Autowired
	private ModuleMapper mapper;
	@Autowired
	private IRuleDao ruleDao;
	@Autowired
	private RuleMapper ruleMapper;
	@Autowired
	private INodeRuleDao nodeRuleDao;
	@Autowired
	private NodeRuleMapper nodeRuleMapper;
	@Autowired
	private IAgentDao agentDao;
	@Autowired
	private IQuestionCopier questionCopier;
	@Autowired
	@Lazy
	private QuestionService questionService;
	@Autowired
	@Lazy
	private SystemPropertyService sysPropService;
	@Autowired
	private SystemPropertyDao sysPropDao;
	@Autowired
	private StudyAgentUtil studyAgentUtil;
	@Autowired
	private INodeLanguageDao nodeLanguageDao;
	@Autowired
	private ReportHistoryService reportHistoryService;
	@Autowired
	private QSFConversionService qsfConversionService;
	@Autowired
	private QualtricsConfig qualtricsConfig;

	@Override
	public List<ModuleVO> listAll() {
		return mapper.convertToModuleVOList(dao.getAllActive(), false);
	}

	@Override
	public List<ModuleVO> findById(Long id) {
		JobModule module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module, true);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public List<ModuleVO> findByIdForInterview(Long id) {
		JobModule module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module, false);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public List<ModuleVO> findByIdNoRules(Long id) {
		JobModule module = dao.get(id);
		boolean includeChildNodes = true;
		boolean includeRules = false;
		ModuleVO moduleVO = mapper.convertToModuleWithFlagsVO(module, includeChildNodes, includeRules);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public ModuleVO create(ModuleVO module) {
		JobModule moduleEntity = dao.save(mapper.convertToModule(module, false));
		studyAgentUtil.createStudyAgentForUpdatedNode(moduleEntity.getIdNode(), moduleEntity.getName());
		return mapper.convertToModuleVO(moduleEntity, false);
	}

	public long update(ModuleVO module) {
		// if (module.getIdNode() != 0) {
		dao.saveOrUpdate(mapper.convertToModule(module, true));
		studyAgentUtil.createStudyAgentForUpdatedNode(module.getIdNode(), module.getName());
		return module.getIdNode();
		// } else {
		// return dao.create(mapper.convertToModule(module, true));
		// }
	}

	public long save(ModuleVO module) {
		return dao.create(mapper.convertToModule(module, true));
	}

	@Override
	public void delete(ModuleVO module) {
		dao.delete(mapper.convertToModule(module, false));
		studyAgentUtil.deleteStudyAgentJson(String.valueOf(module.getIdNode()));
	}

	@Override
	public void merge(ModuleVO module) {
		dao.merge(mapper.convertToModule(module, true));
		studyAgentUtil.deleteStudyAgentJson(String.valueOf(module.getIdNode()));
	}

	@Override
	public Long getMaxId() {
		return dao.generateIdNode();
	}

	@Override
	public NodeRuleHolder copyModule(ModuleCopyVO vo) {
		ModuleVO copyVO = vo.getVo();
		long idNode = dao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		return idNodeRuleHolder;
	}

	@Override
	public NodeRuleHolder copyModule(ModuleCopyVO vo, ModuleReportVO report) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder, report);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		return idNodeRuleHolder;
	}

	private NodeRuleHolder createNodeRuleHolder(Long idNode) {
		NodeRuleHolder idNodeRuleHolder = new NodeRuleHolder();
		long maxRuleId = ruleDao.getMaxRuleId();
		idNodeRuleHolder.setLastIdRule(maxRuleId);
		idNodeRuleHolder.setFirstIdRuleGenerated(maxRuleId);
		idNodeRuleHolder.setIdNode(idNode);
		idNodeRuleHolder.setTopNodeId(idNode);
		return idNodeRuleHolder;
	}

	@Override
	public void copyRules(NodeRuleHolder idNodeHolder) {
		for (RuleVO ruleVO : idNodeHolder.getRuleList()) {
			ruleDao.save(ruleMapper.convertToRule(ruleVO));
		}
	}

	@Override
	public ModuleReportVO copyRulesValidateAgent(NodeRuleHolder idNodeHolder, ModuleReportVO reportVO) {
		List<AgentVO> missingAgentList = new ArrayList<>();
		List<RuleVO> missingRuleList = new ArrayList<>();
		for (RuleVO ruleVO : idNodeHolder.getRuleList()) {
			Agent agent = agentDao.get(ruleVO.getAgent().getIdAgent());
			if (agent != null) {
				ruleDao.save(ruleMapper.convertToRule(ruleVO));
			} else {
				missingAgentList.add(ruleVO.getAgent());
				missingRuleList.add(ruleVO);
			}
		}
		reportVO.setMissingAgentsList(missingAgentList);
		return reportVO;
	}

	@Override
	public void addNodeRules(NodeRuleHolder idNodeHolder) {
		List<NodeRuleVO> list = idNodeHolder.getNodeRuleList();
		for (NodeRuleVO vo : list) {
			nodeRuleDao.save(nodeRuleMapper.convertToNodeRule(vo));
		}
	}

	@Override
	public void addNodeRulesValidateAgent(NodeRuleHolder idNodeHolder, ModuleReportVO report) {
		List<NodeRuleVO> list = idNodeHolder.getNodeRuleList();
		for (NodeRuleVO vo : list) {
			boolean isRuleMissing = false;
			for (RuleVO ruleVO : report.getMissingRuleAgentList()) {
				if (vo.getIdRule() == ruleVO.getIdRule()) {
					isRuleMissing = true;
					break;
				}
			}
			if (!isRuleMissing) {
				nodeRuleDao.save(nodeRuleMapper.convertToNodeRule(vo));
			}
		}
	}

	@Override
	public NodeRuleHolder copyModuleAutoGenerateModule(ModuleCopyVO copyVo, ModuleReportVO report) {
		ModuleVO vo = copyVo.getVo();
		Long idNode = dao.generateIdNode() + 1;
		vo.setIdNode(idNode);
		vo.setName(copyVo.getName());
		vo.setAnchorId(idNode);
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNode, vo.getChildNodes(), idNodeRuleHolder, report);
		createMissingModule(vo, copyVo, idNodeRuleHolder, report);
		dao.saveCopy(mapper.convertToModule(vo, true));
		return idNodeRuleHolder;
	}

	@Override
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo, ModuleReportVO report) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		copyVO.setAnchorId(idNode);
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder,
				report);
		createMissingFragments(copyVO, idNodeRuleHolder, report);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		return idNodeRuleHolder;
	}

	@Override
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo, ModuleReportVO report,
			NodeRuleHolder idNodeRuleHolder) {
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNodeRuleHolder.getLastIdNode(),
				vo.getVo().getChildNodes(), idNodeRuleHolder, report);
		createMissingFragments(vo.getVo(), idNodeRuleHolder, report);
		dao.saveCopy(mapper.convertToModule(vo.getVo(), true));
		return idNodeRuleHolder;
	}

	private void createMissingFragments(ModuleVO copyVO, NodeRuleHolder idNodeRuleHolder, ModuleReportVO report) {
		List<FragmentVO> fragments = copyVO.getFragments();
		for (FragmentVO vo : fragments) {
			// check if fragment exist
			List<Fragment> list = fragmentDao.findByName(vo.getName());
			if (list.isEmpty()) {
				// no fragment lets create one
				generateIdNodeForFragments(vo, idNodeRuleHolder);
				fragmentDao.saveOrUpdate(fragmentMapper.convertToFragment(vo, true));
				report.getFragmentVODecoratorList().add(new FragmentVODecorator(vo, true));
			} else if (list.size() > 1) {
				// found name more than one add warning
				log.error("Error on creating missing fragments, "
						+ "found more than one possible match for fragment name " + vo.getName());
			} else {
				// use existing fragment
				report.getFragmentVODecoratorList().add(new FragmentVODecorator(vo, false));
			}
		}

	}

	public void createMissingModule(ModuleVO moduleVO, ModuleCopyVO copyVO, NodeRuleHolder idNodeRuleHolder,
			ModuleReportVO report) {
		List<ModuleVO> modules = moduleVO.getModules();
		for (ModuleVO vo : modules) {
			// check if module exist
			List<JobModule> list = dao.findByName(vo.getName());
			if (list.isEmpty()) {
				ModuleVO newModuleVO = vo;
				Long idNode = idNodeRuleHolder.getLastIdNode() + 1;
				idNodeRuleHolder.setLastIdNode(idNode);
				idNodeRuleHolder.setTopNodeId(idNode);
				newModuleVO.setIdNode(idNode);
				newModuleVO.setAnchorId(idNode);
				ModuleCopyVO newModuleCopyVO = new ModuleCopyVO();
				newModuleCopyVO.setVo(newModuleVO);
				newModuleCopyVO.setFragments(vo.getFragments());
				newModuleCopyVO.setIncludeRules(true);
				newModuleCopyVO.setName(vo.getName());
				newModuleVO.setName(newModuleCopyVO.getName());
				report.getModuleVODecoratorList().add(new ModuleVODecorator(newModuleVO, true));
				copyModuleAutoGenerateFragments(newModuleCopyVO, report, idNodeRuleHolder);
				// fragmentDao.saveOrUpdate(fragmentMapper.convertToFragment(vo,
				// true));
			} else if (list.size() > 1) {
				// found name more than one add warning
				log.error("Error on creating missing module, " + "found more than one possible match for module name "
						+ vo.getName());
			} else {
				// use existing module
				report.getModuleVODecoratorList().add(new ModuleVODecorator(vo, false));
			}
		}

	}

	private void generateIdNodeForFragments(FragmentVO vo, NodeRuleHolder idNodeRuleHolder) {
		Long idNode = idNodeRuleHolder.getLastIdNode() + 1;
		idNodeRuleHolder.setLastIdNode(idNode);
		idNodeRuleHolder.setTopNodeId(idNode);
		vo.setIdNode(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, vo.getChildNodes(), idNodeRuleHolder);
	}

	@Override
	public void updateMissingLinks(NodeVO nodeVO) {
		List<QuestionVO> qVoList = new ArrayList<>();

		ModuleVO moduleVO = new ModuleVO();
		if (nodeVO instanceof ModuleVO) {
			moduleVO = (ModuleVO) nodeVO;
			qVoList = moduleVO.getChildNodes();
		}
		FragmentVO fragmentVO = new FragmentVO();
		if (nodeVO instanceof FragmentVO) {
			fragmentVO = (FragmentVO) nodeVO;
			qVoList = fragmentVO.getChildNodes();
		}
		PossibleAnswerVO answerVO = new PossibleAnswerVO();
		if (nodeVO instanceof PossibleAnswerVO) {
			answerVO = (PossibleAnswerVO) nodeVO;
			qVoList = answerVO.getChildNodes();
		}

		// lets check our module questions
		for (QuestionVO qVO : qVoList) {
			if ("Q_linkedajsm".equals(qVO.getType())) {
				List<FragmentVO> list = fragmentMapper.convertToFragmentVOList(fragmentDao.findByName(qVO.getName()),
						true);
				if (!list.isEmpty()) {
					FragmentVO fragment = list.get(0);
					qVO.setLink(fragment.getIdNode());
					questionService.updateWithIndependentTransaction(qVO);
				}
			}
			if ("Q_linkedmodule".equals(qVO.getType())) {
				List<ModuleVO> list = mapper.convertToModuleVOList(dao.findByName(qVO.getName()), true);
				if (!list.isEmpty()) {
					ModuleVO module = list.get(0);
					qVO.setLink(module.getIdNode());
					questionService.updateWithIndependentTransaction(qVO);
				}
			}
			if (!qVO.getChildNodes().isEmpty()) {
				for (PossibleAnswerVO ansVo : qVO.getChildNodes()) {
					updateMissingLinks(ansVo);
				}
			}
		}
	}

	@Override
	public void setActiveIntroModule(ModuleVO vo) {
		SystemPropertyVO activeIntro = sysPropService.getByName(Constant.STUDY_INTRO);
		Long id = null;
		if (activeIntro != null) {
			id = activeIntro.getId();
		}
		SystemPropertyVO sysPropVO = new SystemPropertyVO();
		if (id != null) {
			sysPropVO.setId(id);
		}
		sysPropVO.setName(Constant.STUDY_INTRO);
		sysPropVO.setType("config");
		sysPropVO.setValue(String.valueOf(vo.getIdNode()));
		sysPropVO.setUpdatedBy(new TokenManager().extractUserFromToken());
		sysPropService.save(sysPropVO);
	}

	@Override
	public NodeVO getModuleFilterStudyAgent(Long id) {
		Node node = dao.getNodeById(id);
		if ("M".equals(node.getNodeclass())) {
			ModuleVO moduleVO = mapper.convertToModuleVO((JobModule) node, true);
			ModuleVO newModuleVO = sysPropService.filterModulesNodesWithStudyAgents(moduleVO);
			return newModuleVO;
		} else if ("F".equals(node.getNodeclass())) {
			FragmentVO fragmentVO = fragmentMapper.convertToFragmentVO((Fragment) node, true);
			FragmentVO newFragmentVO = sysPropService.filterFragmentNodesWithStudyAgents(fragmentVO);
			return newFragmentVO;
		}
		return null;
	}

	@Override
	public List<String> getFilterStudyAgent(Long id) {
		System.out.println("Node Id for preloading study agent is: " + id);
		Node node = dao.getNodeById(id);
		// System.out.println("Node is: " + node);
		NodeVO vo = convertToModuleOrFragment(node);
		// System.out.println("NodeVO is: " + vo);
		return sysPropService.filterNodesWithStudyAgents(vo);
	}

	private NodeVO convertToModuleOrFragment(Node node) {
		NodeVO vo = null;
		if ("M".equals(node.getNodeclass())) {
			vo = mapper.convertToModuleVO((JobModule) node, true);
		} else if ("F".equals(node.getNodeclass())) {
			vo = fragmentMapper.convertToFragmentVO((Fragment) node, true);
		}
		return vo;
	}

	@Override
	public NodeVO getModuleFilterAgent(Long id, Long idAgent) {
		Node node = dao.getNodeById(id);
		if ("M".equals(node.getNodeclass())) {
			ModuleVO moduleVO = mapper.convertToModuleVO((JobModule) node, true);
			ModuleVO newModuleVO = sysPropService.filterModulesNodesWithAgents(moduleVO, idAgent);
			return newModuleVO;
		} else if ("F".equals(node.getNodeclass())) {
			FragmentVO fragmentVO = fragmentMapper.convertToFragmentVO((Fragment) node, true);
			FragmentVO newFragmentVO = sysPropService.filterFragmentNodesWithAgents(fragmentVO, idAgent);
			return newFragmentVO;
		}
		return null;
	}

	@Override
	public List<JobModule> getAllModules() {
		return dao.getAll(true);
	}

	@Override
	public NodeVO getNodeById(Long idNode) {
		Node node = dao.getNodeById(idNode);
		if ("M".equals(node.getNodeclass())) {
			ModuleVO moduleVO = mapper.convertToModuleVO((JobModule) node, false);
			return moduleVO;
		} else if ("F".equals(node.getNodeclass())) {
			FragmentVO fragmentVO = fragmentMapper.convertToFragmentVO((Fragment) node, false);
			return fragmentVO;
		}
		return null;
	}

	@Override
	public List<PossibleAnswer> getPosAnsWithStudyAgentsByIdMod(Long theId) {
		// dao.getPosAnsWithStudyAgentsByIdMod(theId);
		return sysPropDao.getPosAnsWithStudyAgentsByIdMod(theId);
	}

	@Override
	public ModuleVO getStudyAgentJSON(Long id) {
		try {
			return studyAgentUtil.getStudyAgentJson(String.valueOf(id));
		} catch (JsonGenerationException e) {
			log.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	@Async
	public File convertToApplicationQSF(Long id, String user) {
		List<ModuleVO> modules = this.findById(id);
		if (!modules.isEmpty()) {
			try {
				File file = studyAgentUtil.moduleToApplicationQSF(modules.get(0));
				ReportHistoryVO reportHistoryVO = insertToReportHistorySuccess(file.getName(), file.getAbsolutePath(),
						ReportsEnum.QSF_EXPORT.getValue(),user);
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ModuleVO getModuleByNameLength(String name, int length) {
		Optional<String> moduleName = Optional.ofNullable(name);
		if(moduleName.isPresent()){
			String truncatedName = moduleName.get().substring(0,length);
			List<JobModule> modules = dao.findByNameLength(name);
			if(!modules.isEmpty() && modules.size() == 1){
				return mapper.convertToModuleVO(modules.get(0),false);
			}
			else if(!modules.isEmpty() && modules.size() > 1){
				log.error("Cleanup database their are 2 modules with same {} length for {}", length, truncatedName);
			}
		}
		return null;
	}

	@Override
	@Async("threadPoolTaskExecutor")
	public void manualBuildQSF(Long id, boolean filter) {
		JobModule jobModule = dao.get(id);
		if (Objects.nonNull(jobModule)) {
			if (qualtricsConfig.isExpandModules()) {
				List<String> filterIdNodes = new ArrayList<>();
				if (filter) {
					filterIdNodes = getFilterStudyAgent(id);
				}
				qsfConversionService.uploadQSF(jobModule, filterIdNodes);
			} else {
				studyAgentUtil.buildQSF(mapper.convertToModuleVO(jobModule, true), filter, false);
			}
		}
	}

	private ReportHistoryVO insertToReportHistorySuccess(String name, String fullPath,
														  String type, String user) {

		ReportHistoryVO reportHistoryVO = new ReportHistoryVO();
		reportHistoryVO.setName(name);
		reportHistoryVO.setPath(fullPath);
		reportHistoryVO.setRequestor(user);
		reportHistoryVO.setStatus(ReportsStatusEnum.COMPLETED.getValue());
		reportHistoryVO.setType(type);
		reportHistoryVO.setUpdatedBy(user);
		reportHistoryVO.setProgress("100%");
		return reportHistoryService.save(reportHistoryVO);
	}

	private ReportHistoryVO insertToReportHistoryFailed(String name, String fullPath,
														 String type, String user) {

		ReportHistoryVO reportHistoryVO = new ReportHistoryVO();
		reportHistoryVO.setName(name);
		reportHistoryVO.setPath(fullPath);
		reportHistoryVO.setRequestor(user);
		reportHistoryVO.setStatus(ReportsStatusEnum.FAILED.getValue());
		reportHistoryVO.setType(type);
		reportHistoryVO.setUpdatedBy(user);
		reportHistoryVO.setProgress("0%");
		return reportHistoryService.save(reportHistoryVO);
	}

	private StreamingOutput getOut(final byte[] excelBytes, String pathStr) {
		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
				try {
					java.nio.file.Path path = Paths.get(pathStr);
					byte[] data = Files.readAllBytes(path);
					output.write(data);
					output.flush();
				} catch (Exception e) {
					throw new WebApplicationException();
				}
			}
		};
		return fileStream;
	}

	@Override
	public Integer getModuleTranslationTotalCount(String idNode) {
		final List<? extends Node> nodeList = dao.getDistinctNodeNameByIdNode(idNode);
		if (nodeList.size() <= 1) {
			return nodeList.size();
		}
		CommonUtil.removeNonUniqueNames(nodeList);
		return nodeList.size();
	}

	@Override
	public Integer getModuleTranslationCurrentCount(String idNode, Long languageId) {
		List<String> nodeList = dao.getNodeNameByIdNode(idNode);
		CommonUtil.removeNonUniqueString(nodeList);
		CommonUtil.replaceListWithLowerCaseAndTrim(nodeList);
		List<String> nodeLanguageList = nodeLanguageDao.getNodeLanguageWordsByIdOrderByWord(languageId);
		CommonUtil.replaceListWithLowerCaseAndTrim(nodeLanguageList);
		int count = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeLanguageList.contains(nodeList.get(i).toLowerCase().trim())) {
				count++;
			}
		}
		return count;
	}

	private boolean hasAnyNodeTranslated(String idNode, Long languageId, List<String> nodeLanguageList) {
		List<String> nodeList = dao.getNodeNameByIdNode(idNode);
		CommonUtil.removeNonUniqueString(nodeList);
		CommonUtil.replaceListWithLowerCaseAndTrim(nodeList);
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeLanguageList.contains(nodeList.get(i).toLowerCase().trim())) {
				return true;
			}
		}
		return false;
	}

	private Integer countUniqueNodeNamesInModule(String idNode) {
		List<String> nodeList = dao.getNodeNameByIdNode(idNode);
		CommonUtil.removeNonUniqueString(nodeList);
		return nodeList.size();
	}

	@Override
	public List<LanguageModBreakdownVO> getModuleLanguageBreakdown(Long languageId) {
		List<LanguageModBreakdownVO> results = new ArrayList<>();

		List<JobModule> listOfModuleIdNodes = nodeLanguageDao.getModulesIdNodeSQL();
		for (JobModule module : listOfModuleIdNodes) {
			long idNode = module.getIdNode();
			Integer currentCount = getModuleTranslationCurrentCount(String.valueOf(idNode), languageId);
			Integer totalCount = getModuleTranslationTotalCount(String.valueOf(idNode));

			LanguageModBreakdownVO vo = buildBreakdownStatsForModule(module, idNode, currentCount, totalCount);

			results.add(vo);
		}

		return results;
	}

	@Override
	public Integer getTotalUntranslatedModule(Long languageId) {
		List<JobModule> listOfModuleIdNodes = nodeLanguageDao.getModulesIdNodeSQL();
		int count = 0;
		for (JobModule module : listOfModuleIdNodes) {
			count = count + countUniqueNodeNamesInModule(String.valueOf(module.getIdNode()));
		}
		return count;
	}

	@Override
	public Integer getModulesWithTranslationCount(long languageId) {
		List<JobModule> listOfModuleIdNodes = nodeLanguageDao.getModulesIdNodeSQL();
		int count = 0;
		List<String> nodeLanguageList = nodeLanguageDao.getNodeLanguageWordsByIdOrderByWord(languageId);
		CommonUtil.replaceListWithLowerCaseAndTrim(nodeLanguageList);
		for (JobModule module : listOfModuleIdNodes) {
			if (hasAnyNodeTranslated(String.valueOf(module.getIdNode()), languageId, nodeLanguageList)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public Integer getTotalTranslatedNodeByLanguage(long languageId) {
		List<JobModule> listOfModuleIdNodes = nodeLanguageDao.getModulesIdNodeSQL();
		int count = 0;
		List<String> nodeLanguageList = nodeLanguageDao.getNodeLanguageWordsByIdOrderByWord(languageId);
		CommonUtil.replaceListWithLowerCaseAndTrim(nodeLanguageList);
		for (JobModule module : listOfModuleIdNodes) {
			List<String> nodeList = dao.getNodeNameByIdNode(String.valueOf(module.getIdNode()));
			CommonUtil.removeNonUniqueString(nodeList);
			CommonUtil.replaceListWithLowerCaseAndTrim(nodeList);
			for (int i = 0; i < nodeList.size(); i++) {
				if (nodeLanguageList.contains(nodeList.get(i).toLowerCase().trim())) {
					count++;
				}
			}
		}
		return count;
	}

	private LanguageModBreakdownVO buildBreakdownStatsForModule(JobModule module, long idNode, Integer currentCount,
			Integer totalCount) {
		LanguageModBreakdownVO vo = new LanguageModBreakdownVO();
		vo.setIdNode(idNode);
		vo.setName(module.getName());
		vo.setCurrent(currentCount);
		vo.setTotal(totalCount);
		return vo;
	}

}
