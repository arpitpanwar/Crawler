package edu.upenn.cis455.xpathengine.utils;

/**
 * Constants for token generation including regex
 * @author cis455
 *
 */

public class TokenConstants {
	
	public static final String AXIS="/";
	
	public static final String TEXT = "text()";
	
	public static final String CONTAINS = "contains(";

	public static final String ATTRIBUTE = "@attname";
	
	public static final String ELEMENTTYPE_FUNCTION="Function";
	
	public static final String ELEMENTTYPE_ATTRIBUTE="Attribute";
	
	public static final String ELEMENTTYPE_NODE="Node";
	
	public static final String OPEN_BRACKET="[";
	
	public static final String CLOSING_BRACKET="]";
	
	public static final String TEXT_REGEX="^text\\(\\)\\s*=\\s*\"(.*?)\"";
	
	public static final String CONTAINS_REGEX="^contains\\(text\\(\\)\\s*,\\s*\"(.*?)\"\\s*\\)";
	
	public static final String ATTRIBUTE_REGEX="^@(\\w+)\\s*=\\s*\"(.*?)\"";
	
	public static final String NODE_NAME_REGEX="(^[A-Za-z][A-Za-z0-9-_.]*)";
		
}
