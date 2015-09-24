package edu.upenn.cis455.crawler;

import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.ChannelAccessor;
import edu.upenn.cis455.storage.accessor.ChannelXMLAccessor;
import edu.upenn.cis455.storage.accessor.XMLAccessor;
import edu.upenn.cis455.storage.entity.ChannelEntity;
import edu.upenn.cis455.storage.entity.ChannelXMLEntity;
import edu.upenn.cis455.storage.entity.XMLEntity;
import edu.upenn.cis455.xpathengine.XPathEngine;
import edu.upenn.cis455.xpathengine.XPathEngineFactory;
import edu.upenn.cis455.xpathengine.utils.Utils;

/**
 * Class for matching the xpaths with the xml documents
 * @author cis455
 *
 */


public class XPathMatcher {
	
	private ChannelAccessor channelAccessor;
	private ChannelXMLAccessor channelXMLAccessor;
	private XMLAccessor xmlAccessor;
	private boolean shutdown=false;
	
	public XPathMatcher(){
			channelAccessor = new ChannelAccessor(DBWrapper.getStore());
			channelXMLAccessor = new ChannelXMLAccessor(DBWrapper.getStore());
			xmlAccessor = new XMLAccessor(DBWrapper.getStore());
	}
	
	public void setShutdown(boolean shutdown){
		this.shutdown = shutdown;
	}
	
	/**
	 * Match the xpaths and xml's
	 */
	
	
	public void match(){
		
		
			
			List<XMLEntity> xmlDocuments = this.xmlAccessor.fetchAllEntities();
			List<String> xmlPaths = getXmlPaths(xmlDocuments);
			List<ChannelEntity> channels = this.channelAccessor.fetchAllEntities();
			List<ChannelXMLEntity> channelXmlMapping = this.channelXMLAccessor.fetchAllEntities();
			
			for(ChannelEntity channel:channels){
				
				String xPathExpressions = channel.getxPathExpressions();
				String[] expressions = xPathExpressions.split(";");
				
				ChannelXMLEntity channelXml = this.channelXMLAccessor.fetchEntityFromPrimaryKey(channel.getChannelId());
				List<String> xmlDocs=null;
				if(channelXml!=null){
					 xmlDocs = channelXml.getXmlPageID();
				}
				
				for(XMLEntity doc: xmlDocuments){
					
					if(xmlDocs!=null){
						if(xmlDocs.contains(doc.getXmlPageId())){
							continue;
						}
					}
										
						XPathEngine engine = XPathEngineFactory.getXPathEngine();
						engine.setXPaths(expressions);
						boolean[] status = engine.evaluate(Utils.getDocmentFromContent(doc.getXmlContent()));
						loop:for(boolean b : status){
							if(b==true){
								
								if(xmlDocs==null){
									xmlDocs = new ArrayList<String>();
									xmlDocs.add(doc.getXmlPageId());
								}else{
									xmlDocs.add(doc.getXmlPageId());
								}
								
								break loop;
							}
						}
						}
					
					
					if(channelXml == null & xmlDocs !=null){
						channelXml = new ChannelXMLEntity();
						channelXml.setChannelId(channel.getChannelId());
						channelXml.setXmlPageID(xmlDocs);
						this.channelXMLAccessor.putEntity(channelXml);
					}else{
						if(xmlDocs!=null){
							channelXml.setXmlPageID(xmlDocs);
							this.channelXMLAccessor.putEntity(channelXml);
						}
					}
					
				 }
				
									
		
		
	}

	private List<String> getXmlPaths(List<XMLEntity> documents){
		List<String> xmlPaths = new ArrayList<String>();
		
		for(XMLEntity doc:documents){
			xmlPaths.add(doc.getXmlPageId());
		}
		return xmlPaths;
	}
	

}
