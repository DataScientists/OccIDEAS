package org.occideas.modulefragment.dao;

import org.occideas.entity.ModuleFragment;

import java.util.List;

public interface IModuleFragmentDao {

  List<ModuleFragment> getModuleFragmentByModuleId(long moduleId);

}
