package test.edu.upenn.cis455.JUnitCases;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import edu.upenn.cis455.crawler.Crawler;
import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.HTMLAccessor;
import edu.upenn.cis455.storage.accessor.RobotsAccessor;
import edu.upenn.cis455.storage.accessor.XMLAccessor;
import edu.upenn.cis455.xpathengine.utils.Constants;

public class CrawlerTest {

	private long size;
	private String URL;
	private URL u;
	private XMLAccessor xmlAccessor;
	private HTMLAccessor htmlAccessor;
	private RobotsAccessor robotAccessor;
	private List<String> parsedLinks;
	private long parsedSize=0;
	
	@Test
	public void testProcessLink() {
		try{
			this.URL = "http://www.google.com";
			this.xmlAccessor = new XMLAccessor(new DBWrapper("dbdir").getStore());
			this.htmlAccessor = new HTMLAccessor(DBWrapper.getStore());
			this.robotAccessor=new RobotsAccessor(DBWrapper.getStore());
			this.size = 100000;
			
			Crawler crawl = new Crawler(URL, xmlAccessor, htmlAccessor, size, robotAccessor);
			
			HashMap<String, Object> results = crawl.processLink();
			
			Long val = (Long)results.get(Constants.LINK_INFO_SIZE);

			this.parsedLinks = (List<String>)results.get(Constants.LINK_INFO_LINKS);
			
			assertEquals(20,this.parsedLinks.size());
			
			
		
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
