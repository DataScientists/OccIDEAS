package org.occideas.modulefragment.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.occideas.mapper.ModuleFragmentMapper;
import org.occideas.modulefragment.dao.IModuleFragmentDao;
import org.occideas.vo.ModuleFragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ModuleFragmentServiceImpl implements ModuleFragmentService{

	@Autowired
	private IModuleFragmentDao dao;
	@Autowired
	private ModuleFragmentMapper mapper;
	
	@Override
	public List<ModuleFragmentVO> getModuleFragmentByModuleId(long moduleId) {
		return mapper.convertToModuleFragmentList(dao.getModuleFragmentByModuleId(moduleId));
	}

}
