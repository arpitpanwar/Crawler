package edu.upenn.cis455.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.*;

import org.apache.log4j.Logger;

/**
 * Main servlet which shows the query screen
 * @author cis455
 *
 */

@SuppressWarnings("serial")
public class XPathServlet extends HttpServlet {
	static final Logger LOG = Logger.getLogger(XPathServlet.class);
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		handleRequest(request, response);
	
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		handleRequest(request, response);
		
	}
	
	private void handleRequest(HttpServletRequest request,HttpServletResponse response){
		
		try{
			InputStream stream = getServletContext().getResourceAsStream("/ServletUI.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String read="";
			PrintWriter writer = response.getWriter();
			while((read=reader.readLine())!=null){
				writer.write(read);
			}
			writer.flush();
			reader.close();
		}catch(IOException ioe){
			LOG.debug("Unable to read input file: "+ioe.getMessage());
		}
		
	}

}









