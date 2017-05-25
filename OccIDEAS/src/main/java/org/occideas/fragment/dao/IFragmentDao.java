package org.occideas.fragment.dao;

import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.entity.Question;

public interface IFragmentDao {

	void save(Fragment fragment);

	List<Fragment> findByName(String name);

	List<Question> getLinkingNodeById(long idNode);

	void delete(Fragment fragment);

	Fragment get(Long id);

	Fragment merge(Fragment fragment);

	void saveOrUpdate(Fragment fragment);

	List<Fragment> getAll();

	List<Fragment> getAllActive();

	/**
	 * Get all with children nodes
	 * @param isIncludeChild
	 * @return
	 */
	List<Fragment> getAll(boolean isIncludeChild);

	List<Fragment> getFragmentParents(Long id);

}