package org.occideas.note.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.NoteVO;

import java.util.List;

public interface NoteService extends BaseService<NoteVO> {
  List<NoteVO> getListByInterview(long interviewId);
}
