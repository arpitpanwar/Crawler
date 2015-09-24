package edu.upenn.cis455.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.StringConstants;
import edu.upenn.cis455.xpathengine.utils.Utils;
import edu.upenn.cis455.xpathengine.utils.WebUiUtils;

/**
 * Servlet implementation class ChannelServlet
 */
public class ChannelServlet extends HttpServlet {
	static final Logger LOG = Logger.getLogger(ChannelServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChannelServlet() {
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
		
		EntityStore store = new DBWrapper(getServletContext().getInitParameter(Constants.SERVLET_STORE_PARAM)).getStore();
		
		InputStream  stream = getServletContext().getResourceAsStream("/ChannelList.html");
		
		String content = Utils.fetchDataAsString(stream);
		
		content = content.replace(StringConstants.REPLACE_CHANNEL_TABLE, WebUiUtils.generateAllChannelsTable(store));
		try{
			response.getWriter().write(content);
		}catch(IOException ioe){
			LOG.debug("Unable to write output document: "+ioe.getMessage());
		}
	}

}
