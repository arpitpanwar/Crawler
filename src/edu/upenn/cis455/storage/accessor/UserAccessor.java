package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.entity.CrawlerEntity;
import edu.upenn.cis455.storage.entity.HTMLEntity;
import edu.upenn.cis455.storage.entity.UserEntity;

/**
 * Accessor for UserEntity
 * @author cis455
 *
 */
public class UserAccessor {
	
	private PrimaryIndex<String, UserEntity> primaryIndex;
	
	private SecondaryIndex<String, String, UserEntity> secondaryIndex;
	
	public UserAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, UserEntity.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex,String.class, "userEmail" );
		
	}

	public PrimaryIndex<String, UserEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	public SecondaryIndex<String, String, UserEntity> getSecondaryIndex() {
		return secondaryIndex;
	}
	
	public List<UserEntity> fetchAllEntities(){
		List<UserEntity> userAccessorList = new ArrayList<UserEntity>();
		EntityCursor<UserEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<UserEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				userAccessorList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return userAccessorList;
	}
	
	public UserEntity fetchEntityFromPrimaryKey(String primaryKey){
		
		UserEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
	public UserEntity fetchEntityFromSecondaryKey(String secondaryKey){
		
		UserEntity entity = this.secondaryIndex.get(secondaryKey);
		
		return entity;
	}

	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public UserEntity putEntity(UserEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}

	
}
