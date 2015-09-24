package edu.upenn.cis455.xpathengine.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.UID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class containing Utility functions
 * @author cis455
 *
 */

public class Utils {
	
	static final Logger LOG = Logger.getLogger(Utils.class);
	
	/**
	 * Fetching the data from the URl as a String
	 * @param url
	 * @return String
	 */
	public static String fetchDataAsString(String url){
		StringBuilder sb = new StringBuilder();
		try{
			if(!(url.startsWith("http")|url.startsWith("https"))){
				url = "http://"+url;
			}
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
			conn.connect();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String read="";
			
			while((read=reader.readLine())!=null){
				sb.append(read);
			}
			reader.close();
			
		}catch(MalformedURLException mfe){
			LOG.debug("Malformed Url: "+url+"\nException Received: "+mfe.getMessage());
		}catch(IOException ioe){
			LOG.debug("Error processing request stream: "+ioe.getMessage());
		}
		
		
		
		return sb.toString();
	}
	/**
	 * Fetch the data from a URL Object
	 * @param u
	 * @return
	 */
	public static String fetchDataAsString(URL u){
		StringBuilder sb = new StringBuilder();
		try{
			InputStream stream=null;
			if(!u.getProtocol().equalsIgnoreCase("https")){
				URLConnection conn = u.openConnection();
				conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
				conn.connect();
				stream = conn.getInputStream();
			}else{
				if((u.getProtocol().equalsIgnoreCase("https"))){
					HttpsURLConnection conn = (HttpsURLConnection)u.openConnection();
					conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
					conn.connect();
					stream = conn.getInputStream();
				}
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String read="";
			
			while((read=reader.readLine())!=null){
				sb.append(read);
			}
			reader.close();
			
		}catch(MalformedURLException mfe){
			LOG.debug("Malformed Url: "+u+"\nException Received: "+mfe.getMessage());
		}catch(IOException ioe){
			LOG.debug("Error processing request stream: "+ioe.getMessage());
		}
		
		
		
		return sb.toString();
	}
	
	public static String fetchDataAsString(InputStream stream){
		StringBuilder sb = new StringBuilder();
		try{
			if(stream!=null){
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String read="";
				
				while((read=reader.readLine())!=null){
					sb.append(read);
				}
				reader.close();
			}
		}catch(IOException ioe){
			LOG.debug("Error processing request stream: "+ioe.getMessage());
		}
			
		return sb.toString();
	}
	
	
	/**
	 * Fetch the data as Input stream
	 * @param u
	 * @return
	 */
	
	public static InputStream fetchDataAsStream(URL u){
		InputStream stream=null;
		boolean redirect=false;
		try{	
			if(!u.getProtocol().equalsIgnoreCase("https")){
			
				HttpURLConnection conn = (HttpURLConnection)u.openConnection();
				conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
				conn.connect();
				int status = conn.getResponseCode();
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				}
				if(redirect){
					String newUrl = conn.getHeaderField("Location");
					URL redirectUrl = new URL(newUrl);
					stream = fetchDataAsStream(redirectUrl);
				}else{
					if(status!= HttpURLConnection.HTTP_OK){
						stream = null;
					}else{
						stream = conn.getInputStream();
					}
				}
			}else{
				if(u.getProtocol().equalsIgnoreCase("https")){
					
					HttpsURLConnection conn = (HttpsURLConnection)u.openConnection();
					conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
					conn.connect();
					int status = conn.getResponseCode();
					if (status != HttpURLConnection.HTTP_OK) {
						if (status == HttpURLConnection.HTTP_MOVED_TEMP
							|| status == HttpURLConnection.HTTP_MOVED_PERM
								|| status == HttpURLConnection.HTTP_SEE_OTHER)
						redirect = true;
					}
					if(redirect){
						String newUrl = conn.getHeaderField("Location");
						URL redirectUrl = new URL(newUrl);
						stream = fetchDataAsStream(redirectUrl);
					}else{
						if(status!= HttpURLConnection.HTTP_OK){
							stream = null;
						}else{
							stream = conn.getInputStream();
						}
					}
					
				}else{
					URLConnection conn = u.openConnection();
					conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
					conn.connect();
					stream = conn.getInputStream();
					
				}
				
			}
			
		}catch(MalformedURLException mfe){
			LOG.debug("Malformed Url: "+u.getPath()+"\nException Received: "+mfe.getMessage());
		}catch(IOException ioe){
			LOG.debug("Error processing request stream: "+ioe.getMessage());
		}
			
		
		return stream;
	}
	
	/**
	 * Convert the data from the URL into a w3c document object
	 * @param url
	 * @return Document
	 */
	
