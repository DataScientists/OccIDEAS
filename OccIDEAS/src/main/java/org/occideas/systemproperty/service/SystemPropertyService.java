package org.occideas.systemproperty.service;

import org.occideas.vo.SystemPropertyVO;

public interface SystemPropertyService {

	public SystemPropertyVO save(SystemPropertyVO sysProp);
	public SystemPropertyVO getById(String variable);
	
}
