package org.occideas.possibleanswer.dao;

import org.hibernate.SessionFactory;
import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PossibleAnswerDao implements IPossibleAnswerDao
{
	
	@Autowired
	private SessionFactory sessionFactory;

    @Autowired
    private BaseDao baseDao;

    @Override
    public PossibleAnswer get(long id)
    {
        return baseDao.get(PossibleAnswer.class, id);
    }
    
    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(PossibleAnswer answer) {
		sessionFactory.getCurrentSession().saveOrUpdate(answer);
	}
    
    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateIgnoreFK(PossibleAnswer answer) {
		sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
		.executeUpdate();
		sessionFactory.getCurrentSession().saveOrUpdate(answer);
	}
    
    
}
