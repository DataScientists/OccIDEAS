package org.occideas.mapper;

import org.occideas.entity.Note;
import org.occideas.vo.NoteVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteMapperImpl implements NoteMapper {

  @Override
  public NoteVO convertToNoteVO(Note note) {
    if (note == null) {
      return null;
    }
    NoteVO noteVO = new NoteVO();
    noteVO.setIdNote(note.getIdNote());
    noteVO.setText(note.getText());
    noteVO.setType(note.getType());
    noteVO.setDeleted(note.getDeleted());
    noteVO.setLastUpdated(note.getLastUpdated());
    noteVO.setInterviewId(note.getInterviewId());

    return noteVO;
  }

  @Override
  public List<NoteVO> convertToNoteVOList(List<Note> participantEntity) {
    if (participantEntity == null) {
      return null;
    }
    List<NoteVO> list = new ArrayList<NoteVO>();
    for (Note participant : participantEntity) {
      list.add(convertToNoteVO(participant));
    }
    return list;
  }

  @Override
  public Note convertToNote(NoteVO noteVO) {
    if (noteVO == null) {
      return null;
    }
    Note note = new Note();
    note.setIdNote(noteVO.getIdNote());
    note.setText(noteVO.getText());
    note.setType(noteVO.getType());
    note.setDeleted(noteVO.getDeleted());
    note.setInterviewId(noteVO.getInterviewId());
    note.setLastUpdated(noteVO.getLastUpdated());

    return note;
  }

  @Override
  public List<Note> convertToNoteList(List<NoteVO> noteVOs) {
    if (noteVOs == null) {
      return null;
    }
    List<Note> list = new ArrayList<Note>();
    for (NoteVO note : noteVOs) {
   //   if (note.getDeleted() == 0) {
        list.add(convertToNote(note));
  //    }
    }
    return list;
  }

}
