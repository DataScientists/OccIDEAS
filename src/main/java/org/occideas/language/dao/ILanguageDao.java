package org.occideas.language.dao;

import org.occideas.entity.*;

import java.util.List;

public interface ILanguageDao {

  void batchSave(List<Language> convertToLanguageList);

  void deleteAll();
}
