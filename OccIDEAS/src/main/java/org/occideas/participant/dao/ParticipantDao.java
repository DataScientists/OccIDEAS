package org.occideas.participant.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Interview;
import org.occideas.entity.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ParticipantDao {

    @Autowired
    private SessionFactory sessionFactory;

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

}
