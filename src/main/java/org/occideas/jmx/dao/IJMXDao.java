package org.occideas.jmx.dao;

import java.util.List;

import org.occideas.entity.JMXLog;

public interface IJMXDao
{

    JMXLog save(JMXLog entity);

    void deleteSoft(JMXLog entity);

    void deleteHard(JMXLog entity);

    List<JMXLog> find(String searchName, Object searchVal);

    List<JMXLog> list();

    List<JMXLog> listDeleted();

}
