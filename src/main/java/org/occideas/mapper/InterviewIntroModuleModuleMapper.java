package org.occideas.mapper;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.vo.InterviewIntroModuleModuleVO;

import java.util.List;

public interface InterviewIntroModuleModuleMapper {

  InterviewIntroModuleModuleVO convertToVO(InterviewIntroModuleModule entity);

  List<InterviewIntroModuleModuleVO> convertToVOList(List<InterviewIntroModuleModule> entityList);

}
