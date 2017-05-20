package org.occideas.participant.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.Participant;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.mapper.ParticipantMapper;
import org.occideas.participant.dao.IParticipantDao;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

	@Autowired
    private IParticipantDao participantDao;

	@Autowired
    private ParticipantMapper mapper;
	
	@Autowired
	private PageUtil<ParticipantIntMod> pageUtilIntMod;
	
	@Autowired
	private PageUtil<AssessmentIntMod> pageAssessmentUtilIntMod;
	
	@Autowired
	private PageUtil<ParticipantVO> pageUtil;

    @Override
    public List<ParticipantVO> listAll() {
        return mapper.convertToParticipantVOList(participantDao.getAll(),true);
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
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Override
    public void updateNewTransaction(ParticipantVO o) {
    	participantDao.saveOrUpdate(mapper.convertToParticipant(o,true));
    }
    
    @Auditable(actionType = AuditingActionType.DELETE_PARTICIPANT)
    @Override
    public void delete(ParticipantVO o) {
    	
    	participantDao.delete(mapper.convertToParticipant(o,false));
    }

    @Auditable(actionType = AuditingActionType.ADD_PARTICIPANT)
	@Override
	public ParticipantVO create(ParticipantVO o) {
		Participant entity = new Participant();
		entity.setIdParticipant(participantDao.save(mapper.convertToParticipant(o,true)));
		entity.setReference(o.getReference());
		return mapper.convertToParticipantVO(entity,true);
	}

	@Override
	public List<ParticipantVO> listAllParticipantWithInt() {
		return mapper.convertToParticipantVOListOnly(participantDao.getAll());
	}

	@Override
	public PageVO<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber, int size,GenericFilterVO filterVO) {
		List<ParticipantIntMod> list = participantDao.getPaginatedParticipantWithModList(pageNumber, size,filterVO);
		PageVO<ParticipantIntMod> page = pageUtilIntMod.populatePage(list, pageNumber, size);
		page.setTotalSize(participantDao.getParticipantWithModTotalCount(filterVO).intValue());
		page.setFilterVO(filterVO);
		return page;
	}
	
	@Override
	public PageVO<AssessmentIntMod> getPaginatedAssessmentWithModList(int pageNumber, int size,GenericFilterVO filterVO) {
		List<AssessmentIntMod> list = participantDao.getPaginatedAssessmentWithModList(pageNumber, size,filterVO);
		PageVO<AssessmentIntMod> page = pageAssessmentUtilIntMod.populatePage(list, pageNumber, size);
		page.setTotalSize(participantDao.getAsssessmentWithModTotalCount(filterVO).intValue());
		page.setFilterVO(filterVO);
		return page;
	}

	@Override
	public PageVO<ParticipantIntMod> getPaginatedParticipantList(int pageNumber, int size, GenericFilterVO filterVO) {
		List<ParticipantIntMod> list = participantDao.getPaginatedParticipantList(pageNumber, size,filterVO);
		PageVO<ParticipantIntMod> page = pageUtilIntMod.populatePage(list, pageNumber, size);
		page.setTotalSize(participantDao.getPaginatedParticipantTotalCount(filterVO).intValue());
		page.setFilterVO(filterVO);
		return page;
	}

	@Override
	public Long getMaxParticipantId() {
		return participantDao.getMaxParticipantId();
	}

	@Override
	public String getMaxReferenceNumber() {
		return participantDao.getMaxReferenceNumber();
	}

	@Override
	public ParticipantVO getByReferenceNumber(String referenceNumber) {
		Participant participant = participantDao.getByReferenceNumber(referenceNumber);
		if(participant != null){
			return mapper.convertToParticipantVOonly(participant);
		}
		return null;
	}
}
