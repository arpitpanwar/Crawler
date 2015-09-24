package edu.upenn.cis455.storage.accessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.entity.CrawlerEntity;
import edu.upenn.cis455.storage.entity.HTMLEntity;
import edu.upenn.cis455.storage.entity.UserChannelEntity;
import edu.upenn.cis455.storage.entity.XMLEntity;

/**
 * Accessor for XMlEntity
 * @author cis455
 *
 */
public class XMLAccessor {

	
private PrimaryIndex<String, XMLEntity> primaryIndex;
	
	private SecondaryIndex<String, String, XMLEntity> secondaryIndex;
	
	private SecondaryIndex<String, String, XMLEntity> digestIndex;

	
	public XMLAccessor(EntityStore store){
		
		if(store==null){
			store = DBWrapper.getStore();
		}
		
		primaryIndex = store.getPrimaryIndex(String.class, XMLEntity.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex,String.class, "xmlPageUrl" );
		digestIndex = store.getSecondaryIndex(primaryIndex,String.class, "messageDigest" );

	}

	public PrimaryIndex<String, XMLEntity> getPrimaryIndex() {
		return primaryIndex;
	}


	public SecondaryIndex<String, String, XMLEntity> getSecondaryIndex() {
		return secondaryIndex;
	}
	
	public SecondaryIndex<String, String, XMLEntity> getDigestIndex() {
		return digestIndex;
	}
	
	
	public List<XMLEntity> fetchAllEntities(){
		List<XMLEntity> crawlerList = new ArrayList<XMLEntity>();
		EntityCursor<XMLEntity> channel_cursor = this.primaryIndex.entities();
		
		try{
			
			Iterator<XMLEntity> iter = channel_cursor.iterator();
			
			while(iter.hasNext()){
				
				crawlerList.add(iter.next());
				
			}
			
		}finally{
			channel_cursor.close();
		}
		
		
		return crawlerList;
	}
	
	public XMLEntity fetchEntityFromPrimaryKey(String primaryKey){
		XMLEntity entity = this.primaryIndex.get(primaryKey);
				
		return entity;
	}
	
	public XMLEntity fetchEntityFromSecondaryKey(String secondaryKey){
		XMLEntity entity = this.secondaryIndex.get(secondaryKey);
		
		return entity;
	}
	
	public XMLEntity fetchEntityFromDigestKey(String messageDigest){
		XMLEntity entity = this.digestIndex.get(messageDigest);
		return entity;
	}
	
	public boolean deleteEntity(String primaryKey){
		return this.primaryIndex.delete(primaryKey);
	}
	
	public XMLEntity putEntity(XMLEntity entity){
		
		return this.primaryIndex.put(entity);
		
	}
	
}
