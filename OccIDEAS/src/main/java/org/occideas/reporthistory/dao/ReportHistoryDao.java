package org.occideas.reporthistory.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.ReportHistory;
import org.occideas.utilities.ReportsStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportHistoryDao implements IReportHistoryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<ReportHistory> getAll() {
		Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(ReportHistory.class);
		crit.addOrder(Order.desc("id"));
		crit.add(Restrictions.ne("type", "Lookup"));
		return crit.list();
	}

	@Override
	public List<ReportHistory> getByType(String type) {
		Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(ReportHistory.class);
		crit.add(Restrictions.eq("type",type));
		return crit.list();
	}
	
	@Override
	public ReportHistory getLatestByType(String type) {
	
		Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(ReportHistory.class)
				.addOrder(Order.desc("endDt"))
				.setMaxResults(1)
				.add(Restrictions.eq("type",type))
				.add(Restrictions.eq("status",ReportsStatusEnum.COMPLETED.getValue()));
		
		return (ReportHistory) crit.uniqueResult();
	}

	@Override
	public ReportHistory save(ReportHistory entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
		return entity;
	}

	@Override
	public void delete(ReportHistory entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

}
