package org.occideas.agent.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.agent.dao.AgentDao;
import org.occideas.entity.Agent;
import org.occideas.mapper.AgentMapper;
import org.occideas.vo.AgentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentDao dao;
	
	@Autowired
	private AgentMapper mapper;

	@Override
	public List<AgentVO> listAll() {
		
		return mapper.convertToAgentVOList(dao.getAllActive());
	}

	@Override
	public List<AgentVO> findById(Long id) {
		Agent module = dao.get(id);
		AgentVO moduleVO = mapper.convertToAgentVO(module);
		List<AgentVO> list = new ArrayList<AgentVO>();
		list.add(moduleVO);
		return list;
	}

	@Override
	public AgentVO create(AgentVO o) {
		Agent moduleEntity = dao.save(mapper.convertToAgent(o));
		return mapper.convertToAgentVO(moduleEntity);
	}

	@Override
	public AgentVO update(AgentVO o) {
		Agent moduleEntity = dao.merge(mapper.convertToAgent(o));
		return mapper.convertToAgentVO(moduleEntity);
	}

	@Override
	public void delete(AgentVO o) {
		dao.delete(mapper.convertToAgent(o));
		
	}
}
