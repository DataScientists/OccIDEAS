package org.occideas.module.dao;

import java.util.List;

import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.Question;

public interface IModuleDao {

	public Module save(Module module);

	public void saveCopy(Module module);

	public void delete(Module module);

	public List<Module> findByName(String name);

	public Module get(Long id);

	public Module merge(Module module);

	public void saveOrUpdate(Module module);

	public List<Module> getAll(boolean b);

	public List<Module> getAllActive();

	public Long generateIdNode();
	
	public Node getNodeById(Long idNode);
	
	public List<? extends Node> getNodeByLinkAndModId(Long link, Long modId);

	public Question getLinkingQuestionByModId(Long link, Long modId);
	
	public List<Question> getChildFrequencyNodes(String idNode);

}
