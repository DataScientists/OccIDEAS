package org.occideas.module.dao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.occideas.base.dao.AbstractNodeDao;
import org.occideas.entity.JobModule;
import org.occideas.entity.Node;
import org.occideas.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleDao extends AbstractNodeDao<JobModule> {

}
