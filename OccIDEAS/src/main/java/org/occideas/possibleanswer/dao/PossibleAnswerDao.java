package org.occideas.possibleanswer.dao;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PossibleAnswerDao implements IPossibleAnswerDao
{

    @Autowired
    private BaseDao baseDao;

    @Override
    public PossibleAnswer get(long id)
    {
        return baseDao.get(PossibleAnswer.class, id);
    }
    
    
}
