package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.entity.ChannelEntity;

/**
 * Accessor class for Channel Entity
 * @author cis455
 *
 */
public class ChannelAccessor {
	
	private PrimaryIndex<String, ChannelEntity> primaryIndex;
	
	private SecondaryIndex<String, String, ChannelEntity> secondaryIndex;
	
	public ChannelAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, ChannelEntity.class);
		
	}

	public PrimaryIndex<String, ChannelEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	public SecondaryIndex<String, String, ChannelEntity> getSecondaryIndex() {
		return secondaryIndex;
	}
	
	public List<ChannelEntity> fetchAllEntities(){
		List<ChannelEntity> channelList = new ArrayList<ChannelEntity>();
		EntityCursor<ChannelEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<ChannelEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				channelList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return channelList;
	}
	
	public ChannelEntity fetchEntityFromPrimaryKey(String primaryKey){
		
		ChannelEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
	
	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public ChannelEntity putEntity(ChannelEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}
	
	
	
	

}
