package edu.upenn.cis455.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.UserAccessor;
import edu.upenn.cis455.xpathengine.model.LoginHandler;
import edu.upenn.cis455.xpathengine.utils.Constants;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOG = Logger.getLogger(LoginServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
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
		
		String username = request.getParameter(Constants.LOGIN_USER_NAME);
		
		String password = request.getParameter(Constants.LOGIN_PASSWORD);
		
		try{
		
			if(username==null || password == null || username.length() ==0 || password.length() ==0){
				
				response.sendRedirect("xpath");
				
			}
			EntityStore store = new DBWrapper(getServletContext().getInitParameter(Constants.SERVLET_STORE_PARAM)).getStore();
			if(!LoginHandler.isLoginValid(username, password,store)){
				
				response.sendRedirect("xpath#error");
				
			}else{
				UserAccessor accessor = new UserAccessor(store);
				HttpSession session = request.getSession();
				session.setAttribute("userid", accessor.fetchEntityFromSecondaryKey(username).getUserId());
				//response.addCookie(new Cookie(Constants.COOKIE_SESSION,session.getId()));
				response.sendRedirect("home");
			}
						
			
		}catch(IOException ioe){
			
			LOG.debug("Unexpected error during response sending: "+ioe.getMessage());
			
		}
		
	}

}
