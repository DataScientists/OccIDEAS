package org.occideas.moduleintromodule.dao;

import java.util.List;

import org.occideas.entity.ModuleIntroModule;

public interface IModuleIntroModuleDao {

	List<ModuleIntroModule> getModuleIntroModuleByModuleId(long moduleId);
	
}
