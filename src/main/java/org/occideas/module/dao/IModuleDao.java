package org.occideas.module.dao;

import org.occideas.entity.JobModule;
import org.occideas.entity.Node;
import org.occideas.entity.Question;

import java.util.List;

public interface IModuleDao {

  JobModule save(JobModule module);

  void saveCopy(JobModule module);

  void delete(JobModule module);

  List<JobModule> findByName(String name);

  JobModule get(Long id);

  JobModule merge(JobModule module);

  void saveOrUpdate(JobModule module);

  long create(JobModule module);

  List<JobModule> getAll(boolean b);

  List<JobModule> getAllActive();

  Long generateIdNode();

  Node getNodeById(Long idNode);

  List<? extends Node> getNodeByLinkAndModId(Long link, Long modId);

  Question getLinkingQuestionByModId(Long link, Long modId);

  List<Question> getChildFrequencyNodes(String idNode);

  String getNodeNameById(Long idNode);

  List<Question> getAllLinkingQuestionByModId(Long modId);

    List<Question> getChildLinkNodes(String idNode);

    List<? extends Node> getDistinctNodeNameByIdNode(String idNode);

    List<String> getNodeNameByIdNode(String idNode);

    List<? extends Node> getNodeByType(String type);

    void saveOrUpdateIgnoreFK(JobModule module);

    JobModule getModuleByName(String name);

    List<JobModule> findByNameLength(String name);
}
