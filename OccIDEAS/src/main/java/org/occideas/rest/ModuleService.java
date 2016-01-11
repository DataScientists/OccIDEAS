package org.occideas.rest;

import java.util.List;

//import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.occideas.HibernateUtility;
import org.occideas.Module;

//import org.hibernate.Session;
//import org.occideas.HibernateUtility;
//import org.occideas.Module;

import com.google.gson.Gson;



@Path("/modules")
public class ModuleService{

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public String findModules(){
      return this.findAllModules();
   }
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public String findModule(long id){
      return this.findModuleById(id);
   }
    
   private String findModuleById(long id){
	   Session hibernateSession = null;
		Transaction transaction = null;
		Module module = null;
		try {
			hibernateSession = HibernateUtility.getSessionFactory().openSession();
			transaction = hibernateSession.beginTransaction();
			module = (Module) hibernateSession.get(Module.class, id);
			// NodeHelper.getAllNodes(module);
			transaction.commit();
			hibernateSession.flush();

		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			System.out.println("module not retrieved - " + e.getMessage());
		} finally {
			hibernateSession.close();
		}
		Gson gson = new Gson();
		String json = gson.toJson(module);
		return json;
   }
   private String findAllModules(){
  		Session hibernateSession = null;
		hibernateSession = HibernateUtility.getSessionFactory().openSession();
		hibernateSession.beginTransaction();	
		@SuppressWarnings("unchecked")
		List<Module> modules = hibernateSession.createCriteria(Module.class).list();
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		Gson gson = new Gson();
		String json = gson.toJson(modules);
		
		return json;
  }
  
}

