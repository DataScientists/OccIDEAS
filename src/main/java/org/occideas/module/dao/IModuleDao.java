package org.occideas.module.dao;

import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.Question;

import java.util.List;

public interface IModuleDao {

  Module save(Module module);

  void saveCopy(Module module);

  void delete(Module module);

  List<Module> findByName(String name);

  Module get(Long id);

  Module merge(Module module);

  void saveOrUpdate(Module module);

  long create(Module module);

  List<Module> getAll(boolean b);

  List<Module> getAllActive();

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

  void saveOrUpdateIgnoreFK(Module module);

  List getLinkByTopNodeId(long idNode);
}
