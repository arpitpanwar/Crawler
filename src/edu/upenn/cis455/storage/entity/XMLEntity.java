package edu.upenn.cis455.storage.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Class for XMl Information
 * @author cis455
 *
 */

@Entity
public class XMLEntity implements WebPageEntity{

	
	@PrimaryKey
	private String xmlPageId;
	
	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	private String xmlPageUrl;
	
	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	private String messageDigest;
	
	private String xmlDomain;
	private long xmlLastParsed;
	
	private long xmlLastUpdated;
	
	private boolean xmlParseBanned;
	
	private String xmlContent;

	public String getXmlPageId() {
		return xmlPageId;
	}

	public void setXmlPageId(String xmlPageId) {
		this.xmlPageId = xmlPageId;
	}

	public String getXmlPageUrl() {
		return xmlPageUrl;
	}

	public void setXmlPageUrl(String xmlPageUrl) {
		this.xmlPageUrl = xmlPageUrl;
	}

	public String getXmlDomain() {
		return xmlDomain;
	}

	public void setXmlDomain(String xmlDomain) {
		this.xmlDomain = xmlDomain;
	}

	public long getXmlLastParsed() {
		return xmlLastParsed;
	}

	public void setXmlLastParsed(long xmlLastParsed) {
		this.xmlLastParsed = xmlLastParsed;
	}

	public long getXmlLastUpdated() {
		return xmlLastUpdated;
	}

	public void setXmlLastUpdated(long xmlLastUpdated) {
		this.xmlLastUpdated = xmlLastUpdated;
	}

	public boolean isXmlParseBanned() {
		return xmlParseBanned;
	}

	public void setXmlParseBanned(boolean xmlParseBanned) {
		this.xmlParseBanned = xmlParseBanned;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public String getMessageDigest() {
		return messageDigest;
	}

	public void setMessageDigest(String messageDigest) {
		this.messageDigest = messageDigest;
	}
	
	
}
