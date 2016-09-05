package org.occideas.module.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.agent.dao.AgentDao;
import org.occideas.base.service.IQuestionCopier;
import org.occideas.entity.Agent;
import org.occideas.entity.Module;
import org.occideas.fragment.dao.FragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.NodeRuleMapper;
import org.occideas.mapper.RuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.module.dao.ModuleDao;
import org.occideas.noderule.dao.NodeRuleDao;
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
	public NodeRuleHolder copyModuleAutoGenerateFragments(ModuleCopyVO vo,ModuleReportVO report) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode()+1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		copyVO.setAnchorId(idNode);
//		List<Module> moduleList = dao.findByName(copyVO.getName());
		//need to check if the module exist if not create it
//		if(moduleList.isEmpty()){
//			// no modules found create one
//			dao.saveCopy(mapper.convertToModule(copyVO, false));
//		}
		NodeRuleHolder idNodeRuleHolder = createNodeRuleHolder(idNode);
		questionCopier.populateQuestionsIncludeLinksWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder,report);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		// create the missing fragment
//		for(FragmentVO newFragmentVO:idNodeRuleHolder.getFragmentList()){
//			fragmentDao.saveOrUpdate(fragmentMapper.convertToFragment(newFragmentVO, false));
//		}
		return idNodeRuleHolder;
	}

}
