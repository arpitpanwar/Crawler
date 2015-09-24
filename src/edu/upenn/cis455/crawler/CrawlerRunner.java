package edu.upenn.cis455.crawler;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.HTMLAccessor;
import edu.upenn.cis455.storage.accessor.RobotsAccessor;
import edu.upenn.cis455.storage.accessor.VisitedListAccessor;
import edu.upenn.cis455.storage.accessor.XMLAccessor;
import edu.upenn.cis455.storage.entity.VisitedListEntity;
import edu.upenn.cis455.xpathengine.model.Launcher;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.pool.ThreadPool;

/**
 * Class which launches the crawlers
 * @author cis455
 *
 */


public class CrawlerRunner {
	static final Logger LOG = Logger.getLogger(CrawlerRunner.class);
	
	private Launcher launcher;
	private ThreadPool pool;
	private XMLAccessor xmlAccessor;
	private HTMLAccessor htmlAccessor;
	private VisitedListAccessor visitedAccessor;
	private RobotsAccessor robAccessor;
	
	public CrawlerRunner(Launcher launcher){
			this.launcher = launcher;
			
			xmlAccessor = new XMLAccessor(new DBWrapper(launcher.getDatabaseDirectory()).getStore());
			htmlAccessor = new HTMLAccessor(DBWrapper.getStore());
			visitedAccessor = new VisitedListAccessor(DBWrapper.getStore());
			robAccessor = new RobotsAccessor(DBWrapper.getStore());
			this.pool = new ThreadPool(Constants.MAX_THREAD_COUNT,visitedAccessor,xmlAccessor,htmlAccessor,Long.parseLong(launcher.getMaxsize())*1000000,robAccessor);
	}	
	
	/**
	 * Launch crawler
	 */
	
	public void launchCrawler(){
		
		
		 
	try{	
		XPathMatcher matcher = new XPathMatcher();
		
		
		CrawlerManager manager = new CrawlerManager(launcher.getBaseUrl());
		manager.start();
		
		while(!this.pool.isShutdownRequested()){
			matcher.match();
			Thread.sleep(2000);
		}
		DBWrapper.closeEnvironment();
		this.pool.shutDown();
		
		//matcher.setShutdown(true);
		
		/*
		Crawler crawl = new Crawler("https://dbappserv.cis.upenn.edu/crawltest/nytimes/Africa.xml", xmlAccessor, htmlAccessor, Long.parseLong(launcher.getMaxsize()));
		Object retval = crawl.call();
		if(retval instanceof HashMap<?,?>){
			HashMap<String,Object> returnedMap = (HashMap<String,Object>)retval;
		}
		*/
	}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Inner class for providing the seed and launching the crawler
	 * @author cis455
	 *
	 */
	
	
	class CrawlerManager{
		
		private String baseUrl;
		
		public CrawlerManager(String baseUrl){
				this.baseUrl = baseUrl;	
			
		}
		
		public void start(){
			try{
				clearVisitedLinks();
				Crawler crawler = new Crawler(this.baseUrl, xmlAccessor, htmlAccessor, Long.parseLong(launcher.getMaxsize())*1000000,robAccessor);
				pool.execute(crawler);
			}catch(Exception e){
				
			} 
		}
		
		/**
		 * Clear all the visited links from the database
		 */
		
		private void clearVisitedLinks(){
			System.out.println("Clearing old visited links");
			List<VisitedListEntity> ent = visitedAccessor.fetchAllEntities();
			for(VisitedListEntity entity:ent){
				
				visitedAccessor.deleteEntity(entity.getPageUrl());
			}
		}
		
		
	}	
	
	

}
