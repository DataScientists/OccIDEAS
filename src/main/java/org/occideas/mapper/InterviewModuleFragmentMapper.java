package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewModuleFragment;
import org.occideas.vo.InterviewModuleFragmentVO;

public interface InterviewModuleFragmentMapper {

	InterviewModuleFragmentVO convertToVO(InterviewModuleFragment entity);

	List<InterviewModuleFragmentVO> convertToVOList(List<InterviewModuleFragment> entityList);

}
