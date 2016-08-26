package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.vo.InterviewIntroModuleModuleVO;

public class InterviewIntroModuleModuleMapperImpl implements InterviewIntroModuleModuleMapper {

	@Override
	public InterviewIntroModuleModuleVO convertToVO(InterviewIntroModuleModule entity) {
		if(entity == null){
			return null;
		}
		InterviewIntroModuleModuleVO vo = new InterviewIntroModuleModuleVO();
		vo.setPrimaryKey(entity.getPrimaryKey());
		vo.setIdModule(entity.getIdModule());
		vo.setIdNode(entity.getIdNode());
		vo.setInterviewId(entity.getInterviewId());
		vo.setInterviewModuleName(entity.getInterviewModuleName());
		vo.setIntroModuleNodeName(entity.getIntroModuleNodeName());
		vo.setNodeNumber(entity.getNodeNumber());
		return vo;
	}

	@Override
	public List<InterviewIntroModuleModuleVO> convertToVOList(List<InterviewIntroModuleModule> entityList) {
		if(entityList == null){
			return null;
		}
		List<InterviewIntroModuleModuleVO> list = new ArrayList<>();
		for(InterviewIntroModuleModule entity:entityList){
			list.add(convertToVO(entity));
		}
		return list;
	}

}
