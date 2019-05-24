package org.occideas.jmx.dao;

import org.occideas.entity.JMXLog;

import java.util.List;

public interface IJMXDao {

  JMXLog save(JMXLog entity);

  void deleteSoft(JMXLog entity);

  void deleteHard(JMXLog entity);

  List<JMXLog> find(String searchName, Object searchVal);

  List<JMXLog> list();

  List<JMXLog> listDeleted();

}