	public static Document getDocmentFromUrl(String url){
		
		String content = fetchDataAsString(url);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try{ 
		    factory.setNamespaceAware(true);
		    builder = factory.newDocumentBuilder();
		    builder.setErrorHandler(new ErrorHandler() {
				
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					throw exception;
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					throw exception;					
				}
			});
		    Document doc = builder.parse(url);
		    
		    return doc;
		}catch(ParserConfigurationException pce){
			LOG.debug("Unable to create document: "+pce.getMessage());
			return null;
		}catch(IOException ioe){
			
			LOG.debug("Unable to create document: "+ioe.getMessage());
			return null;
		}catch(SAXException sxe){
			
			LOG.debug("Unable to create document: "+sxe.getMessage());
			return null;
		}
		
		
		
	}
	
public static Document getDocmentFromContent(String content){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try{ 
		    factory.setNamespaceAware(true);
		    builder = factory.newDocumentBuilder();
		    builder.setErrorHandler(new ErrorHandler() {
				
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					throw exception;
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					throw exception;					
				}
			});
		    Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
		    
		    return doc;
		}catch(ParserConfigurationException pce){
			LOG.debug("Unable to create document: "+pce.getMessage());
			return null;
		}catch(IOException ioe){
			
			LOG.debug("Unable to create document: "+ioe.getMessage());
			return null;
		}catch(SAXException sxe){
			
			LOG.debug("Unable to create document: "+sxe.getMessage());
			return null;
		}
		
		
		
	}
	
	/**
	 * Generate Document object from HTML document
	 * @param url
	 * @return Document
	 */
	
public static Document getHtmlDocmentFromUrl(String url){
		
		    String data = fetchDataAsString(url);
			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			tidy.setShowWarnings(false);
			tidy.setShowErrors(0);
			Document doc = tidy.parseDOM(new ByteArrayInputStream(data.getBytes()), null);
			return doc;
}

/**
 * Generate html document from stream
 * @param stream
 * @return
 */

public static Document getHtmlDocumentFromContent(InputStream stream){
	
	Tidy tidy = new Tidy();
	tidy.setXHTML(true);
	tidy.setShowWarnings(false);
	tidy.setShowErrors(0);
	Document doc = tidy.parseDOM(stream, null);
	return doc;
}
/**
 * Generate html document from string
 * @param content
 * @return
 */
public static Document getHtmlDocumentFromContent(String content){
	Document doc=null;
	try{
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);
		doc = tidy.parseDOM(new ByteArrayInputStream(content.getBytes()), null);
	}catch(Exception e){
		LOG.debug("Exception during document processing");
	}
	return doc;
}
 
/**
 * Generates the output document as a String
 * @param url
 * @param results
 * @param xPaths
 * @param stream
 * @return
 */

public static String generateOutputDocument(String url, boolean[] results, String[] xPaths,InputStream stream){
	
	StringBuilder sb = new StringBuilder();
	
	try{
		String data = readFileFromDisk(stream);
		data = data.replace(Constants.REPLACE_URL, url);
		data = data.replace(Constants.REPLACE_TABLE, generateTable(xPaths,results));
		return data;
	}catch(IOException ioe){
		LOG.debug("Unable to generate output: "+ioe.getMessage());
	}
				
	return sb.toString();
}

/**
 * Generates the table to be put in the output
 * @param xPaths
 * @param results
 * @return
 */

private static String generateTable(String[] xPaths, boolean[] results){
	
	StringBuilder sb = new StringBuilder();
	sb.append("<table><tr><th>XPath</th><th>IsValid</th></tr>");
	for(int i=0;i<xPaths.length;i++){
		sb.append("<tr><td>");
		sb.append(xPaths[i]);
		sb.append("</td><td>");
		sb.append(results[i]);
		sb.append("</td></tr>");
	}
	sb.append("</table>");
	
	return sb.toString();
}

/**
 * Reads the file from disk and gives out the content as String
 * @param f
 * @return
 * @throws FileNotFoundException
 */

private static String readFileFromDisk(File f)throws FileNotFoundException{
	StringBuffer sb = new StringBuffer();
	
	BufferedReader br = new BufferedReader(new FileReader(f));
	try{
		String read="";
		while((read = br.readLine())!=null){
			sb.append(read);
		}
		br.close();
	}catch(IOException ioe){
		LOG.debug("Unable to read file from disk "+ioe.getMessage());
	}finally{
		if(br != null){
			try{
				br.close();
			}catch(IOException ioe){
				
			}
		}
		
	}
	
	return sb.toString();
}

/**
 * Reads the gives stream file from disk and returns content as String
 * @param stream
 * @return
 * @throws FileNotFoundException
 */

