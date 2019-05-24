package org.occideas.mapper;

import org.occideas.entity.InterviewModuleFragment;
import org.occideas.vo.InterviewModuleFragmentVO;

import java.util.List;

public interface InterviewModuleFragmentMapper {

  InterviewModuleFragmentVO convertToVO(InterviewModuleFragment entity);

  List<InterviewModuleFragmentVO> convertToVOList(List<InterviewModuleFragment> entityList);

}
