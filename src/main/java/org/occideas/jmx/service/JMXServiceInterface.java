package org.occideas.jmx.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.JMXLogVO;

import java.util.List;


public interface JMXServiceInterface extends BaseService<JMXLogVO> {

  String createJMXFile(List<JMXLogVO> list);

  List<JMXLogVO> find(String searchName, Object searchVal);

  List<JMXLogVO> list();

  List<JMXLogVO> listDeleted();

  void deleteHard(JMXLogVO vo);

  void deleteSoft(JMXLogVO vo);

  JMXLogVO save(JMXLogVO vo);

}
