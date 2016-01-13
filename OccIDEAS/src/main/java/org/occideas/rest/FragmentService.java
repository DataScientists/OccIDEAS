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
import org.occideas.Fragment;
import org.occideas.HibernateUtility;
import org.occideas.rest.common.NodeCommon;

import com.google.gson.Gson;

@Path("/fragment")
public class FragmentService extends NodeCommon {

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
	public String getFragment(@QueryParam("id") long id) {
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String updateFragment(final Fragment fragment) throws IOException {
		return super.update(fragment);
	}

	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFragment(@QueryParam("id") long id) {
		return super.delete(id);
	}

	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFragmentList(@DefaultValue("all") @QueryParam("type") String type) {

		Session session = null;
		Transaction tr = null;
		String resp = "";

		try {
			session = HibernateUtility.getSessionFactory().openSession();

			Criteria cr = session.createCriteria(Fragment.class);

			if (!type.equals("all"))
				cr.add(Restrictions.eq("type", type));

			tr = session.beginTransaction();

			List<Fragment> list = cr.list();

			tr.commit();

			if (list != null) // SUCCESSFULLY GET DATA
				resp = "{\"status\": 1,\"data\": " + new Gson().toJson(list)
						+ "}";
			else
				// No Data
				resp = "{\"status\": -1}";
			
		} catch (Exception e) {
			e.printStackTrace(System.out);

			// SERVER ERROR
			resp = "{\"status\":0}";
		} finally {
			session.close();
		}

		return resp;
	}
}