private static String readFileFromDisk(InputStream stream)throws FileNotFoundException{
	StringBuffer sb = new StringBuffer();
	
	BufferedReader br = new BufferedReader(new InputStreamReader(stream));
	try{
		String read="";
		while((read = br.readLine())!=null){
			sb.append(read);
		}
		br.close();
	}catch(IOException ioe){
		LOG.debug("Unable to read file from disk "+ioe.getMessage());
	}finally{
		if(br != null){
			try{
				br.close();
			}catch(IOException ioe){
				
			}
		}
		
	}
	
	return sb.toString();
}

/**
 * Checks if the name of the Nodes/Attributes is Valid
 * @param name
 * @return boolean isValid
 */

public static boolean isValidName(String name){
	boolean isValid = false;
	
	Pattern pattern = Pattern.compile(TokenConstants.NODE_NAME_REGEX);
	Matcher match = pattern.matcher(name);
	
	if(match.matches()){
		if(!(name.startsWith("XML")||name.startsWith("Xml")||name.startsWith("xml"))){
			isValid= true;
		}
	}
	
	
	
	return isValid;
}
/**
 * Checks if the session is valid
 * @param session
 * @return boolean
 */

public static boolean isValidSession(HttpSession session){
	boolean isValid = false;
	Date curr = new Date();
	try{
		if(!((session.getLastAccessedTime()-curr.getTime())>session.getMaxInactiveInterval())){
			isValid = true;
		}
	}catch(Exception e){
		LOG.debug("Unexpected error during verification: "+e.getMessage());
	}
	
	return isValid;
}

/**
 * Return Headers retrieved from a Head request
 * @param u
 * @return
 */

public static Map<String,Object> fetchHeadHeaders(URL u, long modified){
	
	Map<String, List<String>> headers=null;
	Map<String,Object> retVal = new HashMap<String, Object>();
	boolean redirect=false;
	String redirectURL=null;
	try{
		if(!u.getProtocol().equalsIgnoreCase("https")){
		
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
			conn.setIfModifiedSince(modified);
			conn.setRequestMethod("HEAD");
			conn.connect();
			int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
			}
			if(redirect){
				String newUrl = conn.getHeaderField("Location");
				redirectURL = newUrl;
				URL redirectUrl = new URL(newUrl);
				retVal = fetchHeadHeaders(redirectUrl,modified);
			}else{
				headers = conn.getHeaderFields();
			}
		}else{
			if(u.getProtocol().equalsIgnoreCase("https")){
				
				HttpsURLConnection conn = (HttpsURLConnection)u.openConnection();
				HttpsURLConnection.setFollowRedirects(false);
				conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
				conn.setIfModifiedSince(modified);
				conn.setRequestMethod("HEAD");
				conn.connect();
				int status = conn.getResponseCode();
				
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				}
				if(redirect){
					String newUrl = conn.getHeaderField("Location");
					redirectURL = newUrl;
					URL redirectUrl = new URL(newUrl);
					retVal = fetchHeadHeaders(redirectUrl,modified);
				}else{
					headers = conn.getHeaderFields();
				}				
			}else{
				URLConnection conn = u.openConnection();
				conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
				conn.setRequestProperty(Constants.HEADER_MODIFIED_SINCE, String.valueOf(modified));
				conn.connect();
				headers = conn.getHeaderFields();
			}
			
		}
		retVal.put("Redirect", redirectURL);
		retVal.put("Headers", headers);
	}catch(MalformedURLException mfe){
		LOG.debug("Malformed Url: "+u.getPath()+"\nException Received: "+mfe.getMessage());
	}catch(IOException ioe){
		LOG.debug("Error processing request stream: "+ioe.getMessage());
	}
	return retVal;	
		
}

/**
 * Fetch the page content and headers and return as a Map
 * @param u
 * @return
 */

