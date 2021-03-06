package org.occideas.mapper;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewIntroModuleModuleMapperImpl implements InterviewIntroModuleModuleMapper {

  @Override
  public InterviewIntroModuleModuleVO convertToVO(InterviewIntroModuleModule entity) {
    if (entity == null) {
      return null;
    }
    InterviewIntroModuleModuleVO vo = new InterviewIntroModuleModuleVO();
    vo.setPrimaryKey(entity.getPrimaryKey());
    vo.setIdModule(entity.getIdModule());
    vo.setInterviewId(entity.getInterviewId());
    vo.setInterviewModuleName(entity.getInterviewModuleName());
    vo.setIntroModuleNodeName(entity.getIntroModuleNodeName());
    vo.setInterviewPrimaryKey(entity.getInterviewPrimaryKey());
    //vo.setLinkId(entity.getLinkId());
    //vo.setLinkName(entity.getLinkName());
    return vo;
  }

  @Override
  public List<InterviewIntroModuleModuleVO> convertToVOList(List<InterviewIntroModuleModule> entityList) {
    if (entityList == null) {
      return null;
    }
    List<InterviewIntroModuleModuleVO> list = new ArrayList<>();
    for (InterviewIntroModuleModule entity : entityList) {
      list.add(convertToVO(entity));
    }
    return list;
  }

}
