package org.occideas.utilities;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeUtil {
	
	@Autowired
	private ModuleMapper moduleMapper;
	@Autowired
	private FragmentMapper fragmentMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private PossibleAnswerMapper answerMapper;
	
	public List<NodeVO> convertToNodeVOList(List<Node> nodes){
		if(nodes == null) {
			return null;
		}
		List<NodeVO> list = new ArrayList<>();
		for(Node entity:nodes) {
			list.add(convertToNodeVO(entity));
		}
		return list;
	}
	
	
	public NodeVO convertToNodeVO(Node node) {
		if(NodeDiscriminatorEnum.M.name().equals(node.getNodeDiscriminator())) {
			Module module = new Module(node);
			return moduleMapper.convertToModuleVOOnly(module);
		}
		if(NodeDiscriminatorEnum.F.name().equals(node.getNodeDiscriminator())) {
			Fragment fragment = new Fragment(node);
			return fragmentMapper.convertToFragmentVO(fragment, false);
		}
		if(NodeDiscriminatorEnum.Q.name().equals(node.getNodeDiscriminator())) {
			Question question = new Question(node);
			return questionMapper.convertToQuestionVO(question);
		}
		if(NodeDiscriminatorEnum.P.name().equals(node.getNodeDiscriminator())) {
			PossibleAnswer answer = new PossibleAnswer(node);
			return answerMapper.convertToPossibleAnswerVO(answer, false);
		}
		if(NodeDiscriminatorEnum.O.name().equals(node.getNodeDiscriminator())) {
			Question question = new Question(node);
			return questionMapper.convertToQuestionVO(question);
		}
		return null;
	}
	
}
