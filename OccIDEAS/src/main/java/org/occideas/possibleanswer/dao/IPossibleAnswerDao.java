package org.occideas.possibleanswer.dao;

import org.occideas.entity.PossibleAnswer;

public interface IPossibleAnswerDao
{

    PossibleAnswer get(long id);

	void saveOrUpdate(PossibleAnswer answer);

	void saveOrUpdateIgnoreFK(PossibleAnswer answer);
    
}
