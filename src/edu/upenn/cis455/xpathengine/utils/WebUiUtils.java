package edu.upenn.cis455.xpathengine.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;

import edu.upenn.cis455.storage.accessor.ChannelAccessor;
import edu.upenn.cis455.storage.accessor.UserAccessor;
import edu.upenn.cis455.storage.accessor.UserChannelAccessor;
import edu.upenn.cis455.storage.entity.ChannelEntity;
import edu.upenn.cis455.storage.entity.UserChannelEntity;
import edu.upenn.cis455.storage.entity.UserEntity;

public class WebUiUtils {
	
	public static String generateAllChannelsTable(EntityStore store){
		
		StringBuilder sb = new StringBuilder();
		
		ChannelAccessor accessor = new ChannelAccessor(store);
		
		EntityCursor<ChannelEntity> entity_cursor = accessor.getPrimaryIndex().entities();
		
		Iterator<ChannelEntity> it = entity_cursor.iterator();
		
		sb.append(StringConstants.TABLE_HEADER);
		int ctr=1;
		while(it.hasNext()){
			
			ChannelEntity entity = it.next();
			
			sb.append("<tr><td>");
			sb.append(ctr++);
			sb.append("</td><td>");
			sb.append("<a href=\"getchannel?id=");
			sb.append(entity.getChannelId());
			sb.append(">");
			sb.append(entity.getChannelName());
			sb.append("</a>");
			sb.append("</td></tr>");
		}
		entity_cursor.close();
		sb.append("</table>");	
		return sb.toString();
	}
	
	public static boolean isEmailPresent(String email , EntityStore store){
		boolean isPresent = true;
		
		UserAccessor accessor = new UserAccessor(store);
		
		UserEntity entity =accessor.fetchEntityFromSecondaryKey(email);
		
		if(entity==null){
			isPresent=false;
		}
		
		return isPresent;
	}
	
	public static String generateUserTableInfo(String userId,EntityStore store){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(StringConstants.USER_TABLE_HEADER);
		int ctr=1;
		
		UserChannelAccessor accessor = new UserChannelAccessor(store);
		
		ChannelAccessor channelAccessor = new ChannelAccessor(store);
		
		UserChannelEntity userChannelEnt = accessor.fetchEntityFromSecondaryKey(userId);
		
		if(userChannelEnt!=null){
		
			List<String> channelIds = userChannelEnt.getChannelId();
			
			for(String id: channelIds){
					ChannelEntity currChannel = channelAccessor.fetchEntityFromPrimaryKey(id);
					if(currChannel!=null){	
						sb.append("<tr><td>");
						sb.append(ctr++);
						sb.append("</td><td>");
						sb.append("<a href=\"getchannel?id=");
						sb.append(currChannel.getChannelId());
						sb.append("\">");
						sb.append(currChannel.getChannelName());
						sb.append("</a>");
						sb.append("</td><td>");
						sb.append("<a href=\"deletechannel?id=");
						sb.append(currChannel.getChannelId());
						sb.append("\">");
						sb.append(StringConstants.DELETE);
						sb.append("</td></tr>");
					}	
			}
			
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	public static InputStream generateChannelStream(String userId , String channelId){
		
		InputStream stream=null;
		
		
		
		
		
		return stream;
	}
	
	

}
