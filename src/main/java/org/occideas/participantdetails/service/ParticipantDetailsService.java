package org.occideas.participantdetails.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.ParticipantDetails;
import org.occideas.vo.ParticipantDetailsVO;

import java.util.List;

public interface ParticipantDetailsService extends BaseService<ParticipantDetailsVO> {



  void deleteList(List<ParticipantDetailsVO> detailsList);
}
