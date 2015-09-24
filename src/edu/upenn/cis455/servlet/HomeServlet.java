package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.Utils;
import edu.upenn.cis455.xpathengine.utils.WebUiUtils;

/**
 * Servlet implementation class HomeServlet
 */
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOG = Logger.getLogger(HomeServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			handleRequest(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			handleRequest(request, response);
	}
	
	private void handleRequest(HttpServletRequest request, HttpServletResponse response){
		
		HttpSession session = request.getSession(false);
		try{
			
			if(session==null || !Utils.isValidSession(session)){
				response.sendRedirect("xpath");
			}else{
				EntityStore store = new DBWrapper(getServletContext().getInitParameter(Constants.SERVLET_STORE_PARAM)).getStore();
				InputStream stream = getServletContext().getResourceAsStream("/home.html");
				String content = Utils.fetchDataAsString(stream);
				content = content.replace(Constants.REPLACE_TABLE, WebUiUtils.generateUserTableInfo(session.getAttribute("userid").toString(), store));
				response.getWriter().write(content);
			}
		}catch(IOException ioe){
			LOG.debug("Error redirecting: "+ioe.getMessage());
		}
		
	}

}
