package edu.upenn.cis455.xpathengine.utils;

public class StringConstants {
	
	public static final String USAGE="Usage:\n"+
	"java -jar crawler.jar [URL] [Directory] [Size]\n"+
	"URL: URL of the page to start crawling from\n"+
	"Directory: Directory containing the berkeley db\n"+
	"Size: Maximum size of the document to retrieve";
	
	public static final String INVALID="Invalid number of argument\n";
	
	public static final String ROBOTS_USER_AGENT="User-agent";
	
	public static final String ROBOTS_DISALLOW="Disallow";
	
	public static final String ROBOTS_ALLOW="Allow";
	
	public static final String ROBOTS_SITEMAP="Sitemap";
	
	public static final String ROBOTS_CRAWL_DELAY="Crawl-delay";
	
	public static final String TABLE_HEADER="<table class=\"channelTable\"><tr><th>S.No.</th><th>Channel Name</th></tr>";

	public static final String USER_TABLE_HEADER="<table class=\"channelTable\"><tr><th>S.No.</th><th>Channel Name</th><th>Action</th></tr>";

	public static final String DELETE = "Delete";
	
	public static final String XML_HEADER="<?xml version=\"1.0\" charset=\"utf-8\"?>";
	
	public static final String XML_STYLESHEET_START="<?xml-stylesheet href=\"";
	
	public static final String XML_STYLESHEET_END="\"?>";
	
	public static final String CHANNEL_START_TAG="<documentcollection>";
	
	public static final String CHANNEL_END_TAG="</documentcollection>";
	
	public static final String DOCUMENT_START_TAG="<document>";
	
	public static final String DOCUMENT_END_TAG="</document>";
	
	public static final String DOCUMENT_CRAWLED_START_TAG="<crawled>";
	
	public static final String DOCUMENT_CRAWLED_END_TAG="</crawled>";

	public static final String DOCUMENT_LOCATION_START_TAG="<location>";

	public static final String DOCUMENT_LOCATION_END_TAG="</location>";
	
	public static final String REPLACE_CHANNEL_TABLE="%%%Insert Table here%%%";
	
}