public static HashMap<String,Object> fetchPageAndHeaders(URL u){
	
	HashMap<String,Object> pageContent = new HashMap<String, Object>();
	Map<String, List<String>> headers = null;
	InputStream stream=null;
	Document doc=null;
	String content=null;
	String contentType=null;
	try{
		if(!u.getProtocol().equalsIgnoreCase("https")){
		
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
			conn.setRequestMethod("GET");
			conn.connect();
			headers = conn.getHeaderFields();
			contentType = conn.getContentType();
			stream = conn.getInputStream();
			
		}else{
			if(u.getProtocol().equalsIgnoreCase("https")){
				
				HttpsURLConnection conn = (HttpsURLConnection)u.openConnection();
				conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
				conn.setRequestMethod("GET");
				conn.connect();
				headers = conn.getHeaderFields();
				contentType = conn.getContentType();
				stream = conn.getInputStream();
				
			}else{
				URLConnection conn = u.openConnection();
				conn.setRequestProperty(Constants.USER_AGENT_STRING, Constants.DEFAULT_USER_AGENT);
				conn.connect();
				headers = conn.getHeaderFields();
				contentType = conn.getContentType();
				stream = conn.getInputStream();
			}
			
		}
		content = fetchDataAsString(stream);
		
		if(Utils.isHtmlType(contentType)){
			doc = getHtmlDocumentFromContent(content);
		}
		
		if(Utils.isXmlType(contentType)){
			doc = getDocmentFromContent(content);
		}
		
		}catch(MalformedURLException mfe){
			LOG.debug("Malformed Url: "+u.getPath()+"\nException Received: "+mfe.getMessage());
		}catch(IOException ioe){
			LOG.debug("Error processing request stream: "+ioe.getMessage());
		}catch(Exception e){
			LOG.debug("Unexpected exception: "+e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
	pageContent.put(Constants.SITE_HEADERS, headers);
	pageContent.put(Constants.SITE_CONTENT, content);
	pageContent.put(Constants.SITE_DOCUMENT, doc);
	
	return pageContent;
}

/**
 * Checks if the content type is an xml type
 * @param type
 * @return boolean isXml
 */

public static boolean isXmlType(String contentType){
	boolean isXml = false;
	if(contentType!=null){
		String type = contentType.split(";")[0];
		if(type.trim().equalsIgnoreCase(Constants.CONTENT_TYPE_TEXT_XML) || type.trim().equalsIgnoreCase(Constants.CONTENT_TYPE_APPLICATION_XML)){
			isXml = true;
		}else{
			if(type.trim().endsWith("+xml")){
				isXml = true;
			}
		}
	}
	return isXml;
}

/**
 * Checks if the content type is html
 */
public static boolean isHtmlType(String contentType){
	
	boolean isHtml=false;
	if(contentType!=null){
		String type = contentType.split(";")[0];
		if(type.trim().equalsIgnoreCase(Constants.CONTENT_TYPE_TEXT_HTML)){
			isHtml=true;
		}
	}
	return isHtml;
}

/**
 * Generate Unique primary key
 * @return String id
 */

public static String generateUniqueId(){
	
	String id = null;
	
	UID uuid = new UID();
	id = uuid.toString();
	
	return id;
}

/**
 * 
 * @return
 */

public static String getFormattedDate(){
	return getFormattedDate(new Date().getTime());
}

/**
 * 
 * @param time
 * @return
 */

public static String getFormattedDate(long time){
	String date="";
	
	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	date = sdf.format(new Date(time));
			
	return date;
}

/**
 * Returns formatted date for putting in channel xml
 * @param time
 * @return
 */

public static String getFormatterDateForXml(long time){
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	String date = sdf.format(new Date(time));
	
	return date;
}

/**
 * 
 * @param date
 * @return
 */

public static long getTimeFromString(String date){
	Date requestedDate = new Date();
	boolean fetched = false;
	try{
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		Date dt = sdf.parse(date);
		return dt.getTime();
	}catch(ParseException pe){
		LOG.debug("Unable to parse date:"+date+"\n"+pe.getMessage());
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
	try{
		requestedDate =	sdf.parse(date);
		fetched = true;
	}catch(ParseException pe){
		LOG.debug("Unable to parse date string");
	}
	if(!fetched){
		SimpleDateFormat format2 = new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss z");
		format2.setTimeZone(TimeZone.getTimeZone("GMT"));
		try{
			requestedDate = format2.parse(date);
			fetched = true;
		}catch(ParseException pe){
			LOG.debug("Unable to parse date string");
		}
	}
	if(!fetched){
		SimpleDateFormat format3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
		format3.setTimeZone(TimeZone.getTimeZone("GMT"));
		try{
			requestedDate = format3.parse(date);
			fetched = true;
		}catch(ParseException pe){
			LOG.debug("Unable to parse date string");
		}
	}
	
	return requestedDate.getTime();
}

/**
 * 
 * @param path
 * @param date
 * @return
 */

public static boolean isResourceModified(String path, String date){
	boolean modified = false;
	Date requestedDate = new Date();
	boolean fetched = false;
	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	
	
	try{
	requestedDate =	sdf.parse(date);
	fetched = true;
	}catch(ParseException pe){
		LOG.debug("Unable to parse date string");
	}
	if(!fetched){
		SimpleDateFormat format2 = new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try{
			requestedDate = format2.parse(date);
			fetched = true;
		}catch(ParseException pe){
			LOG.debug("Unable to parse date string");
		}
	}
	if(!fetched){
		SimpleDateFormat format3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try{
			requestedDate = format3.parse(date);
			fetched = true;
		}catch(ParseException pe){
			LOG.debug("Unable to parse date string");
		}
	}
	if(!fetched)
		return true;
	File f = new File(path);
	long dateMod =  f.lastModified();
	modified = dateMod > requestedDate.getTime();		
	return modified;
}


}
