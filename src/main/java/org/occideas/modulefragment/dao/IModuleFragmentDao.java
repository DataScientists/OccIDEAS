package org.occideas.modulefragment.dao;

import java.util.List;

import org.occideas.entity.ModuleFragment;

public interface IModuleFragmentDao {

	List<ModuleFragment> getModuleFragmentByModuleId(long moduleId);
	
}
