package org.occideas.note.service;

import org.occideas.entity.Note;
import org.occideas.mapper.NoteMapper;
import org.occideas.note.dao.NoteDao;
import org.occideas.vo.NoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteDao dao;
	
	@Autowired
	private NoteMapper mapper;

	@Override
	public List<NoteVO> listAll() {
		return mapper.convertToNoteVOList(dao.getAllActive());
	}

	@Override
	public List<NoteVO> findById(Long id) {
		Note note = dao.get(id);
		NoteVO noteVO = mapper.convertToNoteVO(note);
		List<NoteVO> list = new ArrayList<NoteVO>();
		list.add(noteVO);
		return list;
	}

	@Override
	public NoteVO create(NoteVO o) {
		Note noteEntity = dao.save(mapper.convertToNote(o));
		return mapper.convertToNoteVO(noteEntity);
	}

	@Override
	public void update(NoteVO o) {
		dao.saveOrUpdate(mapper.convertToNote(o));
	}

	@Override
	public void delete(NoteVO o) {
		dao.delete(mapper.convertToNote(o));
	}

    @Override
    public List<NoteVO> getListByInterview(long interviewId) {
        List<Note> list = dao.getListByInterview(interviewId);
        return mapper.convertToNoteVOList(list);
    }
}
