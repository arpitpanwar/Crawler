package edu.upenn.cis455.crawler;

import edu.upenn.cis455.xpathengine.model.Launcher;
import edu.upenn.cis455.xpathengine.utils.StringConstants;


public class XPathCrawler {
	
	public static void main(String[] args){
		
		if(args.length<3){
			System.out.println(StringConstants.INVALID+StringConstants.USAGE);
			System.exit(1);
		}else{
			Launcher launch = new Launcher(args);
			if(!launch.validate()){
				System.out.println(StringConstants.INVALID+StringConstants.USAGE);
				System.exit(1);
			}else{
				
				new CrawlerRunner(launch).launchCrawler();
				
			}
		}
		
		
	}
	
	
	
	
	
}
