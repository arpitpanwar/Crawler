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
import edu.upenn.cis455.storage.entity.WebPageEntity;
import edu.upenn.cis455.storage.entity.XMLEntity;

/**
 * Accessor class for HTMlEntity
 * @author cis455
 *
 */

public class HTMLAccessor{
	
private PrimaryIndex<String, HTMLEntity> primaryIndex;
	
	private SecondaryIndex<String, String, HTMLEntity> secondaryIndex;
	
	private SecondaryIndex<String, String, HTMLEntity> digestIndx;
	
	public HTMLAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, HTMLEntity.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex,String.class, "htmlPageUrl" );
		digestIndx = store.getSecondaryIndex(primaryIndex,String.class, "messageDigest" );
		
	}

	public PrimaryIndex<String, HTMLEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	public SecondaryIndex<String, String, HTMLEntity> getSecondaryIndex() {
		return secondaryIndex;
	}
	
	public SecondaryIndex<String, String, HTMLEntity> getDigestIndex() {
		return digestIndx;
	}
	
	public List<HTMLEntity> fetchAllEntities(){
		List<HTMLEntity> crawlerList = new ArrayList<HTMLEntity>();
		EntityCursor<HTMLEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<HTMLEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				crawlerList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return crawlerList;
	}
	
	public HTMLEntity fetchEntityFromPrimaryKey(String primaryKey){
		HTMLEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
	public HTMLEntity fetchEntityFromSecondaryKey(String secondaryKey){
		HTMLEntity entity = this.secondaryIndex.get(secondaryKey);
		
		return entity;
	}
	
	public HTMLEntity fetchEntityFromDigestKey(String messageDigest){
		HTMLEntity entity = this.digestIndx.get(messageDigest);
		
		return entity;
	}
	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public HTMLEntity putEntity(HTMLEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}
	
}
