package org.occideas.participant.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.entity.Interview;
import org.occideas.entity.Participant;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantDao {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private PageUtil<ParticipantVO> pageUtil;

    public Long save(Participant participant){
        return (Long) sessionFactory.getCurrentSession().save(participant);
      }

    public void delete(Participant participant){
    	participant.setDeleted(1);
    	sessionFactory.getCurrentSession().saveOrUpdate(participant);
    }

	public Participant get(Long id){
      return (Participant) sessionFactory.getCurrentSession().get(Participant.class, id);
    }

	public Participant merge(Interview participant)   {
      return (Participant) sessionFactory.getCurrentSession().merge(participant);
    }

    public void saveOrUpdate(Participant participant){
      sessionFactory.getCurrentSession().saveOrUpdate(participant);
    }

    @SuppressWarnings("unchecked")
	public List<Participant> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Participant.class);
      
      return crit.list();
    }
    
    private final String paginatedParticipantSQL 
    = "select p.idParticipant,p.reference,p.status,p.lastUpdated,p.deleted,i.idinterview,im.idModule"
    		+",im.interviewModuleName from participant p " 
    		+" join interview i join interviewintromodule_module im "
    		+" where p.idParticipant = i.idParticipant " 
    		+ " and i.idinterview = im.interviewId "
    		+ " and p.idParticipant like :idParticipant"
    		+ " and p.reference like :reference"
    		+ " and p.status like :status"
    		+ " and i.idinterview like :idinterview"
    		+ " and im.interviewModuleName like :interviewModuleName"
    		+ " and im.idModule != (select value from sys_config where name = 'activeintro' limit 1)"
    		+ " and p.deleted = 0";
    
    @SuppressWarnings("unchecked")
	public List<ParticipantIntMod> getPaginatedParticipantList(int pageNumber,int size,GenericFilterVO filter) {
//    	 final Session session = sessionFactory.getCurrentSession();
//         final Criteria crit = session.createCriteria(Participant.class);
//         crit.setFirstResult(pageUtil.calculatePageIndex(pageNumber, size));
//         crit.setMaxResults(size);
//         crit.setFetchMode("interviews", FetchMode.JOIN);
//         filter.applyFilter(filter, crit);
//         return crit.list();
    	final Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(paginatedParticipantSQL).
				addEntity(ParticipantIntMod.class);
		sqlQuery.setFirstResult(pageUtil.calculatePageIndex(pageNumber, size));
		sqlQuery.setMaxResults(size);
		filter.applyFilter(filter, sqlQuery);
		List<ParticipantIntMod> list = sqlQuery.list();
		return list;
    }

    private final String participantCountWithModule = 
    		"select count(*) from participant p " 
    	    		+" join interview i join interviewintromodule_module im "
    	    		+" where p.idParticipant = i.idParticipant " 
    	    		+ " and i.idinterview = im.interviewId "
    	    		+ " and p.idParticipant like :idParticipant"
    	    		+ " and p.reference like :reference"
    	    		+ " and p.status like :status"
    	    		+ " and i.idinterview like :idinterview"
    	    		+ " and im.interviewModuleName like :interviewModuleName"
    	    		+ " and im.idModule != (select value from sys_config where name = 'activeintro' limit 1)"
    	    		+ " and p.deleted = 0";
    
    public BigInteger getParticipantTotalCount(GenericFilterVO filter){
    	final Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(participantCountWithModule);
		filter.applyFilter(filter, sqlQuery);
		return (BigInteger) sqlQuery.uniqueResult();
    }

}
