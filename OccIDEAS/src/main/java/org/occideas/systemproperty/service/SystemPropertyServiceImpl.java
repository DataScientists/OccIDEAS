package org.occideas.systemproperty.service;

import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.SystemProperty;
import org.occideas.mapper.SystemPropertyMapper;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SystemPropertyServiceImpl implements SystemPropertyService{
	
	@Autowired
	private SystemPropertyMapper mapper;
	@Autowired
	private SystemPropertyDao dao;
	
	@Override
	public SystemPropertyVO save(SystemPropertyVO sysProp) {
		SystemProperty systemProperty = dao.save(mapper.convertSytemPropertyVOtoSystemProperty(sysProp));
		return mapper.convertSytemPropertyToSystemPropertyVO(systemProperty);
	}

	@Override
	public SystemPropertyVO getById(String variable) {
		SystemProperty sysProp = dao.getById(variable);
		return mapper.convertSytemPropertyToSystemPropertyVO(sysProp);
	}

	@Override
	public List<SystemPropertyVO> getAll() {
		List<SystemProperty> list = dao.getAll();
		return mapper.convertSystemPropertyListToSystemPropertyVOList(list);
	}

	@Override
	public void delete(SystemPropertyVO vo) {
		dao.delete(mapper.convertSytemPropertyVOtoSystemProperty(vo));
	}

}
