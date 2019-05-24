package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Note;
import org.occideas.vo.NoteVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

  NoteVO convertToNoteVO(Note note);

  List<NoteVO> convertToNoteVOList(List<Note> note);

  Note convertToNote(NoteVO nodeVO);

  List<Note> convertToNoteList(List<NoteVO> nodeVO);

}
