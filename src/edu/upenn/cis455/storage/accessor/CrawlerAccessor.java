package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.entity.CrawlerEntity;

/**
 * Accessor for Crawler Class
 * @author cis455
 *
 */


public class CrawlerAccessor {

	
	private PrimaryIndex<String, CrawlerEntity> primaryIndex;
	
	private SecondaryIndex<String, String, CrawlerEntity> secondaryIndex;
	
	public CrawlerAccessor(EntityStore store){
		
		primaryIndex = store.getPrimaryIndex(String.class, CrawlerEntity.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex,String.class, "crawlerDomain" );
		
	}

	public PrimaryIndex<String, CrawlerEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	public SecondaryIndex<String, String, CrawlerEntity> getSecondaryIndex() {
		return secondaryIndex;
	}
	
	public List<CrawlerEntity> fetchAllEntities(){
		List<CrawlerEntity> crawlerList = new ArrayList<CrawlerEntity>();
		EntityCursor<CrawlerEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<CrawlerEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				crawlerList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return crawlerList;
	}
	
	public CrawlerEntity fetchEntityFromPrimaryKey(String primaryKey){
		
		CrawlerEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
	public CrawlerEntity fetchEntityFromSecondaryKey(String secondaryKey){
		
		CrawlerEntity entity = this.secondaryIndex.get(secondaryKey);
		
		return entity;
	}
	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public CrawlerEntity putEntity(CrawlerEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}
	
}
