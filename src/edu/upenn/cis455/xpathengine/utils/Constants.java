package edu.upenn.cis455.xpathengine.utils;

/**
 * File containing some static constants
 * @author cis455
 *
 */

public class Constants {
	
	public static String USER_AGENT_STRING="User-Agent";
	
	
	public static String DEFAULT_USER_AGENT="cis455crawler";
	
	public static String UNIVERSAL_USER_AGENT="*";

	public static final String REPLACE_URL="%%REPLACE URL HERE%%";
	
	public static final String REPLACE_TABLE="%%REPLACE TABLE HERE%%";
	
	public static final String ENTITY_STORE_NAME="Crawler_Entity_Store";
	
	public static final String LOGIN_USER_NAME="username";
	
	public static final String LOGIN_PASSWORD="password";
	
	public static final String COOKIE_SESSION="JSessionId";
	
	public static final String ROBOTS_URL="/robots.txt";
	
	public static final String SITE_HEADERS="Headers";
	
	public static final String SITE_CONTENT="Content";
	
	public static final String SITE_DOCUMENT="Document";
	
	public static final String LINK_INFO_LINKS="Links";
	
	public static final String LINK_INFO_SIZE="Size";
	
	public static final String CONTENT_TYPE_TEXT_XML="text/xml";
	
	public static final String CONTENT_TYPE_APPLICATION_XML="application/xml";
	
	public static final String CONTENT_TYPE_TEXT_HTML="text/html";
	
	public static final String HEADER_MODIFIED_SINCE="If-modified-since";
	
	public static final String SERVLET_STORE_PARAM="BDBstore";
	
	public static final String USER_TYPE_GENERAL="GeneralUser";
	
	public static final String USER_TYPE_ADMIN="AdminUser";
	
	public static final String CHARSET=";charset=UTF-8";
	
	
	public static final long ROBOTS_FETCH_DELAY=864000;
	
	public static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE;


	public static final int MAX_THREAD_COUNT = 20;
	
	
}
