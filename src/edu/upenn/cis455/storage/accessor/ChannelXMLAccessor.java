package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.entity.ChannelXMLEntity;

/**
 * Accessor for ChannelXMlEntity
 * @author cis455
 *
 */

public class ChannelXMLAccessor {
	
	private PrimaryIndex<String, ChannelXMLEntity> primaryIndex;
	private SecondaryIndex<List, String, ChannelXMLEntity> xmlPageIndex;

	
	public ChannelXMLAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, ChannelXMLEntity.class);
		xmlPageIndex = store.getSecondaryIndex(primaryIndex,List.class, "xmlPageID" );

	}

	public PrimaryIndex<String, ChannelXMLEntity> getPrimaryIndex() {
		return primaryIndex;
	}


		
	public SecondaryIndex<List, String, ChannelXMLEntity> getXMLIndex() {
		return xmlPageIndex;
	}
	
	
	public List<ChannelXMLEntity> fetchAllEntities(){
		List<ChannelXMLEntity> crawlerList = new ArrayList<ChannelXMLEntity>();
		EntityCursor<ChannelXMLEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<ChannelXMLEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				crawlerList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return crawlerList;
	}
	
	public ChannelXMLEntity fetchEntityFromPrimaryKey(String primaryKey){
		ChannelXMLEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
		
	public ChannelXMLEntity fetchEntityFromXMLKey(List<String> messageDigest){
		ChannelXMLEntity entity = this.xmlPageIndex.get(messageDigest);
		return entity;
	}
	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public ChannelXMLEntity putEntity(ChannelXMLEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}
}
