package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.SystemProperty;
import org.occideas.vo.SystemPropertyVO;

public interface SystemPropertyMapper {

	SystemProperty convertSytemPropertyVOtoSystemProperty(SystemPropertyVO vo);
	SystemPropertyVO convertSytemPropertyToSystemPropertyVO(SystemProperty entity);
	List<SystemProperty> convertSystemPropertyVOListToSystemPropertyList(List<SystemPropertyVO> list);
	List<SystemPropertyVO> convertSystemPropertyListToSystemPropertyVOList(List<SystemProperty> list);
}
