package org.occideas.modulerule.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.occideas.entity.ModuleRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ModuleRuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public ModuleRule save(ModuleRule module){
      return (ModuleRule) sessionFactory.getCurrentSession().save(module);
    }

    public void delete(ModuleRule module){
      sessionFactory.getCurrentSession().delete(module);
    }

	public ModuleRule get(Long id){
      return (ModuleRule) sessionFactory.getCurrentSession().get(ModuleRule.class, id);
    }

	public ModuleRule merge(ModuleRule module)   {
      return (ModuleRule) sessionFactory.getCurrentSession().merge(module);
    }

    public void saveOrUpdate(ModuleRule module){
      sessionFactory.getCurrentSession().saveOrUpdate(module);
    }
    @SuppressWarnings("unchecked")
	public List<ModuleRule> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      
      Query query = session
              .createSQLQuery("SELECT COUNT(*) as count FROM ModuleRules WHERE idModule= :id")
              .addScalar("count", LongType.INSTANCE).setParameter("id",18432); 
      
      List firstResult = query.list();
      /*Query query = session.createSQLQuery("select * from ModuleRules")
    		  .addScalar("idModule", StandardBasicTypes.LONG)
    		  .addScalar("idRule", StandardBasicTypes.LONG)
    		  .addScalar("idAgent", StandardBasicTypes.LONG);
      List<ModuleRule> list = query.setResultTransformer(Transformers.aliasToBean(ModuleRule.class)).list();
      */
      /*final Criteria crit = session.createCriteria(ModuleRule.class)   		  						 		  						
    		  						.setResultTransformer(Transformers.aliasToBean(ModuleRule.class));*/
      return null;
    }
    @SuppressWarnings("unchecked")
	public List<ModuleRule> getRulesByModule(Long moduleId) {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(ModuleRule.class)
    		  						.add(Restrictions.eq("idModule",moduleId)) 		  						
    		  						.setResultTransformer(Transformers.aliasToBean(ModuleRule.class));
      return crit.list();
    }
    @SuppressWarnings("unchecked")
	public List<ModuleRule> getRulesByAgent(Long agentId) {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(ModuleRule.class)
    		  						.add(Restrictions.eq("idModule",agentId)) 		  						
    		  						.setResultTransformer(Transformers.aliasToBean(ModuleRule.class));
      return crit.list();
    }
}
