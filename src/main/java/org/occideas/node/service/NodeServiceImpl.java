package org.occideas.node.service;

import org.occideas.entity.Fragment;
import org.occideas.entity.JobModule;
import org.occideas.entity.Node;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.node.dao.INodeDao;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeServiceImpl implements INodeService {

	@Autowired
	private INodeDao dao;
	@Autowired
	private ModuleMapper moduleMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private PossibleAnswerMapper answerMapper;
	@Autowired
	private FragmentMapper fragmentMapper;
	
	@Override
	public ModuleVO getModule(Long idNode) {
		JobModule module = (JobModule) dao.getNode(idNode);
		return moduleMapper.convertToModuleVOOnly(module);
	}

	@Override
	public QuestionVO getQuestion(Long idNode) {
		Question question = (Question) dao.getNode(idNode);
		return questionMapper.convertToQuestionVOOnly(question);
	}

	@Override
	public NodeVO getNode(Long idNode) {
		Node node = dao.getNode(idNode);
		if ("M".equals(node.getNodeclass())) {
			JobModule module = (JobModule) node;
			return moduleMapper.convertToModuleVO(module,true);
		}
		if ("Q".equals(node.getNodeclass())) {
			Question question = (Question) node;
			return questionMapper.convertToQuestionVO(question);
		}
		if ("P".equals(node.getNodeclass())) {
			PossibleAnswer answer = (PossibleAnswer) node;
			return answerMapper.convertToPossibleAnswerVOOnly(answer, true);
		}
		if ("F".equals(node.getNodeclass())) {
			Fragment fragment = (Fragment) node;
			return fragmentMapper.convertToFragmentVO(fragment, true);
		}
		return null;
	}

}
