package org.occideas.agent.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.agent.dao.AgentDao;
import org.occideas.entity.Agent;
import org.occideas.mapper.AgentMapper;
import org.occideas.vo.AgentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentDao dao;
	
	@Autowired
	private AgentMapper mapper;

	@Override
	public List<AgentVO> listAll() {
		
		return mapper.convertToAgentVOList(dao.getAllActive(),false);
	}

	@Override
	public List<AgentVO> findById(Long id) {
		Agent module = dao.get(id);
		AgentVO moduleVO = mapper.convertToAgentVO(module,true);
		List<AgentVO> list = new ArrayList<AgentVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public AgentVO create(AgentVO o) {
		Agent moduleEntity = dao.save(mapper.convertToAgent(o,false));
		return mapper.convertToAgentVO(moduleEntity,false);
	}

	@Override
	public void update(AgentVO o) {
		dao.saveOrUpdate(mapper.convertToAgent(o,false));
	}

	@Override
	public void delete(AgentVO o) {
		dao.delete(mapper.convertToAgent(o,false));
		
	}
}
