package org.occideas.module.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Module;
import org.occideas.entity.NodeRule;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.RuleMapper;
import org.occideas.module.dao.ModuleDao;
import org.occideas.noderule.dao.NodeRuleDao;
import org.occideas.rule.dao.RuleDao;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.ModuleIdNodeRuleHolder;
import org.occideas.vo.ModuleRuleVO;
import org.occideas.vo.ModuleVO;
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
	private ModuleDao dao;
	@Autowired
	private ModuleMapper mapper;
	@Autowired
	private RuleDao ruleDao;
	@Autowired
	private RuleMapper ruleMapper;
	@Autowired
	private NodeRuleDao nodeRuleDao;

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
	public ModuleIdNodeRuleHolder copyModule(ModuleCopyVO vo) {
		ModuleVO copyVO = vo.getVo();
		Long idNode = dao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		ModuleIdNodeRuleHolder idNodeRuleHolder = new ModuleIdNodeRuleHolder();
		long maxRuleId = ruleDao.getMaxRuleId();
		idNodeRuleHolder.setLastIdRule(maxRuleId);
		idNodeRuleHolder.setFirstIdRuleGenerated(maxRuleId);
		idNodeRuleHolder.setIdNode(idNode);
		idNodeRuleHolder.setTopNodeId(idNode);
		populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder);
		dao.saveCopy(mapper.convertToModule(copyVO, true));
		return idNodeRuleHolder;
	}

	private void populateQuestionsWithIdNode(Long idNode, List<QuestionVO> childNodes,
			ModuleIdNodeRuleHolder idNodeRuleHolder) {
		idNodeRuleHolder.setLastIdNode(idNode);
		for (QuestionVO vo : childNodes) {
			vo.setParentId(String.valueOf(idNode));
			vo.setTopNodeId(idNodeRuleHolder.getTopNodeId());
			Long qsIdNode = idNodeRuleHolder.getLastIdNode() + 1;
			idNodeRuleHolder.setLastIdNode(qsIdNode);
			vo.setIdNode(qsIdNode);
			if (!vo.getChildNodes().isEmpty()) {
				populateAnswerWithIdNode(qsIdNode, vo.getChildNodes(), idNodeRuleHolder);
			}
			if (qsIdNode > idNodeRuleHolder.getLastIdNode()) {
				idNodeRuleHolder.setLastIdNode(qsIdNode);
			}
		}
	}

	private void populateAnswerWithIdNode(Long qsIdNode, List<PossibleAnswerVO> childNodes,
			ModuleIdNodeRuleHolder idNodeRuleHolder) {
		int count = 1;
		for (PossibleAnswerVO vo : childNodes) {
			vo.setParentId(String.valueOf(qsIdNode));
			vo.setTopNodeId(idNodeRuleHolder.getTopNodeId());
			Long asIdNode = idNodeRuleHolder.getLastIdNode() + count;
			vo.setIdNode(asIdNode);
			idNodeRuleHolder.setLastIdNode(asIdNode);
			if (!vo.getModuleRule().isEmpty()) {
				populateRulesWithIdRule(vo, vo.getModuleRule(), idNodeRuleHolder);
			}
			if (!vo.getChildNodes().isEmpty()) {
				populateQuestionsWithIdNode(asIdNode, vo.getChildNodes(), idNodeRuleHolder);
			}
			count++;
		}
	}

	private void populateRulesWithIdRule(PossibleAnswerVO vo, List<ModuleRuleVO> moduleRule,
			ModuleIdNodeRuleHolder idNodeRuleHolder) {
		boolean ruleExist = false;
		for (ModuleRuleVO ruleVo : moduleRule) {
			if(idNodeRuleHolder.getRuleIdStorage().
					containsKey(ruleVo.getRule().getIdRule())){
				ruleVo.getRule().setIdRule(idNodeRuleHolder.
						getRuleIdStorage().get(ruleVo.getRule().getIdRule()));
				ruleExist = true;
			}else if (ruleVo.getRule().getIdRule() <= idNodeRuleHolder.getFirstIdRuleGenerated()) {
				idNodeRuleHolder.setLastIdRule(idNodeRuleHolder.getLastIdRule() + 1);
				idNodeRuleHolder.getRuleIdStorage().put(ruleVo.getRule().getIdRule(), 
						idNodeRuleHolder.getLastIdRule());
				ruleVo.getRule().setIdRule(idNodeRuleHolder.getLastIdRule());
			}
			populateRuleCondition(vo, ruleVo.getRule().getConditions(), ruleVo.getRule(), 
					idNodeRuleHolder,ruleExist);
		}
	}

	private void populateRuleCondition(PossibleAnswerVO vo, List<PossibleAnswerVO> conditions, RuleVO ruleVO,
			ModuleIdNodeRuleHolder idNodeRuleHolder,boolean ruleExist) {
		boolean readyToSaveRule = true;
		boolean hasIdNodeBeenSet = false;
		for (PossibleAnswerVO condition : conditions) {
			if (condition.getNumber().equalsIgnoreCase(vo.getNumber()) && condition.getName().equalsIgnoreCase(vo.getName())) {
				NodeRule nodeRule = new NodeRule();
				nodeRule.setIdNode(vo.getIdNode());
				nodeRule.setIdRule(ruleVO.getIdRule());
				idNodeRuleHolder.getNodeRuleList().add(nodeRule);
				hasIdNodeBeenSet = true;
			}
			if (idNodeRuleHolder.getFirstIdRuleGenerated() > vo.getIdNode()) {
				readyToSaveRule = false;
				if (hasIdNodeBeenSet) {
					break;
				}
			}
		}
		if (readyToSaveRule && !ruleExist) {
			ruleVO.getConditions().clear();
			idNodeRuleHolder.getRuleList().add(ruleVO);
		}
	}

	@Override
	public void copyRules(ModuleIdNodeRuleHolder idNodeHolder) {
		for (RuleVO ruleVO : idNodeHolder.getRuleList()) {
			ruleDao.save(ruleMapper.convertToRule(ruleVO));
		}
	}
	
	@Override
	public void addNodeRules(ModuleIdNodeRuleHolder idNodeHolder) {
		for (NodeRule nodeRule : idNodeHolder.getNodeRuleList()) {
			nodeRuleDao.save(nodeRule);
		}
	}

}
