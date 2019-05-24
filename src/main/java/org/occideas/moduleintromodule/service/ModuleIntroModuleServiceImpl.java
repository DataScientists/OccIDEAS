package org.occideas.moduleintromodule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.occideas.entity.Module;
import org.occideas.fragment.service.FragmentService;
import org.occideas.mapper.ModuleIntroModuleMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.modulefragment.service.ModuleFragmentService;
import org.occideas.moduleintromodule.dao.IModuleIntroModuleDao;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleFragmentVO;
import org.occideas.vo.ModuleIntroModuleVO;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ModuleIntroModuleServiceImpl implements ModuleIntroModuleService{

	@Autowired
	private IModuleIntroModuleDao dao;
	@Autowired
	private IModuleDao moduleDao;
	@Autowired
	private ModuleMapper moduleMapper;
	@Autowired
	private ModuleIntroModuleMapper mapper;
	@Autowired
	private ModuleFragmentService moduleFragmentService;
	@Autowired
	private FragmentService fragmentService;
	
	@Override
	public List<ModuleIntroModuleVO> getModuleIntroModuleByModuleId(long moduleId) {
		return mapper.convertToModuleIntroModuleList(dao.getModuleIntroModuleByModuleId(moduleId));
	}

	@Override
	public List<ModuleVO> findByIdWithFragments(Long id) {
		Module module = moduleDao.get(id);
		ModuleVO moduleVO = moduleMapper.convertToModuleVO(module, true);
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		List<ModuleFragmentVO> moduleFragmentList = moduleFragmentService.getModuleFragmentByModuleId(id);
		List<FragmentVO> fragmentVOList = new ArrayList<>();
		for(ModuleFragmentVO vo:moduleFragmentList){
			List<FragmentVO> resultList = fragmentService.findById(vo.getFragmentId());
			if(!resultList.isEmpty() && resultList.get(0) != null){
				FragmentVO fragmentVO = resultList.get(0);
				fragmentVOList.add(fragmentVO);
			}
		}
		moduleVO.setFragments(fragmentVOList);
		list.add(moduleVO);
		return list;
	}

}
