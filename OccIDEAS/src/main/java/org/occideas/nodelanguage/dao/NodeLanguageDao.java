package org.occideas.nodelanguage.dao;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Language;
import org.occideas.entity.NodeLanguage;
import org.occideas.entity.NodeNodeLanguageMod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NodeLanguageDao implements INodeLanguageDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addLanguage(Language entity){
		 sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}
	
	@Override
	public List<Language> getAllLanguage(){
		final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Language.class);
        return crit.list();
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	@Override
	public void save(NodeLanguage entity){
       sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

	@Override
    public void delete(NodeLanguage entity){
      sessionFactory.getCurrentSession().delete(entity);
    }

	@Override
	public NodeLanguage get(Long id){
      return (NodeLanguage) sessionFactory.getCurrentSession().get(NodeLanguage.class, id);
    }
	
	@Override
	public List<NodeLanguage> getNodeLanguageById(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(NodeLanguage.class)
      		  						.add(Restrictions.eq("languageId",id));
        return crit.list();
	}
	
	@Override
	public List<NodeLanguage> getNodesByLanguage(String language) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(NodeLanguage.class)
      		  						.add(Restrictions.eq("language",language));
        return crit.list();
	}
	
	@Override
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
	
	@Override
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
	
	@Override
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
	
	@Override
	public Language getLanguageById(Long id){
		final Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Language.class)
				.add(Restrictions.eq("id",id)); 
		Object uniqueResult = crit.uniqueResult();
		if(uniqueResult != null){
			return (Language)uniqueResult;
		}
		return null;
	}
	
	@Override
	public List<NodeNodeLanguageMod> getNodeNodeLanguageListMod(){
		final Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(NodeNodeLanguageMod.class); 
		List list = crit.list();
		if(list != null){
			return list;
		}
		return null;
	}
	
	@Override
	public NodeNodeLanguageMod getNodeNodeLanguageMod(long idNode,String flag){
		final Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(NodeNodeLanguageMod.class)
				.add(Restrictions.eq("idNode",idNode))
				.add(Restrictions.eq("flag","bfh-flag-"+flag)); 
		Object uniqueResult = crit.uniqueResult();
		if(uniqueResult != null){
			return (NodeNodeLanguageMod)uniqueResult;
		}
		return null;
	}

	@Override
	public NodeLanguage getNodeLanguageByWordAndLanguage(String word, long languageId) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(NodeLanguage.class)
				.add(Restrictions.eq("word",word))
				.add(Restrictions.eq("languageId",languageId)); 
		Object uniqueResult = crit.uniqueResult();
		if(uniqueResult != null){
			return (NodeLanguage)uniqueResult;
		}
		return null;
	}
	
}
