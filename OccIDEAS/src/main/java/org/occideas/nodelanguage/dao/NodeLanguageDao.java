package org.occideas.nodelanguage.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.Language;
import org.occideas.entity.NodeLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NodeLanguageDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void addLanguage(Language entity){
		 sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}
	
	public List<Language> getAllLanguage(){
		final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Language.class);
        return crit.list();
	}
	
	public void save(NodeLanguage entity){
       sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(NodeLanguage entity){
      sessionFactory.getCurrentSession().delete(entity);
    }

	public NodeLanguage get(Long id){
      return (NodeLanguage) sessionFactory.getCurrentSession().get(NodeLanguage.class, id);
    }
	
	public List<NodeLanguage> getNodeLanguageById(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(NodeLanguage.class)
      		  						.add(Restrictions.eq("languageId",id));
        return crit.list();
	}
	
	public List<NodeLanguage> getNodesByLanguage(String language) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(NodeLanguage.class)
      		  						.add(Restrictions.eq("language",language));
        return crit.list();
	}
	
	public NodeLanguage getNodesByLanguageAndWord(Long language,String word) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(NodeLanguage.class)
      		  						.add(Restrictions.eq("languageId",language))
      		  						.add(Restrictions.eq("word",word));
        List list = crit.list();
        if(!list.isEmpty()){
        	return (NodeLanguage)list.get(0);
        }
        
        return null;
	}
	
	public List<Long> getDistinctNodeLanguageId(){
		final Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(NodeLanguage.class)
				.setProjection(
			    Projections.distinct(Projections.projectionList()
			    .add(Projections.property("languageId"), "languageId"))); 
		List list = crit.list();
		if(list != null){
			return list;
		}
		return null;
	}
	
	public List<Language> getDistinctLanguage(List<Long> ids){
		final Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Language.class)
				.add(Restrictions.in("id",ids)); 
		List list = crit.list();
		if(list != null){
			return list;
		}
		return null;
	}
	
}
