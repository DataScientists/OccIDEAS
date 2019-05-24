package org.occideas.moduleintromodule.dao;

import org.occideas.entity.ModuleIntroModule;

import java.util.List;

public interface IModuleIntroModuleDao {

  List<ModuleIntroModule> getModuleIntroModuleByModuleId(long moduleId);

}
