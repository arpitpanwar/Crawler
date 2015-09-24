package edu.upenn.cis455.xpathengine.model;


import java.util.List;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.ChannelAccessor;
import edu.upenn.cis455.storage.accessor.UserChannelAccessor;
import edu.upenn.cis455.storage.entity.UserChannelEntity;
import edu.upenn.cis455.storage.entity.UserEntity;
import edu.upenn.cis455.storage.entity.ChannelEntity;

/**
 * Utility clas for fetching user related questions
 * @author cis455
 *
 */
public class UserInfo {
	
	private UserEntity entity;
	
	public UserInfo(UserEntity entity)
	{
		this.entity = entity;
	}

	/*
	public List<ChannelEntity> getUserChannels(){
		
		UserChannelAccessor accessor = new UserChannelAccessor(DBWrapper.getStore());
		
		return accessor.fetchChannelsForUser(this.entity.getUserId());
		
	}
	
	*/
	
	/**
	 * Check if the channel is a valid channel for the user
	 * @param channelId
	 * @return
	 */
	
	public boolean isUserChannelPresent(String channelId){
		boolean isPresent = true;
		
		UserChannelAccessor accessor = new UserChannelAccessor(DBWrapper.getStore());
		
		UserChannelEntity userChannelentity = accessor.fetchEntityFromSecondaryKey(this.entity.getUserId());
		
		if(userChannelentity!=null){
			
			List<String> channelIds = userChannelentity.getChannelId();
			
			if(channelIds!=null){
				
				if(!channelIds.contains(channelId)){
					isPresent = false;
				}
				
				
			}else{
				isPresent=false;
			}
			
		}
			
		return isPresent;
		
	}
	
	/**
	 * Delete the channel from the user's channels
	 * @param channelId
	 * @return boolean isDeleted
	 */
	
	public boolean deleteChannel(String channelId){
		boolean isDeleted = false;
		
		UserChannelAccessor accessor = new UserChannelAccessor(DBWrapper.getStore());
		
		UserChannelEntity userChannelentity = accessor.fetchEntityFromSecondaryKey(this.entity.getUserId());
		
		ChannelAccessor channelAccessor = new ChannelAccessor(DBWrapper.getStore());
		
		if(userChannelentity!=null){
			
			List<String> channelIds = userChannelentity.getChannelId();
			
			if(channelIds!=null){
				
				if(channelIds.contains(channelId)){
					
					ChannelEntity chEntity = channelAccessor.fetchEntityFromPrimaryKey(channelId);
					
					if(chEntity!=null){
						
						int index = channelIds.indexOf(channelId);
						channelIds.remove(index);
						userChannelentity.setChannelId(channelIds);
						accessor.putEntity(userChannelentity);
						channelAccessor.deleteEntity(chEntity.getChannelId());
						isDeleted = true;
					}
				}
				
				
			}			
		}
				
		return isDeleted;
	}
	

}
