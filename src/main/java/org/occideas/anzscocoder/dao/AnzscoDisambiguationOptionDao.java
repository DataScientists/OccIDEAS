package org.occideas.anzscocoder.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.AnzscoDisambiguationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnzscoDisambiguationOptionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<AnzscoDisambiguationOption> findByPrefix(String prefix) {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(AnzscoDisambiguationOption.class)
            .add(Restrictions.eq("anzscoPrefix", prefix))
            .addOrder(Order.asc("sequence"))
            .list();
    }
}
