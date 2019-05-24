package org.occideas.note.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Note save(Note note){
      return (Note) sessionFactory.getCurrentSession().save(note);
    }


    public void delete(Note note){
      sessionFactory.getCurrentSession().delete(note);
    }

	public Note get(Long id){
      return (Note) sessionFactory.getCurrentSession().get(Note.class, id);
    }

	public Note merge(Note note)   {
      return (Note) sessionFactory.getCurrentSession().merge(note);
    }

    public void saveOrUpdate(Note note){
      sessionFactory.getCurrentSession().saveOrUpdate(note);
    }

    @SuppressWarnings("unchecked")
	public List<Note> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Note.class)
    		  						.setProjection(Projections.projectionList()
    		  						.add(Projections.property("idNote"), "idNote")
    		  						.add(Projections.property("text"), "text")
    		  						.add(Projections.property("type"), "type"))
    		  						.setResultTransformer(Transformers.aliasToBean(Note.class));
      return crit.list();
    }
    @SuppressWarnings("unchecked")
   	public List<Note> getAllActive() {
         final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Note.class)
                                    .add(Restrictions.eq("deleted", 0))
                                    .setProjection(Projections.projectionList()
                                            .add(Projections.property("idNote"), "idNote")
                                            .add(Projections.property("text"), "text")
                                            .add(Projections.property("type"), "type"))
                                    .setResultTransformer(Transformers.aliasToBean(Note.class));
         return crit.list();
       }

    @SuppressWarnings("unchecked")
    public List<Note> getListByInterview(long interviewId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Note.class)
                .add(Restrictions.eq("interviewId",interviewId))
                .add(Restrictions.eq("deleted", 0))
                .add(Restrictions.ne("type", "System"))
                .add(Restrictions.isNotNull("text"));
        return crit.list();
    }

}
