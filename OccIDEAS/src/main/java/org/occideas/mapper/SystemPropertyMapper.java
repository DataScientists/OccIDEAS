package org.occideas.mapper;

import org.occideas.entity.SystemProperty;
import org.occideas.vo.SystemPropertyVO;

public interface SystemPropertyMapper {

	SystemProperty convertSytemPropertyVOtoSystemProperty(SystemPropertyVO vo);
	SystemPropertyVO convertSytemPropertyToSystemPropertyVO(SystemProperty entity);
	
}
