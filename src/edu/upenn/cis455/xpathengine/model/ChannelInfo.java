package edu.upenn.cis455.xpathengine.model;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.accessor.ChannelAccessor;
import edu.upenn.cis455.storage.accessor.ChannelXMLAccessor;
import edu.upenn.cis455.storage.accessor.XMLAccessor;
import edu.upenn.cis455.storage.entity.ChannelEntity;
import edu.upenn.cis455.storage.entity.ChannelXMLEntity;
import edu.upenn.cis455.storage.entity.XMLEntity;
import edu.upenn.cis455.xpathengine.utils.StringConstants;
import edu.upenn.cis455.xpathengine.utils.Utils;

/**
 * Channel related functions
 * @author cis455
 *
 */
public class ChannelInfo {
	
	public ChannelInfo(){
		
	}
	
	public String generateChannelXml(String channelId){
		
		StringBuilder sb = new StringBuilder();
		
		
		return sb.toString();
	}
	/**
	 * Generate xml document for channel id
	 * @param channelId
	 * @return
	 */
	public String generateDocument(String channelId){
		
		try{
			
			ChannelXMLAccessor accessor = new ChannelXMLAccessor(DBWrapper.getStore());
			ChannelAccessor chAccessor = new ChannelAccessor(DBWrapper.getStore());
			
			ChannelEntity chEntity = chAccessor.fetchEntityFromPrimaryKey(channelId);
			
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			if(chEntity!=null){
				ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\""+chEntity.getStyleSheet()+"\"");
				doc.appendChild(pi);
			}
			
			org.w3c.dom.Element rootElement = doc.createElement("documentcollection");
			doc.appendChild(rootElement);
			
			ChannelXMLEntity entity = accessor.fetchEntityFromPrimaryKey(channelId);
			
			XMLAccessor xmlAccessor = new XMLAccessor(DBWrapper.getStore());
			
			if(entity!=null){
				
				List<String> xmlPages = entity.getXmlPageID();
				
				if(xmlPages!=null){
					
					for(String page:xmlPages){
						
						XMLEntity currPage = xmlAccessor.fetchEntityFromPrimaryKey(page);
						
						if(currPage!=null){
							org.w3c.dom.Element currDoc = doc.createElement("document");
							Attr att1 = doc.createAttribute("crawled");
							att1.setValue(Utils.getFormatterDateForXml(currPage.getXmlLastParsed()));
							
							Attr att2 = doc.createAttribute("location");
							att2.setValue(currPage.getXmlPageUrl());
							
							currDoc.setAttributeNode(att1);
							currDoc.setAttributeNode(att2);
							
							//currDoc.appendChild(doc.createTextNode(currPage.getXmlContent()));
							
							Document fetchedDoc = Utils.getDocmentFromContent(currPage.getXmlContent());
							int childCount=0;
							Node node= fetchedDoc.getChildNodes().item(childCount);
							
							if(node.getNodeType()!=Node.ELEMENT_NODE){
								
								while(node.getNodeType()!=Node.ELEMENT_NODE){
									node = fetchedDoc.getChildNodes().item(++childCount);
								}
								
							}
							
							
							currDoc.appendChild(doc.adoptNode(node));
							rootElement.appendChild(currDoc);
							
						}
						
						
					}
					
				}
				
			}
			
			DOMSource domSource=new DOMSource(doc);

		    StringWriter stringWriter=new StringWriter();

		    StreamResult result = new StreamResult(stringWriter);

		    TransformerFactory tFactory =TransformerFactory.newInstance();
		    Transformer transformer = tFactory.newTransformer();
		    transformer.setOutputProperty("indent","yes");

		    transformer.transform(domSource, result);        
		    return stringWriter.toString();
			
			
		}catch(ParserConfigurationException pce){
			
		}catch(TransformerException tfe){
			System.out.println("Some Exception");
		}
		
		return null;
	}
	

}
