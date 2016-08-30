package org.occideas.interviewintromodulemodule.service;

import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.interviewintromodulemodule.dao.InterviewIntroModuleModuleDao;
import org.occideas.mapper.InterviewIntroModuleModuleMapper;
import org.occideas.vo.InterviewIntroModuleModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class InterviewIntroModuleModuleServiceImpl implements InterviewIntroModuleModuleService {

	@Autowired
	private InterviewIntroModuleModuleDao dao;
	@Autowired
	private InterviewIntroModuleModuleMapper mapper;
	
	@Override
	public List<InterviewIntroModuleModuleVO> findInterviewByModuleId(long idModule) {
		List<InterviewIntroModuleModule> list = dao.getInterviewIntroModByModId(idModule);
		return mapper.convertToVOList(list);
	}
	@Override
	public List<InterviewIntroModuleModuleVO> findModulesByInterviewId(long idInterview) {
		List<InterviewIntroModuleModule> list = dao.getModulesByInterviewId(idInterview);
		return mapper.convertToVOList(list);
	}

}
