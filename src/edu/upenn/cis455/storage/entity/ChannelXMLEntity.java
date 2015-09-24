package edu.upenn.cis455.storage.entity;

import java.util.List;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Entity for ChannelXML Data
 * @author cis455
 *
 */

@Entity
public class ChannelXMLEntity {
		
	@PrimaryKey
	private String channelId;
	
	@SecondaryKey(relate=Relationship.MANY_TO_MANY,onRelatedEntityDelete=DeleteAction.CASCADE)
	private List<String> xmlPageID;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List<String> getXmlPageID() {
		return xmlPageID;
	}

	public void setXmlPageID(List<String> xmlPageID) {
		this.xmlPageID = xmlPageID;
	}

}
