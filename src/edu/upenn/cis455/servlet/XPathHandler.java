package edu.upenn.cis455.servlet;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import edu.upenn.cis455.xpathengine.XPathEngine;
import edu.upenn.cis455.xpathengine.XPathEngineFactory;
import edu.upenn.cis455.xpathengine.utils.Utils;

/**
 * Servlet for handling the request coming in from the main servlet
 * @author cis455
 *
 */

@SuppressWarnings("serial")
public class XPathHandler extends HttpServlet {
	static final Logger LOG = Logger.getLogger(XPathHandler.class);
	
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
			XPathEngine engine = XPathEngineFactory.getXPathEngine();
			String xPaths = request.getParameter("xpath");
			String url = request.getParameter("url");
			PrintWriter writer = response.getWriter();
			String toWrite="";
			if(url!=null && xPaths!=null){
				
				String[] xPathTokens = xPaths.split(";");
				engine.setXPaths(xPathTokens);
				Document doc = Utils.getDocmentFromUrl(url);
				if(doc == null){
					doc = Utils.getHtmlDocmentFromUrl(url);
				}
				boolean[] xpathResults = engine.evaluate(doc);
				
				toWrite = Utils.generateOutputDocument(url,xpathResults,xPathTokens,getServletContext().getResourceAsStream("/ServletAnswer.html"));
								
			}
			
			writer.write(toWrite);
			writer.close();
		}catch(IOException ioe){
			LOG.debug("Unable to read input file: "+ioe.getMessage());
		}
		
	}

}