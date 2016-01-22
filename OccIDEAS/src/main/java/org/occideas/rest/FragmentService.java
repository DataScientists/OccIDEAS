package org.occideas.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Fragment;
import org.occideas.entity.HibernateUtility;
import org.occideas.entity.Node;
import org.occideas.rest.common.NodeCommon;

@Path("/fragment")
public class FragmentService extends NodeCommon 
{
	public FragmentService() 
	{
		super.childClass = Fragment.class;
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String createFragment(final Fragment fragment) throws IOException {
		return super.create(fragment);
	}
		
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getFragment(@QueryParam("id") long id) {
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Node updateFragment(final Fragment fragment) throws IOException {
		return super.update(fragment);
	}

	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteFragment(@QueryParam("id") long id) {
		super.delete(id);
	}

	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Fragment> getFragmentList(@DefaultValue("all") @QueryParam("type") String type) {

		Session session = null;
		List<Fragment> list = null;
		try {
			session = HibernateUtility.getSessionFactory().openSession();

			Criteria cr = session.createCriteria(Fragment.class);

			if (!type.equals("all")){
				cr.add(Restrictions.eq("type", type));
			}
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 
		return list;
	}


}
