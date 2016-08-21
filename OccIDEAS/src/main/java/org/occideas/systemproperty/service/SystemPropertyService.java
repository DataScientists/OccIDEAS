package org.occideas.systemproperty.service;

import java.util.List;

import org.occideas.vo.SystemPropertyVO;

public interface SystemPropertyService {

	public SystemPropertyVO save(SystemPropertyVO sysProp);
	public SystemPropertyVO getById(String variable);
	public List<SystemPropertyVO> getAll();
	public void delete(SystemPropertyVO vo);
	
}
