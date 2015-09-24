package edu.upenn.cis455.storage.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * Entity for storing visited links
 * @author cis455
 *
 */

@Entity
public class VisitedListEntity {
 
	@PrimaryKey
	private String pageUrl;
	
	private long visitedTime;

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public long getVisitedTime() {
		return visitedTime;
	}

	public void setVisitedTime(long visitedTime) {
		this.visitedTime = visitedTime;
	}
	
	
	
	
}
