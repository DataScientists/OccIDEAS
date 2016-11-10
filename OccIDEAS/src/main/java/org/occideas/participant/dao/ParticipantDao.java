package org.occideas.participant.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.occideas.entity.Interview;
import org.occideas.entity.Participant;
import org.occideas.utilities.PageUtil;
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
    
    @SuppressWarnings("unchecked")
	public List<Participant> getPaginatedParticipantList(int pageNumber,int size) {
    	 final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(Participant.class);
         crit.setFirstResult(pageUtil.calculatePageIndex(pageNumber, size));
         crit.setMaxResults(size);
         return crit.list();
    }
    
    public Integer getParticipantTotalCount(){
    	final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Participant.class);
    	Integer totalResult = ((Number)crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    	return totalResult;
    }

}
