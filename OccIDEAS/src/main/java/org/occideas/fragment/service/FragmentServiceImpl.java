package org.occideas.fragment.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.occideas.entity.Fragment;
import org.occideas.fragment.dao.FragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.vo.FragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class FragmentServiceImpl implements FragmentService {

	private Logger log = Logger.getLogger(this.getClass());
	
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
	public List<FragmentVO> findByIdForInterview(Long id) {
		Fragment module = dao.get(id);
		FragmentVO moduleVO = mapper.convertToFragmentVO(module,false);
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		list.add(moduleVO);
		return list;
	}
	@Override
	public boolean checkExists(Long id) {
		Fragment fragment = dao.get(id);
		if(fragment!=null){
			return true;
		}
		return false;
	}

	@Override
	public void createFragment(FragmentVO fragmentVO) {
		log.info("FragmentVO:"+fragmentVO);
		dao.save(mapper.convertToFragment(fragmentVO,true));
	}
	
	@Override
	public void update(FragmentVO module) {
		//generateIdIfNotExist(module);
		dao.saveOrUpdate(mapper.convertToFragment(module,true));
		
	}

	@Override
	public void delete(FragmentVO module) {
		dao.delete(mapper.convertToFragment(module,false));
	}

	@Override
	public FragmentVO create(FragmentVO o) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void merge(FragmentVO module) {
		dao.merge(mapper.convertToFragment(module,true));
	}
}
