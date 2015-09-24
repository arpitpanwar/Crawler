package edu.upenn.cis455.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.UserAccessor;
import edu.upenn.cis455.storage.entity.UserEntity;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.PasswordHash;
import edu.upenn.cis455.xpathengine.utils.Utils;
import edu.upenn.cis455.xpathengine.utils.WebUiUtils;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	
	static final Logger LOG = Logger.getLogger(RegisterServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.sendRedirect("register.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);

	}
	
	private void handleRequest(HttpServletRequest request, HttpServletResponse response){
		
		EntityStore store = new DBWrapper(getServletContext().getInitParameter(Constants.SERVLET_STORE_PARAM)).getStore();
		try{
			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			if(firstName ==null || lastName == null || email == null || password == null){
				response.sendRedirect("register.html#error");
			}
			
			if(WebUiUtils.isEmailPresent(email, store)){
				response.sendRedirect("register.html#uniqueError");
			}else{
				UserEntity user = new UserEntity();
				user.setUserId(Utils.generateUniqueId());
				user.setUserEmail(email);
				user.setUserFirstName(firstName);
				user.setUserLastName(lastName);
				user.setUserPassword(PasswordHash.createHash(password));
				user.setUserType(Constants.USER_TYPE_GENERAL);
				user.setLastLogin(0);
				UserAccessor accessor = new UserAccessor(store);
				accessor.putEntity(user);
				response.sendRedirect("register.html#success");
			}
			
			
		}catch(IOException ioe){
			LOG.debug("Unable to send register response: "+ioe.getMessage());
		}catch(Exception e){
			LOG.debug("Unexpected error during request processing: "+e.getMessage());
		}		
			
	}

}
