package org.occideas.rest;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.occideas.HibernateUtility;
import org.occideas.Module;

import com.google.gson.Gson;

@Path("/ModuleService")
public class ModuleService {

   @GET
   @Path("/modules")
   @Produces(MediaType.APPLICATION_JSON)
   public List<Module> getModules(){
      return this.getAllModules();
   }	
   
   private List<Module> getAllModules(){
   		Session hibernateSession = null;
		hibernateSession = HibernateUtility.getSessionFactory().openSession();
		hibernateSession.beginTransaction();	
		@SuppressWarnings("unchecked")
		List<Module> modules = hibernateSession.createCriteria(Module.class).list();
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		Gson gson = new Gson();
		//Gson gson = new GsonBuilder().excludeFieldsWithModifiers(modifiers).excludeFieldsWithoutExposeAnnotation().create() ;
		String json = gson.toJson(modules);
		return modules;
   }
}

