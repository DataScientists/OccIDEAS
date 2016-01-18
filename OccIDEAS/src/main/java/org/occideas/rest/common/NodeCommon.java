package org.occideas.rest.common;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.occideas.HibernateUtility;
import org.occideas.Module;
import org.occideas.Node;
import org.occideas.mapper.ModuleMapper;
import org.occideas.vo.ModuleVO;

public class NodeCommon 
{
	protected Class<? extends Node> childClass = Node.class;
	
	protected String create(Node node) throws IOException
	{
		Session session = null;
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		node.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		session.save(node);
		} catch (Exception e) {
			System.out.println("Transaction Fail "+e.getMessage());
			return "0";
		}
		finally{
			session.close();					
		}
		return "1";		
	}
	
	protected Node get(long id)
	{
		Session session = null;
		Node node = null;
		try {
			session= HibernateUtility.getSessionFactory().openSession();
			node =  (Node) session.get(childClass, id);
		}
		catch(Exception e){
			System.out.println("node Not retrieved"+e.getMessage());
		}
		return node;
	}
	
	@SuppressWarnings("unchecked")
	protected List<ModuleVO> getAllModules()
	{
		Session session = null;
		List<ModuleVO> moduleList = null;
		try {
			session= HibernateUtility.getSessionFactory().openSession();
			List<Module> modules = session.createCriteria(Module.class).list();
			ModuleMapper moduleMapper = new ModuleMapper();
	   		moduleList = moduleMapper.convertToListVO(modules);
		}catch(Exception e){
			System.out.println("node Not retrieved"+e.getMessage());
		}
		
		return moduleList;
	}
	
	protected Node update(Node node) throws IOException
	{	   	
		Session session = null;
		try {
			session= HibernateUtility.getSessionFactory().openSession();
	   		node.setLastUpdated(new Date(Calendar.getInstance().getTimeInMillis()));
	   		session.update(node);
		} catch (Exception e) {
			System.out.println("Transaction Fail"+e.getMessage());
		}
		return node;
	}	

	protected void delete(long id)
	{
		Session session = null;
		try {
			session= HibernateUtility.getSessionFactory().openSession();
			Node node =  (Node) session.get(this.childClass, id);
			session.delete(node);
		}
		catch(Exception e){
			System.out.println("node Not Deleted"+e.getMessage());
		}
	}	
}
