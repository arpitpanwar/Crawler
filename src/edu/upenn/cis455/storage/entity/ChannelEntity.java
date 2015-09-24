package edu.upenn.cis455.storage.entity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Entity for Channel information
 * @author cis455
 *
 */

@Entity
public class ChannelEntity {

	@PrimaryKey
	private String channelId;
	
	
	private String channelName;
	
	private String xPathExpressions;
	
	private String styleSheet;
	
	private String channelCreatedBy;
	
	private long channelLastUpdated;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelCreatedBy() {
		return channelCreatedBy;
	}

	public void setChannelCreatedBy(String channelCreatedBy) {
		this.channelCreatedBy = channelCreatedBy;
	}

	public long getChannelLastUpdated() {
		return channelLastUpdated;
	}

	public void setChannelLastUpdated(long channelLastUpdated) {
		this.channelLastUpdated = channelLastUpdated;
	}

	public String getxPathExpressions() {
		return xPathExpressions;
	}

	public void setxPathExpressions(String xPathExpressions) {
		this.xPathExpressions = xPathExpressions;
	}

	public String getStyleSheet() {
		return styleSheet;
	}

	public void setStyleSheet(String styleSheet) {
		this.styleSheet = styleSheet;
	}
	
	
}
