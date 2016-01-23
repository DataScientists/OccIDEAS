package org.occideas.base.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.occideas.entity.Module;
import org.occideas.entity.Node;

import static org.mockito.Mockito.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class BaseDaoTest {

	private Module module;
	
	@Mock
	private SessionFactory sessionFactory;
	@Mock
	private Session session;
	
	@InjectMocks
	private BaseDao baseDao;
	
	@Before
	public void init(){
		module = new Module();
	}
	
	@Test
	public void saveTest(){
		when(sessionFactory.getCurrentSession()).thenReturn(session);
		when(sessionFactory.getCurrentSession().save(any(Object.class))).thenReturn(module);
		assertNotNull(baseDao.save(new Object()));
	}
	
}
