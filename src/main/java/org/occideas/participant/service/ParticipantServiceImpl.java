package org.occideas.participant.service;

import org.apache.commons.lang3.StringUtils;
import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.Constant;
import org.occideas.entity.Participant;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.interviewquestion.dao.IInterviewQuestionDao;
import org.occideas.mapper.ParticipantMapper;
import org.occideas.participant.dao.IParticipantDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

  @Autowired
  private IParticipantDao participantDao;

  @Autowired
  @Lazy
  private IInterviewQuestionDao interviewQuestionDao;

  @Autowired
  private ParticipantMapper mapper;

  @Autowired
  private SystemPropertyService systemPropertyService;
  private PageUtil<ParticipantIntMod> pageUtilIntMod = new PageUtil<>();
  private PageUtil<AssessmentIntMod> pageAssessmentUtilIntMod = new PageUtil<>();
  private PageUtil<ParticipantVO> pageUtil = new PageUtil<>();

  @Override
  public List<ParticipantVO> listAll() {
    return mapper.convertToParticipantVOList(participantDao.getAll(), true);
  }

  @Override
  public List<ParticipantVO> findById(Long id) {
    Participant participant = participantDao.get(id);
    ParticipantVO ParticipantVO = mapper.convertToParticipantVO(participant, true);
    List<ParticipantVO> list = new ArrayList<ParticipantVO>();
    list.add(ParticipantVO);
    return list;
  }

  @Override
  public List<ParticipantVO> findByIdForInterview(Long id) {
    Participant participant = participantDao.get(id);
    ParticipantVO ParticipantVO = mapper.convertToInterviewParticipantVO(participant);
    List<ParticipantVO> list = new ArrayList<ParticipantVO>();
    list.add(ParticipantVO);
    return list;
  }

  @Override
  public void update(ParticipantVO o) {
    Participant p = mapper.convertToParticipant(o, true);
    participantDao.saveOrUpdate(p);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void updateNewTransaction(ParticipantVO o) {
    participantDao.saveOrUpdate(mapper.convertToParticipant(o, true));
  }

  @Override
  public void delete(ParticipantVO o) {

    participantDao.delete(mapper.convertToParticipant(o, false));
  }

  @Override
  public ParticipantVO create(ParticipantVO o) {
    return create(o, false);
  }

  @Override
  public ParticipantVO createPublic(ParticipantVO o) {
    return create(o, true);
  }

  private ParticipantVO create(ParticipantVO o, boolean applyStudyIdPrefix) {
    Participant toSave = mapper.convertToParticipant(o, true);
    boolean autoAssignReference = StringUtils.isBlank(toSave.getReference());
    if (autoAssignReference) {
      // NOT NULL column - use a placeholder until the generated id is known, below.
      toSave.setReference("");
    }
    Long idParticipant = participantDao.save(toSave);

    String reference = o.getReference();
    if (autoAssignReference) {
      reference = applyStudyIdPrefix
        ? buildPrefixedReference(idParticipant)
        : String.valueOf(idParticipant);
      Participant saved = participantDao.get(idParticipant);
      saved.setReference(reference);
      participantDao.saveOrUpdate(saved);
    }

    Participant entity = new Participant();
    entity.setIdParticipant(idParticipant);
    entity.setReference(reference);
    entity.setStatus(o.getStatus());
    return mapper.convertToParticipantVO(entity, true);
  }

  // "LIVE" + idParticipant zero-padded to at least 5 digits, e.g. "LIVE00001" - same minimum-width
  // padding already used for imported references (see InterviewServiceImpl.generateReferenceAuto).
  // Falls back to the plain numeric reference (historical behaviour) when the SYS_CONFIG prefix is
  // unset or blank, so an unconfigured environment doesn't unexpectedly grow a prefix.
  private String buildPrefixedReference(Long idParticipant) {
    SystemPropertyVO prop = systemPropertyService.getByName(Constant.START_INTERVIEW_ID_PREFIX);
    String prefix = prop == null ? null : StringUtils.trimToNull(prop.getValue());
    if (prefix == null) {
      return String.valueOf(idParticipant);
    }
    return prefix + String.format("%05d", idParticipant);
  }

  @Override
  public List<ParticipantVO> listAllParticipantWithInt() {
    return mapper.convertToParticipantVOListOnly(participantDao.getAll());
  }

  @Override
  public PageVO<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber, int size, GenericFilterVO filterVO) {
    List<ParticipantIntMod> list = participantDao.getPaginatedParticipantWithModList(pageNumber, size, filterVO);
    PageVO<ParticipantIntMod> page = pageUtilIntMod.populatePage(list, pageNumber, size);
    page.setTotalSize(participantDao.getParticipantWithModTotalCount(filterVO).intValue());
    page.setFilterVO(filterVO);
    return page;
  }

  @Override
  public PageVO<AssessmentIntMod> getPaginatedAssessmentWithModList(int pageNumber, int size, GenericFilterVO filterVO) {
    List<AssessmentIntMod> list = participantDao.getPaginatedAssessmentWithModList(pageNumber, size, filterVO);
    PageVO<AssessmentIntMod> page = pageAssessmentUtilIntMod.populatePage(list, pageNumber, size);
    page.setTotalSize(participantDao.getAsssessmentWithModTotalCount(filterVO).intValue());
    page.setFilterVO(filterVO);
    return page;
  }

  @Override
  public PageVO<ParticipantIntMod> getPaginatedParticipantList(int pageNumber, int size, GenericFilterVO filterVO) {
    List<ParticipantIntMod> list = participantDao.getPaginatedParticipantList(pageNumber, size, filterVO);
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
    if (participant != null) {
      return mapper.convertToParticipantVO(participant,false);
    }
    return null;
  }

	@Override
	public List<ParticipantVO> getByReferenceNumberPrefix(String referenceNumberPrefix) {
		List<Participant> participants = participantDao.getByReferenceNumberPrefix(referenceNumberPrefix);

		return mapper.convertToParticipantVOList(participants,false);
	}

  @Override
  public Boolean checkIfStudyAgentPreLoaded() {
    return interviewQuestionDao.checkIfStudyAgentPreLoaded();
  }

  @Override
  public void softDeleteAll() {
     participantDao.softDeleteAll();
  }
}
