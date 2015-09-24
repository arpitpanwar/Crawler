package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.entity.HTMLEntity;
import edu.upenn.cis455.storage.entity.VisitedListEntity;

/**
 * Accessor for VisitedListEntity
 * @author cis455
 *
 */
public class VisitedListAccessor {
	
private PrimaryIndex<String, VisitedListEntity> primaryIndex;
	
	
	
	public VisitedListAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, VisitedListEntity.class);
		
	}

	public PrimaryIndex<String, VisitedListEntity> getPrimaryIndex() {
		return primaryIndex;
	}

	
	public List<VisitedListEntity> fetchAllEntities(){
		List<VisitedListEntity> crawlerList = new ArrayList<VisitedListEntity>();
		EntityCursor<VisitedListEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<VisitedListEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				crawlerList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return crawlerList;
	}
	
	public VisitedListEntity fetchEntityFromPrimaryKey(String primaryKey){
		VisitedListEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
		
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public VisitedListEntity putEntity(VisitedListEntity entity){
		return this.primaryIndex.put(entity);
	}

}
