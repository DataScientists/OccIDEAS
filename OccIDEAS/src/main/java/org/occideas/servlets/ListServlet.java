package org.occideas.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.occideas.entity.HibernateUtility;
import org.occideas.entity.Module;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class FragmentList
 */
public class ListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		try {
			String action = "loadfullmodulelist";
			if (action.equalsIgnoreCase("loadfullmodulelist")) {
				// hibernate code to retrieve list of modules
				
				List<Module> json = this.loadFullModuleList();
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(new ObjectMapper().writeValueAsString(json));
			}
		} catch (Throwable t) {
			// throw 400 back to user
			try {
				response.getWriter().write(t.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    @SuppressWarnings("unchecked")
	private List<Module> loadFullModuleList(){
    	Session hibernateSession = HibernateUtility.getSessionFactory().openSession();
    	return (List<Module>)hibernateSession.createCriteria(Module.class).list();
    }
}
