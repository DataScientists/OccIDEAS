package org.occideas.fragment.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.entity.Module;
import org.occideas.fragment.dao.FragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FragmentServiceImpl implements FragmentService {

	@Autowired
	private FragmentDao dao;
	
	@Autowired
	private FragmentMapper mapper;
	
	@Override
	public List<FragmentVO> listAll() {
		return mapper.convertToFragmentVOList(dao.getAllActive(),false);
	}

	@Override
	public List<FragmentVO> findById(Long id) {
		Fragment module = dao.get(id);
		FragmentVO moduleVO = mapper.convertToFragmentVO(module,true);
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public FragmentVO create(FragmentVO module) {
		Fragment moduleEntity = dao.save(mapper.convertToFragment(module));
		return mapper.convertToFragmentVO(moduleEntity,false);
	}

	@Override
	public void update(FragmentVO module) {
		dao.merge(mapper.convertToFragment(module));
	}

	@Override
	public void delete(FragmentVO module) {
		dao.delete(mapper.convertToFragment(module));
	}

}
