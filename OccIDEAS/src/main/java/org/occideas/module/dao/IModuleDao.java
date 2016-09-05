package org.occideas.module.dao;

import java.util.List;

import org.occideas.entity.Module;

public interface IModuleDao {

	public Module save(Module module);

	public void saveCopy(Module module);

	public void delete(Module module);

	public List<Module> findByName(String name);

	public Module get(Long id);

	public Module merge(Module module);

	public void saveOrUpdate(Module module);

	public List<Module> getAll();

	public List<Module> getAllActive();

	public Long generateIdNode();

}
