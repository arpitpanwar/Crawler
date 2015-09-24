package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.entity.ChannelEntity;
import edu.upenn.cis455.storage.entity.CrawlerEntity;
import edu.upenn.cis455.storage.entity.UserChannelEntity;
import edu.upenn.cis455.storage.entity.UserEntity;
/**
 * Accessor for UserChannelEntity
 * @author cis455
 *
 */
public class UserChannelAccessor {
	
private PrimaryIndex<String, UserChannelEntity> primaryIndex;
	
	private SecondaryIndex<String, String, UserChannelEntity> secondaryIndex;
	
	public UserChannelAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, UserChannelEntity.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex,String.class, "userId" );
		
	}

	public PrimaryIndex<String, UserChannelEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	public SecondaryIndex<String, String, UserChannelEntity> getSecondaryIndex() {
		return secondaryIndex;
	}
	
	public List<UserChannelEntity> fetchAllEntities(){
		List<UserChannelEntity> userChannelList = new ArrayList<UserChannelEntity>();
		EntityCursor<UserChannelEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<UserChannelEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				userChannelList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return userChannelList;
	}
	
	public UserChannelEntity fetchEntityFromPrimaryKey(String primaryKey){
		
		UserChannelEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
	public UserChannelEntity fetchEntityFromSecondaryKey(String secondaryKey){
		
		UserChannelEntity entity = this.secondaryIndex.get(secondaryKey);
		
		return entity;
	}
	
	
	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	
	
	public UserChannelEntity putEntity(UserChannelEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}
	
}
