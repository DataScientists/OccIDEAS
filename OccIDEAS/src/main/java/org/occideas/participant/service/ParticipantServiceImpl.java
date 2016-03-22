package org.occideas.participant.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Participant;
import org.occideas.mapper.ParticipantMapper;
import org.occideas.participant.dao.ParticipantDao;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantServiceImpl implements ParticipantService {

	@Autowired
    private ParticipantDao participantDao;

	@Autowired
    private ParticipantMapper mapper;

    @Override
    public List<ParticipantVO> listAll() {
        return mapper.convertToParticipantVOList(participantDao.getAll(),false);
    }
    
    @Override
    public List<ParticipantVO> findById(Long id) {
        Participant participant = participantDao.get( id);
        ParticipantVO ParticipantVO = mapper.convertToParticipantVO(participant,true);
        List<ParticipantVO> list = new ArrayList<ParticipantVO>();
        list.add(ParticipantVO);
        return list;
    }
    @Override
    public List<ParticipantVO> findByIdForInterview(Long id) {
        Participant participant = participantDao.get( id);
        ParticipantVO ParticipantVO = mapper.convertToInterviewParticipantVO(participant);
        List<ParticipantVO> list = new ArrayList<ParticipantVO>();
        list.add(ParticipantVO);
        return list;
    }
    @Override
    public void update(ParticipantVO o) {
    	participantDao.saveOrUpdate(mapper.convertToParticipant(o,true));
    }
    
    @Override
    public void delete(ParticipantVO o) {
    	o.setDeleted(1);
    	participantDao.saveOrUpdate(mapper.convertToParticipant(o,false));
    }

	@Override
	public ParticipantVO create(ParticipantVO o) {
		Participant entity = new Participant();
		entity.setIdParticipant(participantDao.save(mapper.convertToParticipant(o,true)));
		entity.setReference(o.getReference());
		return mapper.convertToParticipantVO(entity,true);
	}
}
