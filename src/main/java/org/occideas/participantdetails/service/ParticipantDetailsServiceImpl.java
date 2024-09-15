package org.occideas.participantdetails.service;

import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.Participant;
import org.occideas.entity.ParticipantDetails;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.interviewquestion.dao.IInterviewQuestionDao;
import org.occideas.mapper.ParticipantDetailsMapper;
import org.occideas.mapper.ParticipantMapper;
import org.occideas.participant.dao.IParticipantDao;
import org.occideas.participant.service.ParticipantService;
import org.occideas.participantdetails.dao.ParticipantDetailsDao;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantDetailsVO;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ParticipantDetailsServiceImpl implements ParticipantDetailsService {
  @Autowired
  private ParticipantDetailsDao participantDetailsDao;
  @Autowired
  private ParticipantDetailsMapper mapper;

  @Override
  public void deleteList(List<ParticipantDetailsVO> detailsVOList) {
    List<ParticipantDetails> detailsList = mapper.convertToParticipantDetailsList(detailsVOList);
    participantDetailsDao.deleteList(detailsList);
  }

  @Override
  public List<ParticipantDetailsVO> listAll() {
    return null;
  }

  @Override
  public List<ParticipantDetailsVO> findById(Long id) {
    return null;
  }

  @Override
  public ParticipantDetailsVO create(ParticipantDetailsVO o) {
    return null;
  }

  @Override
  public void update(ParticipantDetailsVO o) {

  }

  @Override
  public void delete(ParticipantDetailsVO o) {

  }
}
