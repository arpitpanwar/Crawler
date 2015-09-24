package edu.upenn.cis455.storage.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;


/**
 * Entity for HTMl Page information
 * @author cis455
 *
 */

@Entity
public class HTMLEntity implements WebPageEntity {
	
	@PrimaryKey
	private String htmlPageId;
	
	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	private String htmlPageUrl;
	
	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	private String messageDigest;
	
	private String htmlDomain;
	private long htmlLastParsed;
	
	private long htmlLastUpdated;
	
	private boolean htmlParseBanned;
	
	private String htmlContent;

	public String getHtmlPageId() {
		return htmlPageId;
	}

	public void setHtmlPageId(String htmlPageId) {
		this.htmlPageId = htmlPageId;
	}

	public String getHtmlPageUrl() {
		return htmlPageUrl;
	}

	public void setHtmlPageUrl(String htmlPageUrl) {
		this.htmlPageUrl = htmlPageUrl;
	}

	public String getHtmlDomain() {
		return htmlDomain;
	}

	public void setHtmlDomain(String htmlDomain) {
		this.htmlDomain = htmlDomain;
	}

	public long getHtmlLastParsed() {
		return htmlLastParsed;
	}

	public void setHtmlLastParsed(long htmlLastParsed) {
		this.htmlLastParsed = htmlLastParsed;
	}

	public long getHtmlLastUpdated() {
		return htmlLastUpdated;
	}

	public void setHtmlLastUpdated(long htmlLastUpdated) {
		this.htmlLastUpdated = htmlLastUpdated;
	}

	public boolean isHtmlParseBanned() {
		return htmlParseBanned;
	}

	public void setHtmlParseBanned(boolean htmlParseBanned) {
		this.htmlParseBanned = htmlParseBanned;
	}

	public String isHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getMessageDigest() {
		return messageDigest;
	}

	public void setMessageDigest(String messageDigest) {
		this.messageDigest = messageDigest;
	}
	

}
