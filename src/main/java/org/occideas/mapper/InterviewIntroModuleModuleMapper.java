package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.vo.InterviewIntroModuleModuleVO;

public interface InterviewIntroModuleModuleMapper {

	InterviewIntroModuleModuleVO convertToVO(InterviewIntroModuleModule entity);
	
	List<InterviewIntroModuleModuleVO> convertToVOList(List<InterviewIntroModuleModule> entityList);

}
