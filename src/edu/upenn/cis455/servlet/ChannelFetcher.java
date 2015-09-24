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
import edu.upenn.cis455.xpathengine.model.ChannelInfo;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.Utils;
import edu.upenn.cis455.xpathengine.utils.WebUiUtils;


/**
 * Servlet for retrieving channel xml from the database
 * @author cis455
 *
 */
public class ChannelFetcher extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	static final Logger LOG = Logger.getLogger(HomeServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChannelFetcher() {
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
					
					ChannelInfo channelData = new ChannelInfo();
					response.setContentType(Constants.CONTENT_TYPE_APPLICATION_XML);
					response.getWriter().write(channelData.generateDocument(channelId));
			
				}
				
				
				
			}
		}catch(IOException ioe){
			LOG.debug("Error redirecting: "+ioe.getMessage());
		}
		
	}


}
