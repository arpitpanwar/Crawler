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
import edu.upenn.cis455.storage.accessor.UserAccessor;
import edu.upenn.cis455.storage.entity.UserEntity;
import edu.upenn.cis455.xpathengine.model.UserInfo;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.StringConstants;
import edu.upenn.cis455.xpathengine.utils.Utils;
import edu.upenn.cis455.xpathengine.utils.WebUiUtils;


/**
 * Servlet for deleting the channels
 * @author cis455
 *
 */
public class ChannelModifier extends HttpServlet {

	static final Logger LOG = Logger.getLogger(ChannelServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChannelModifier() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request,response);
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
			
				String userId = (String)session.getAttribute("userid");
				String channelId = request.getParameter("id");
				if(userId==null || channelId==null){
					session.invalidate();
					response.sendRedirect("xpath");
				}else{
						
					UserAccessor accessor = new UserAccessor(DBWrapper.getStore());
					UserEntity ent = accessor.fetchEntityFromPrimaryKey(userId);
					
					if(ent==null){
						session.invalidate();
						response.sendRedirect("xpath");
					}else{
						
						UserInfo userInf = new UserInfo(ent);
						if(!userInf.isUserChannelPresent(channelId)){
							session.invalidate();
							response.sendRedirect("xpath");
						}else{
							
							if(userInf.deleteChannel(channelId)){
								response.sendRedirect("home#deletesuccess");
							}else{
								response.sendRedirect("home#deleteerror");
							}
							
						}
						
					}
					
				}
				
				
				
			}
			
			
		}catch(IOException ioe){
			LOG.debug("Unable to send response: "+ioe.getMessage());
		}catch(Exception e){
			LOG.debug("Unexpected error during servlet processing: "+e.getMessage());
			session.invalidate();
			try{
				response.sendRedirect("xpath");
			}catch(IOException ioe){
				
			}
		}
		
		
	}

	
}
