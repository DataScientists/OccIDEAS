package org.occideas.fragment.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.base.service.IQuestionCopier;
import org.occideas.entity.Fragment;
import org.occideas.fragment.dao.IFragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.rule.dao.IRuleDao;
import org.occideas.rule.dao.RuleDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.FragmentCopyVO;
import org.occideas.vo.FragmentReportVO;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FragmentServiceImpl implements FragmentService {

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private IModuleDao moduleDao;

	@Autowired
	private IRuleDao ruleDao;

	@Autowired
	private IFragmentDao dao;

	@Autowired
	private FragmentMapper mapper;

	@Autowired
	private IQuestionCopier questionCopier;

	@Autowired
	private QuestionMapper questionMapper;

	@Autowired
	private SystemPropertyService systemPropertyService;

	@Override
	public List<FragmentVO> listAll() {
		return mapper.convertToFragmentVOList(dao.getAllActive(), false);
	}

	@Override
	public List<FragmentVO> findById(Long id) {
		Fragment module = dao.get(id);
		FragmentVO moduleVO = mapper.convertToFragmentVO(module, true);
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public List<FragmentVO> findByIdForInterview(Long id) {
		Fragment module = dao.get(id);
		FragmentVO moduleVO = mapper.convertToFragmentVO(module, false);
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public boolean checkExists(Long id) {
		Fragment fragment = dao.get(id);
		if (fragment != null) {
			return true;
		}
		return false;
	}

	@Override
	public void createFragment(FragmentVO fragmentVO) {
		log.info("FragmentVO:" + fragmentVO);
		dao.save(mapper.convertToFragment(fragmentVO, true));
	}

	@Override
	public void update(FragmentVO module) {
		// generateIdIfNotExist(module);
		dao.saveOrUpdate(mapper.convertToFragment(module, true));

	}

	@Override
	public void delete(FragmentVO module) {
		dao.delete(mapper.convertToFragment(module, false));
	}

	@Override
	public FragmentVO create(FragmentVO o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void merge(FragmentVO module) {
		dao.merge(mapper.convertToFragment(module, true));
	}

	@Override
	public NodeRuleHolder copyFragment(FragmentCopyVO vo, FragmentReportVO report) {
		FragmentVO copyVO = vo.getVo();
		Long idNode = moduleDao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		NodeRuleHolder idNodeRuleHolder = new NodeRuleHolder();
		long maxRuleId = ruleDao.getMaxRuleId();
		idNodeRuleHolder.setLastIdRule(maxRuleId);
		idNodeRuleHolder.setFirstIdRuleGenerated(maxRuleId);
		idNodeRuleHolder.setIdNode(idNode);
		idNodeRuleHolder.setTopNodeId(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder, report);
		dao.save(mapper.convertToFragment(copyVO, true));
		return idNodeRuleHolder;
	}

	@Override
	public NodeRuleHolder deepCopyFragment(FragmentCopyVO vo, FragmentReportVO report, Long parentIdNode,
			Long topNodeId) {
		FragmentVO copyVO = vo.getVo();
		Long idNode = moduleDao.generateIdNode() + 1;
		copyVO.setIdNode(idNode);
		copyVO.setName(vo.getName());
		copyVO.setParentId(String.valueOf(parentIdNode));
		if (topNodeId != null) {
			copyVO.setTopNodeId(topNodeId);
		}
		NodeRuleHolder idNodeRuleHolder = new NodeRuleHolder();
		long maxRuleId = ruleDao.getMaxRuleId();
		idNodeRuleHolder.setLastIdRule(maxRuleId);
		idNodeRuleHolder.setFirstIdRuleGenerated(maxRuleId);
		idNodeRuleHolder.setIdNode(idNode);
		idNodeRuleHolder.setTopNodeId(idNode);
		questionCopier.populateQuestionsWithIdNode(idNode, copyVO.getChildNodes(), idNodeRuleHolder, report);
		dao.save(mapper.convertToFragment(copyVO, true));
		return idNodeRuleHolder;
	}

	@Override
	public List<QuestionVO> getLinkingNodes(Long id) {
		return questionMapper.convertToQuestionVOExcludeChildsList(dao.getLinkingNodeById(id));
	}

	@Override
	public List<FragmentVO> getFilterStudyAgents(Long id) {
		Fragment fragment = dao.get(id);
		FragmentVO fragmentVO = mapper.convertToFragmentVO(fragment, true);
		FragmentVO filteredFragmentVO = systemPropertyService.getFragmentNodesWithStudyAgents(fragmentVO);
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		list.add(filteredFragmentVO);
		return list;
	}

	@Override
	public List<FragmentVO> getFilterStudyAgents(Long id, Long idAgent) {
		Fragment fragment = dao.get(id);
		FragmentVO fragmentVO = mapper.convertToFragmentVO(fragment, true);
		FragmentVO filteredFragmentVO = systemPropertyService.getFragmentNodesWithAgents(fragmentVO, idAgent);
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		list.add(filteredFragmentVO);
		return list;
	}

	@Override
	public List<Fragment> getAllFragments() {
		return dao.getAll(true);
	}

	@Override
	public List<FragmentVO> getFragmentParents(Long id) {
		return mapper.convertToFragmentVOList(dao.getFragmentParents(id), false);
	}
}
