package org.occideas.module.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Module;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.ModuleDao;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private ModuleDao dao;
	
	@Autowired
	private ModuleMapper mapper;
	
	@Override
	public List<ModuleVO> listAll() {
		return mapper.convertToModuleVOList(dao.getAllActive(),false);
	}

	@Override
	public List<ModuleVO> findById(Long id) {
		Module module = dao.get(id);
		ModuleVO moduleVO = mapper.convertToModuleVO(module,true);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public ModuleVO create(ModuleVO module) {
		Module moduleEntity = dao.save(mapper.convertToModule(module));
		return mapper.convertToModuleVO(moduleEntity,false);
	}

	@Override
	public ModuleVO update(ModuleVO module) {
		Module moduleEntity = dao.merge(mapper.convertToModule(module));
		return mapper.convertToModuleVO(moduleEntity,false);
	}

	@Override
	public void delete(ModuleVO module) {
		dao.delete(mapper.convertToModule(module));
	}

}