package org.occideas.mapper;

import org.occideas.entity.JMXLog;
import org.occideas.vo.JMXLogVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JMXLogMapper {

  public JMXLogVO convertToVO(JMXLog entity) {
    if (entity == null) {
      return null;
    }
    JMXLogVO vo = new JMXLogVO();
    vo.setId(entity.getId());
    vo.setDeleted(entity.getDeleted());
    vo.setFunction(entity.getFunction());
    vo.setGetParameters(entity.getGetParameters());
    vo.setHeader(entity.getHeader());
    vo.setPostParameters(entity.getPostParameters());
    vo.setSessionId(entity.getSessionId());
    vo.setUserId(entity.getUserId());
    vo.setCreatedDate(entity.getCreatedDate());
    return vo;
  }


  public JMXLog convertToEntity(JMXLogVO vo) {
    if (vo == null) {
      return null;
    }
    JMXLog entity = new JMXLog();
    entity.setPostParameters(vo.getPostParameters());
    entity.setSessionId(vo.getSessionId());
    entity.setGetParameters(vo.getGetParameters());
    entity.setFunction(vo.getFunction());
    entity.setHeader(vo.getHeader());
    entity.setUserId(vo.getUserId());
    entity.setDeleted(vo.getDeleted());
    return entity;
  }


  public List<JMXLog> convertToEntityList(List<JMXLogVO> voList) {
    if (voList.isEmpty()) {
      return null;
    }
    List<JMXLog> list = new ArrayList<>();
    for (JMXLogVO vo : voList) {
      list.add(convertToEntity(vo));
    }
    return list;
  }


  public List<JMXLogVO> convertToVOList(List<JMXLog> entityList) {
    if (entityList.isEmpty()) {
      return null;
    }
    List<JMXLogVO> list = new ArrayList<>();
    for (JMXLog entity : entityList) {
      list.add(convertToVO(entity));
    }
    return list;
  }
}
