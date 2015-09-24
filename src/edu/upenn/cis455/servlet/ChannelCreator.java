package edu.upenn.cis455.servlet;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.ChannelAccessor;
import edu.upenn.cis455.storage.accessor.UserChannelAccessor;
import edu.upenn.cis455.storage.entity.ChannelEntity;
import edu.upenn.cis455.storage.entity.UserChannelEntity;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.StringConstants;
import edu.upenn.cis455.xpathengine.utils.Utils;
import edu.upenn.cis455.xpathengine.utils.WebUiUtils;

/**
 * Servlet which creates channels
 * @author cis455
 *
 */

public class ChannelCreator extends HttpServlet {

	
	static final Logger LOG = Logger.getLogger(ChannelServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChannelCreator() {
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
				
				String channelName = request.getParameter("channelname");
				String channelXPaths = request.getParameter("channelpaths");
				String channelstylesheet = request.getParameter("channelstylesheet");
				
				if(channelName==null|| channelXPaths==null || channelstylesheet ==null){
					response.sendRedirect("home#createerror");					
				}else{
					String userId = session.getAttribute("userid").toString();
					
					EntityStore store = new DBWrapper(getServletContext().getInitParameter(Constants.SERVLET_STORE_PARAM)).getStore();
					ChannelAccessor accessor = new ChannelAccessor(store);
					UserChannelAccessor userChannelAccessor = new UserChannelAccessor(store);
					UserChannelEntity userChannelEntity = userChannelAccessor.fetchEntityFromPrimaryKey(userId);
					

					if(userChannelEntity==null){
						
						ChannelEntity entity = new ChannelEntity();
						entity.setChannelId(Utils.generateUniqueId());
						entity.setChannelName(channelName);
						entity.setxPathExpressions(channelXPaths);
						entity.setStyleSheet(channelstylesheet);
						entity.setChannelCreatedBy(userId);
						entity.setChannelLastUpdated(new Date().getTime());
						accessor.putEntity(entity);
						
						userChannelEntity = new UserChannelEntity();
						userChannelEntity.setUserId(userId);
						List<String> ids = new ArrayList<String>();
						ids.add(entity.getChannelId());
						userChannelEntity.setChannelId(ids);
						userChannelEntity.setUserChannelId(Utils.generateUniqueId());
						userChannelAccessor.putEntity(userChannelEntity);
											
					}else{
						
						List<String> channels = userChannelEntity.getChannelId();
						
						for(String id:channels){
							
							ChannelEntity channelEnt = accessor.fetchEntityFromPrimaryKey(id);
							
							if(channels.contains(channelName)){
								response.sendRedirect("home#uniqueerror");					
							}
											
						}
						

						ChannelEntity entity = new ChannelEntity();
						entity.setChannelId(Utils.generateUniqueId());
						entity.setChannelName(channelName);
						entity.setxPathExpressions(channelXPaths);
						entity.setStyleSheet(channelstylesheet);
						entity.setChannelCreatedBy(userId);
						entity.setChannelLastUpdated(new Date().getTime());
						accessor.putEntity(entity);
						
						channels.add(entity.getChannelId());
						userChannelEntity.setChannelId(channels);
						userChannelAccessor.putEntity(userChannelEntity);
					
					}
			
					response.sendRedirect("home#createsuccess");
				}
				
			}
		}catch (IOException ioe) {
			LOG.debug("IO error while sending response: "+ioe.getMessage());
		}catch(Exception e){
			LOG.debug("Unexpected error when handling request: "+e.getMessage());
		}
			
		
		
	}
	
	
}
