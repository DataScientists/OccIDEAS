package org.occideas.base.service;

import java.util.List;

public interface BaseService<T> {

  List<T> listAll();

  List<T> findById(Long id);

  T create(T o);

  void update(T o);

  void delete(T o);

}
