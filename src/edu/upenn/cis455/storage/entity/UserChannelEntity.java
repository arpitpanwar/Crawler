package edu.upenn.cis455.storage.entity;

import java.util.List;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Entity for User Channel information
 * @author cis455
 *
 */

@Entity
public class UserChannelEntity {

	@PrimaryKey
	private String userChannelId;
	
	@SecondaryKey(relate=Relationship.ONE_TO_ONE,onRelatedEntityDelete=DeleteAction.CASCADE,relatedEntity=UserEntity.class)
	private String userId;
	
	private List<String> channelId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getChannelId() {
		return channelId;
	}

	public void setChannelId(List<String> channelId) {
		this.channelId = channelId;
	}

	public String getUserChannelId() {
		return userChannelId;
	}

	public void setUserChannelId(String userChannelId) {
		this.userChannelId = userChannelId;
	}
	
}
