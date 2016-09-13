package org.occideas.module.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.agent.dao.AgentDao;
import org.occideas.base.service.IQuestionCopier;
import org.occideas.entity.Agent;
import org.occideas.entity.Fragment;
import org.occideas.entity.Module;
import org.occideas.fragment.dao.FragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.NodeRuleMapper;
import org.occideas.mapper.RuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.noderule.dao.NodeRuleDao;
import org.occideas.question.service.QuestionService;
import org.occideas.rule.dao.RuleDao;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.vo.AgentVO;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.ModuleReportVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.NodeRuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private IModuleDao dao;
	@Autowired
	private FragmentMapper fragmentMapper;
	@Autowired
	private FragmentDao fragmentDao;
	@Autowired
	private ModuleMapper mapper;
	@Autowired
	private RuleDao ruleDao;
	@Autowired
	private RuleMapper ruleMapper;
	@Autowired
	private NodeRuleDao nodeRuleDao;
	@Autowired
	private NodeRuleMapper nodeRuleMapper;
	@Autowired
	private AgentDao agentDao;
	@Autowired
	private IQuestionCopier questionCopier;
	@Autowired
	private QuestionService questionService;

	@Auditable(actionType = AuditingActionType.GENERIC)
    @Override
	public List<ModuleVO> listAll() {
		return mapper.convertToModuleVOList(dao.getAllActive(), false);
	}

	@Override
	public List<ModuleVO> findById(Long id) {
		Module module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module, true);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public List<ModuleVO> findByIdForInterview(Long id) {
		Module module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module, false);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public ModuleVO create(ModuleVO module) {
		Module moduleEntity = dao.save(mapper.convertToModule(module, false));
		return mapper.convertToModuleVO(moduleEntity, false);
	}

	@Override
	public void update(ModuleVO module) {
		generateIdIfNotExist(module);
		dao.saveOrUpdate(mapper.convertToModule(module, true));
	}

	@Override
	public void delete(ModuleVO module) {
		dao.delete(mapper.convertToModule(module, false));
	}

	@Override
	public void merge(ModuleVO module) {
		dao.merge(mapper.convertToModule(module, true));
	}

	@Override
	public Long getMaxId() {
		return dao.generateIdNode();
	}

	private void generateIdIfNotExist(ModuleVO module) {
		if (StringUtils.isEmpty(module.getIdNode())) {
			module.setIdNode(dao.generateIdNode());
		}
	}

	@Override
	public NodeRuleHolder copyModule(ModuleCopyVO vo) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		return idNodeRuleHolder;
	}
	
	@Override
	public NodeRuleHolder copyModule(ModuleCopyVO vo,ModuleReportVO report) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder,report);
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
	public ModuleReportVO copyRulesValidateAgent(NodeRuleHolder idNodeHolder,ModuleReportVO reportVO) {
		List<AgentVO> missingAgentList = new ArrayList<>();
		List<RuleVO> missingRuleList = new ArrayList<>();
		for (RuleVO ruleVO : idNodeHolder.getRuleList()) {
			Agent agent = agentDao.get(ruleVO.getAgent().getIdAgent());
			if(agent !=null){
			    ruleDao.save(ruleMapper.convertToRule(ruleVO));
			}else{
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
			for(RuleVO ruleVO:report.getMissingRuleAgentList()){
				if(vo.getIdRule() == ruleVO.getIdRule()){
					isRuleMissing = true;
					break;
				}
			}
			if(!isRuleMissing){
				nodeRuleDao.save(nodeRuleMapper.convertToNodeRule(vo));
			}
		}
	}

	@Override
	public NodeRuleHolder copyModuleAutoGenerateModule(ModuleCopyVO copyVo, ModuleReportVO report) {
		ModuleVO vo = copyVo.getVo();
		Long idNode = dao.generateIdNode()+1;
		vo.setIdNode(idNode);
		vo.setName(copyVo.getName());
		vo.setAnchorId(idNode);
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNode, vo.getChildNodes(), idNodeRuleHolder,report);
		createMissingModule(vo,copyVo,idNodeRuleHolder);
		dao.saveCopy(mapper.convertToModule(vo, true));
		return idNodeRuleHolder;
	}

	@Override
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo,ModuleReportVO report) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode()+1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		copyVO.setAnchorId(idNode);
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder,report);
		createMissingFragments(copyVO,idNodeRuleHolder);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		return idNodeRuleHolder;
	}
	
	@Override
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo,ModuleReportVO report,NodeRuleHolder idNodeRuleHolder) {
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNodeRuleHolder.getLastIdNode(), vo.getVo().getChildNodes(), idNodeRuleHolder,report);
		createMissingFragments(vo.getVo(),idNodeRuleHolder);
		dao.saveCopy(mapper.convertToModule(vo.getVo(), true));
		return idNodeRuleHolder;
	}

	private void createMissingFragments(ModuleVO copyVO, NodeRuleHolder idNodeRuleHolder) {
		List<FragmentVO> fragments = copyVO.getFragments();
		for(FragmentVO vo:fragments){
			//check if fragment exist
			List<Fragment> list = fragmentDao.findByName(vo.getName());
			if(list.isEmpty()){
				// no fragment lets create one
				generateIdNodeForFragments(vo,idNodeRuleHolder);
				fragmentDao.saveOrUpdate(fragmentMapper.convertToFragment(vo, true));
			}else if(list.size() > 1){
				// found name more than one add warning
			}else{
				// use existing fragment
			}
		}
		
	}
	
	public void createMissingModule(ModuleVO moduleVO,ModuleCopyVO copyVO,
				NodeRuleHolder idNodeRuleHolder) {
		List<ModuleVO> modules = moduleVO.getModules();
		for(ModuleVO vo:modules){
			//check if module exist
			List<Module> list = dao.findByName(vo.getName());
			if(list.isEmpty()){
				ModuleVO newModuleVO = vo;
				Long idNode = idNodeRuleHolder.getLastIdNode()+1;
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
				copyModuleAutoGenerateFragments(newModuleCopyVO,new ModuleReportVO(),idNodeRuleHolder);
//				fragmentDao.saveOrUpdate(fragmentMapper.convertToFragment(vo, true));
			}else if(list.size() > 1){
				// found name more than one add warning
			}else{
				// use existing fragment
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
		if(nodeVO instanceof ModuleVO){
			moduleVO = (ModuleVO)nodeVO;
			qVoList = moduleVO.getChildNodes();
		}
		FragmentVO fragmentVO = new FragmentVO();
		if(nodeVO instanceof FragmentVO){
			fragmentVO = (FragmentVO)nodeVO;
			qVoList = fragmentVO.getChildNodes();
		}
		PossibleAnswerVO answerVO = new PossibleAnswerVO();
		if(nodeVO instanceof PossibleAnswerVO){
			answerVO = (PossibleAnswerVO)nodeVO;
			qVoList = answerVO.getChildNodes();
		}

		//lets check our module questions
		for(QuestionVO qVO : qVoList){
			if("Q_linkedajsm".equals(qVO.getType())){
				List<FragmentVO> list = fragmentMapper.convertToFragmentVOList(fragmentDao.findByName(qVO.getName()), true);
				if(!list.isEmpty()){
					FragmentVO fragment = list.get(0);
					qVO.setLink(fragment.getIdNode());
					questionService.updateWithIndependentTransaction(qVO);
				}
			}
			if("Q_linkedmodule".equals(qVO.getType())){
				List<ModuleVO> list = mapper.convertToModuleVOList(dao.findByName(qVO.getName()), true);
				if(!list.isEmpty()){
					ModuleVO module = list.get(0);
					qVO.setLink(module.getIdNode());
					questionService.updateWithIndependentTransaction(qVO);
				}
			}
			if(!qVO.getChildNodes().isEmpty()){
				for(PossibleAnswerVO ansVo:qVO.getChildNodes()){
					updateMissingLinks(ansVo);
				}
			}
		}
	}


}
