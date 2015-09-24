package edu.upenn.cis455.storage.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import edu.upenn.cis455.crawler.info.RobotsTxtInfo;
/**
 * Entity for Robots information
 * @author cis455
 *
 */

@Entity
public class RobotsEntity {

	@PrimaryKey
	private String domainName;
	
	private RobotsTxtInfo robots;
	
	private long lastUpdatedDate;

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public RobotsTxtInfo getRobots() {
		return robots;
	}

	public void setRobots(RobotsTxtInfo robots) {
		this.robots = robots;
	}

	public long getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(long lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	
	
	
	
}
