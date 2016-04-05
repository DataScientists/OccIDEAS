package org.occideas.base.dao;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BaseDaoTest {

	@Mock
	private SessionFactory sessionFactory;
	@Mock
	private Session session;
	
	@InjectMocks
	private BaseDao baseDao;
	
	@Before
	public void init(){
	}
	
	@Test
	public void saveTest(){
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(sessionFactory.getCurrentSession().save(any(Object.class))).thenReturn(1L);
		assertNotNull(baseDao.save(new Object()));
	}
	
}
