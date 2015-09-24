package edu.upenn.cis455.xpathengine.model;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Model class for launching the crawler
 * @author cis455
 *
 */

public class Launcher {
	static final Logger LOG = Logger.getLogger(Launcher.class);
	
	
	private String[] arguments;
	
	private String baseUrl;
	private String databaseDirectory;
	private String maxsize;
	
	
	public Launcher(String[] args){
		
		this.arguments = args;		
	}
	
	
	public boolean validate(){
		boolean isValid = false;
		try{
			String url = this.arguments[0];
			
			URL u = new URL(url);
			
			String directory = this.arguments[1];
			
			File f = new File(directory);
			f.getCanonicalPath();
			
			String size = this.arguments[2];
			
			Double.parseDouble(size);
			
			isValid = true;
			setBaseUrl(url);
			setDatabaseDirectory(directory);
			setMaxsize(size);
		}catch(Exception e){
			LOG.debug("Unexpected exception: "+e.getMessage());
		}
		
		return isValid;
		
	}


	public String getBaseUrl() {
		return baseUrl;
	}


	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public String getDatabaseDirectory() {
		return databaseDirectory;
	}


	public void setDatabaseDirectory(String databaseDirectory) {
		this.databaseDirectory = databaseDirectory;
	}


	public String getMaxsize() {
		return maxsize;
	}


	public void setMaxsize(String maxsize) {
		this.maxsize = maxsize;
	}
	
	

}
