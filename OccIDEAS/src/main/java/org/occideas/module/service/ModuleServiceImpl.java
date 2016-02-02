package org.occideas.module.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Module;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.ModuleDao;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


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
	public void update(ModuleVO module) {
		generateIdIfNotExist(module);
		dao.saveOrUpdate(mapper.convertToModule(module));
	}

	private void generateIdIfNotExist(ModuleVO module) {
		if(StringUtils.isEmpty(module.getIdNode())){
			module.setIdNode(dao.generateIdNode());
		}
	}

	@Override
	public void delete(ModuleVO module) {
		dao.delete(mapper.convertToModule(module));
	}

	@Override
	public void merge(ModuleVO module) {
		dao.merge(mapper.convertToModule(module));
	}

}
