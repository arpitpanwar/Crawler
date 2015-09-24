package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.entity.CrawlerEntity;
import edu.upenn.cis455.storage.entity.RobotsEntity;

/**
 * Accessor for Robots Entity
 * @author cis455
 *
 */
public class RobotsAccessor {
	
private PrimaryIndex<String, RobotsEntity> primaryIndex;
	

	public RobotsAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, RobotsEntity.class);
		
	}

	public PrimaryIndex<String, RobotsEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	
	public List<RobotsEntity> fetchAllEntities(){
		List<RobotsEntity> robotsList = new ArrayList<RobotsEntity>();
		EntityCursor<RobotsEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<RobotsEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				robotsList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return robotsList;
	}
	
	public RobotsEntity fetchEntityFromPrimaryKey(String primaryKey){
		
		RobotsEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
		
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public RobotsEntity putEntity(RobotsEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}

}
