package edu.upenn.cis455.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.upenn.cis455.crawler.info.RobotsTxtInfo;
import edu.upenn.cis455.storage.accessor.HTMLAccessor;
import edu.upenn.cis455.storage.accessor.RobotsAccessor;
import edu.upenn.cis455.storage.accessor.XMLAccessor;
import edu.upenn.cis455.storage.entity.HTMLEntity;
import edu.upenn.cis455.storage.entity.RobotsEntity;
import edu.upenn.cis455.storage.entity.WebPageEntity;
import edu.upenn.cis455.storage.entity.XMLEntity;
import edu.upenn.cis455.xpathengine.utils.Constants;
import edu.upenn.cis455.xpathengine.utils.HeaderConstants;
import edu.upenn.cis455.xpathengine.utils.StringConstants;
import edu.upenn.cis455.xpathengine.utils.Utils;

/**
 * Class which crawls the web
 * @author cis455
 *
 */

public class Crawler implements Callable<Object> {
	
	static final Logger LOG = Logger.getLogger(Crawler.class);
	
	private long size;
	private String URL;
	private URL u;
	private XMLAccessor xmlAccessor;
	private HTMLAccessor htmlAccessor;
	private RobotsAccessor robotAccessor;
	private List<String> parsedLinks;
	private long parsedSize=0;
	
	public Crawler(String URL, XMLAccessor accessor , HTMLAccessor htmlAccessor,long size,RobotsAccessor robotAccessor)throws MalformedURLException{
		
		this.URL = URL;
		u = new URL(this.URL);
		this.xmlAccessor = accessor;
		this.htmlAccessor = htmlAccessor;
		this.robotAccessor=robotAccessor;
		this.size = size;
	}
	
	public String getCurrentURL(){
		return this.URL;
	}
	
	/**
	 * Process the link passed to the crawler
	 * @return
	 */
	
	public HashMap<String,Object> processLink(){
		System.out.println("Starting Processing: "+this.URL);
		HashMap<String,Object> linkInfo = new HashMap<>();
		
		RobotsTxtInfo robots = fetchAndParseRobotsFile();
			
		WebPageEntity entity = fetchPageInfoFromDB();
		
		long modified=0;
		
		if(entity!=null){
			if(entity instanceof XMLEntity){
				XMLEntity xmlEnt = (XMLEntity)entity;
				modified = xmlEnt.getXmlLastUpdated();
			}else{
				HTMLEntity htmlEnt = (HTMLEntity)entity;
				modified = htmlEnt.getHtmlLastUpdated();	
			}
		}
		
		if(isRequestValid(robots)){
			if(robots!=null){
				boolean found=false;
				try{
				Integer delay = robots.getCrawlDelay(Constants.DEFAULT_USER_AGENT);
				if(delay!=null){
					if(delay>0){
						found=true;
						try{
							System.out.println(this.URL+": Crawl Delay encountered. Sleeping");
							Thread.sleep(delay*1000);
						}catch(InterruptedException ie){
							LOG.debug("Thread interrupted");
						}
					}
				}
				delay = robots.getCrawlDelay(Constants.UNIVERSAL_USER_AGENT);
				if(delay!=null & !found){
					if(delay>0){
						try{
							System.out.println(this.URL+": Crawl Delay encountered. Sleeping");
							Thread.sleep(delay*1000);
						}catch(InterruptedException ie){
							LOG.debug("Thread interrupted");
						}
					}
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			System.out.println(this.URL+" :Fetching head headers");
			Map<String, Object> headers = fetchHeader(modified);
			Map<String,List<String>> fetchedHeaders = (Map<String,List<String>>)headers.get("Headers");
			if(headers.get("Redirect")!=null){
				this.URL = (String)headers.get("Redirect");
				try{
					this.u = new URL(URL);
				}catch(Exception e){
					
				}
				return processLink();
			}else{
			
			System.out.println(this.URL+" :Verifying head headers");
			if(isNewRequestRequired(fetchedHeaders)){
				System.out.println(this.URL+" :Fetching page content");
				HashMap<String,Object> pageInfo = fetchPage();
				System.out.println(this.URL+":Parsing Content and looking for links");
				parseAndAddPage(pageInfo, entity);				
			}
			}
		}
		linkInfo.put(Constants.LINK_INFO_LINKS, this.parsedLinks);
		linkInfo.put(Constants.LINK_INFO_SIZE, this.parsedSize);
				
		return linkInfo;
	}
	
	/**
	 * 
	 * @param pageInfo
	 * @param entity
	 * @return
	 */
	
	
	private boolean parseAndAddPage(HashMap<String,Object> pageInfo, WebPageEntity entity){
		
		boolean parsed=false;
		Map<String,List<String>> headers;
		Document doc;
		String content;
		if(entity == null){
			
			headers = (Map<String,List<String>>)pageInfo.get(Constants.SITE_HEADERS);
			content = (String)pageInfo.get(Constants.SITE_CONTENT);
			
			doc = (Document)pageInfo.get(Constants.SITE_DOCUMENT);
			if(headers != null & content!=null){
				String digest = generateMD5Hash(content);
				this.parsedSize = content.length();
				List<String> contentType = headers.get(HeaderConstants.HEADER_CONTENT_TYPE);
				if(contentType!=null){
					
					String type = contentType.get(0);
					
					if(Utils.isHtmlType(type)){
						HTMLEntity digestEntity =this.htmlAccessor.fetchEntityFromDigestKey(digest);
						if(digestEntity==null){
							HTMLEntity htmlEnt = new HTMLEntity();
							htmlEnt.setHtmlPageId(Utils.generateUniqueId());
							htmlEnt.setHtmlPageUrl(this.URL);
							htmlEnt.setHtmlLastParsed(new Date().getTime());
							htmlEnt.setHtmlLastUpdated(Utils.getTimeFromString(headers.get(HeaderConstants.HEADER_DATE).get(0)));
							htmlEnt.setHtmlDomain(this.u.getHost());
							htmlEnt.setHtmlParseBanned(false);
							htmlEnt.setHtmlContent(content);
							htmlEnt.setMessageDigest(digest);
							this.htmlAccessor.putEntity(htmlEnt);
							this.parsedLinks = parseLinks(doc);
						}else{
							LOG.debug("Content Earlier seen: "+this.URL);
							System.out.println(this.URL+" :Content seen earlier");
						}
					}
					if(Utils.isXmlType(type)){
						
						XMLEntity digestEntity = this.xmlAccessor.fetchEntityFromDigestKey(digest);
						if(digestEntity==null){
							XMLEntity xmlEnt = new XMLEntity();
							xmlEnt.setXmlPageId(Utils.generateUniqueId());
							xmlEnt.setXmlPageUrl(this.URL);
							xmlEnt.setXmlLastParsed(new Date().getTime());
							xmlEnt.setXmlLastUpdated(Utils.getTimeFromString(headers.get(HeaderConstants.HEADER_DATE).get(0)));
							xmlEnt.setXmlDomain(this.u.getHost());
							xmlEnt.setXmlParseBanned(false);
							xmlEnt.setXmlContent(content);
							xmlEnt.setMessageDigest(digest);
							this.xmlAccessor.putEntity(xmlEnt);
						}else{
							LOG.debug("Content Earlier seen: "+this.URL);
							System.out.println(this.URL+" :Content seen earlier");
						}
					}
										
				}
				
			}
			
		}else{
			
			headers = (Map<String,List<String>>)pageInfo.get(Constants.SITE_HEADERS);
			content = (String)pageInfo.get(Constants.SITE_CONTENT);
			doc = (Document)pageInfo.get(Constants.SITE_DOCUMENT);
			if(headers != null & content!=null){
				String digest = generateMD5Hash(content);
				this.parsedSize = content.length();
				List<String> contentType = headers.get(HeaderConstants.HEADER_CONTENT_TYPE);
				if(contentType!=null){
					
					String type = contentType.get(0);
					
					if(Utils.isHtmlType(type)){
						
						HTMLEntity htmlEnt = (HTMLEntity)entity;
						htmlEnt.setHtmlLastParsed(new Date().getTime());
						htmlEnt.setHtmlLastUpdated(Utils.getTimeFromString(headers.get(HeaderConstants.HEADER_DATE).get(0)));
						htmlEnt.setHtmlDomain(this.u.getHost());
						htmlEnt.setHtmlParseBanned(false);
						htmlEnt.setHtmlContent(content);
						htmlEnt.setMessageDigest(digest);
						this.htmlAccessor.putEntity(htmlEnt);
						this.parsedLinks = parseLinks(doc);
					
					}
					if(Utils.isXmlType(type)){
						XMLEntity xmlEnt = (XMLEntity)entity;
						xmlEnt.setXmlLastParsed(new Date().getTime());
						xmlEnt.setXmlLastUpdated(Utils.getTimeFromString(headers.get(HeaderConstants.HEADER_DATE).get(0)));
						xmlEnt.setXmlDomain(this.u.getHost());
						xmlEnt.setXmlParseBanned(false);
						xmlEnt.setXmlContent(content);
						xmlEnt.setMessageDigest(digest);
						this.xmlAccessor.putEntity(xmlEnt);
						
					}
									
					}
			
				}
			}
		return parsed;
	}
	
	/**
	 * 
	 * @param headers
	 * @return
	 */
	
	private boolean isNewRequestRequired(Map<String, List<String>> headers){
		boolean isValid = false;
		
		//if(entity==null){
			if(headers!=null){
			List<String> responseString = headers.get(null);
			
			if(responseString!=null){
				
				String resString = responseString.get(0);
				String[] tokens = resString.split(" ");
				String code = tokens[1];
				
				if(!code.equalsIgnoreCase(HeaderConstants.STATUSCODE_OK)){
					isValid = false;
				}else{
					
					List<String> contentType = headers.get(HeaderConstants.HEADER_CONTENT_TYPE);
					
					if(contentType!=null){
						String type = contentType.get(0);
						if(Utils.isXmlType(type) || Utils.isHtmlType(type)){
							
							List<String> values = headers.get(HeaderConstants.HEADER_CONTENT_LENGTH);
							
							if(values!=null){
								if(!values.isEmpty()){
									
									long value = Long.parseLong(values.get(0));
									
									if(value<=this.size){
										isValid=true;								
									}else{
										System.out.println(this.URL+" :Content Size too large");
									}
									
									
								}else{
									isValid = true;
								}
							}else{
								isValid=true;
							}
						}else{
							System.out.println(this.URL+" :Content not xml or html");
						}
					}
				}
				
			}
			}		
		//}
		/*
		else{
			
			if(entity!=null){
				XMLEntity xmlEnt;
				HTMLEntity htmlEnt;
				boolean isXml = false, isHtml=false;
				if(entity instanceof XMLEntity){
					 xmlEnt = (XMLEntity)entity;
					isXml = true;
				}else{
					htmlEnt = (HTMLEntity)entity;
					isHtml=true;
				}
				
				if(isXml){
					
					
					
				}
				if(isHtml){
					
				}
				
				
			}
			
			
		}
		*/
		
		
		
		return isValid;
	}
	
	/**
	 * Fetch the page info from database
	 * @return WebPageEntity
	 */
	
	private WebPageEntity fetchPageInfoFromDB(){

		WebPageEntity entity = null;
		
		entity = xmlAccessor.fetchEntityFromSecondaryKey(this.URL);
		if(entity==null){
			entity = htmlAccessor.fetchEntityFromSecondaryKey(this.URL);
		}
				
		return entity;		
	}
	
	/**
	 * Check if the request is valid with respect to the robots file
	 * @param robotsInfo
	 * @return boolean isValid
	 */
	
	private boolean isRequestValid(RobotsTxtInfo robotsInfo){
		if(robotsInfo==null){
			return true;
		}
		boolean isValid = false;
		String path = this.u.getPath();
		if(robotsInfo.containsUserAgent(Constants.DEFAULT_USER_AGENT)){
			
			ArrayList<String>links = robotsInfo.getAllowedLinks(Constants.DEFAULT_USER_AGENT);
			boolean shouldParse = true;
			if(links!=null){
				loop:for(String link:links){
					if(path.startsWith(link)){
						shouldParse = true;
						break loop;
					}
				}
			}
			ArrayList<String> disAllowedLinks = robotsInfo.getDisallowedLinks(Constants.DEFAULT_USER_AGENT);
			if(disAllowedLinks!=null){
				loop:for(String link:disAllowedLinks){
					if(path.startsWith(link)){
						shouldParse = false;
						break loop;
					}
				}
			}
						
			isValid = shouldParse;
			
		}else{
			
			if(robotsInfo.containsUserAgent(Constants.UNIVERSAL_USER_AGENT)){
				boolean shouldParse = true;
				ArrayList<String> links = robotsInfo.getAllowedLinks(Constants.UNIVERSAL_USER_AGENT);
				
				if(links!=null){			
				
					loop:for(String link:links){
						if(path.startsWith(link)){
							shouldParse = true;
							break loop;
						}
					}
				}
				
				ArrayList<String> disAllowedLinks = robotsInfo.getDisallowedLinks(Constants.UNIVERSAL_USER_AGENT);
				
				if(disAllowedLinks!=null){
				
					loop:for(String link:disAllowedLinks){
						if(path.startsWith(link)){
							shouldParse = false;
							break loop;
						}
					}
				}
				
				isValid = shouldParse;
			}else{
				isValid = true;
			}
			
		}
				
		return isValid;
	}
	
	/**
	 * Fetch and parse robots file
	 * @return parsed robots file
	 */
	
	private RobotsTxtInfo fetchAndParseRobotsFile(){
		System.out.println(this.URL+" :Fetching and parsing Robots.txt");
		
		RobotsEntity robEntity = this.robotAccessor.fetchEntityFromPrimaryKey(this.u.getHost());
		
		if(robEntity!=null){
			long time = robEntity.getLastUpdatedDate();
			long curr = new Date().getTime();
			
			if((curr-time)<=Constants.ROBOTS_FETCH_DELAY){
				return robEntity.getRobots();
			}
		}
		
		RobotsTxtInfo robotInfo = new RobotsTxtInfo();
		String host = u.getHost();
		String protocol = u.getProtocol();
		int port = u.getPort();
		try{
			URL robot = new URL(protocol, host, Constants.ROBOTS_URL);
			InputStream robotStream = Utils.fetchDataAsStream(robot);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(robotStream));
			String read="";
			String currUserName=null;
			while((read=reader.readLine())!=null){
				if(read.startsWith("#"))
					continue;
				if(read.startsWith(StringConstants.ROBOTS_USER_AGENT)){
					String[] tokens = read.split(":");
					if(tokens.length>1){
						String agentName = tokens[1].trim();
						if(agentName.length()>0){
							robotInfo.addUserAgent(agentName);
							currUserName = agentName;
							robotInfo.addCrawlDelay(currUserName, 0);
						}
					}
				}
				if(read.startsWith(StringConstants.ROBOTS_DISALLOW)){
					String[] tokens = read.split(":");
					if(tokens.length>1){
						String disallowName = tokens[1].trim();			
						if(currUserName!=null){
							robotInfo.addDisallowedLink(currUserName, disallowName);
							
						}
					}
					
				}
				if(read.startsWith(StringConstants.ROBOTS_ALLOW)){
					String[] tokens = read.split(":");
					if(tokens.length>1){
					String allowName = tokens[1].trim();			
					if(currUserName!=null){
						robotInfo.addAllowedLink(currUserName, allowName);
						
					}
					}
				}
				
				if(read.startsWith(StringConstants.ROBOTS_CRAWL_DELAY)){
					String[] tokens = read.split(":");
					if(tokens.length>1){
						String delay = tokens[1].trim();			
						if(currUserName!=null){
							robotInfo.addCrawlDelay(currUserName, Integer.parseInt(delay));
						}
					}
				}
				
				if(read.startsWith(StringConstants.ROBOTS_SITEMAP)){
					String[] tokens = read.split(":",2);
					if(tokens.length>1){
					String sitemap = tokens[1].trim();	
					robotInfo.addSitemapLink(sitemap);
					}				
				}
			}
			
			robEntity = new RobotsEntity();
			robEntity.setDomainName(this.u.getHost());
			robEntity.setRobots(robotInfo);
			robEntity.setLastUpdatedDate(new Date().getTime());
			this.robotAccessor.putEntity(robEntity);
			
			return robotInfo;
		}catch(MalformedURLException mfe){
			
			LOG.debug("Malformed URL: "+mfe.getMessage());
			
		}catch(IOException ioe){
			LOG.debug("IOException during robot parsing: "+ioe.getMessage());
		}catch(Exception e){
			LOG.debug("Unexpected error during robots fetching: "+e.getMessage());
			return null;
		}
		
		return robotInfo;
		
	}
	
	/**
	 * Fetch the header files
	 * @param lastModified
	 * @return
	 */
	
	private Map<String, Object> fetchHeader(long lastModified){
		
		Map<String, Object> headers = Utils.fetchHeadHeaders(this.u,lastModified);
		
		return headers;
	}
	
	/**
	 * Fetch page and page headers
	 * @return HashMap of objects
	 */
	
	
	private HashMap<String,Object> fetchPage(){
		HashMap<String,Object> pageInfo = new HashMap<String, Object>();
		
		pageInfo = Utils.fetchPageAndHeaders(this.u);
		
		return pageInfo;
	}
	
	

	@Override
	public Object call() throws Exception {
		
		return this.processLink();
	}
	
	/**
	 * Parse the document and extract links
	 * @param doc
	 * @return List<String> of links
	 */
	
	private List<String> parseLinks(Document doc){
		List<String> links = new ArrayList<String>();
		
		List<Element> elemList = new ArrayList<Element>();
		NodeList nl = doc.getElementsByTagName("html");
		Element elem = doc.getDocumentElement();
		
		findAllElementsByTagName(elem, "a", elemList);
		
		for(Element e:elemList){
			NamedNodeMap attMap = e.getAttributes();
			for(int i=0;i<attMap.getLength();i++){
			if(attMap.item(i).getNodeName().equalsIgnoreCase("href")){
				try{
					String link = e.getAttribute("href");
					URL temp = new URL(u, link);
					links.add(temp.toString());
				}catch(MalformedURLException mfe){
					LOG.debug("Malformed url: "+mfe.getMessage());
				}
			}
			}
		}
		
		return links;
	} 
	
	/**
	 * Find all elements with a tag
	 * @param el
	 * @param tagName
	 * @param elementList
	 */
	
	private void findAllElementsByTagName(Element el, String tagName, List<Element> elementList){
		if (tagName.equals(el.getTagName())) {
		      elementList.add(el);
		    }
		    Element elem = getFirstElement(el);
		    while (elem != null) {
		      findAllElementsByTagName(elem, tagName, elementList);
		      elem = getNextElement(elem);
		    }
	}
	
	/**
	 * Get elements
	 * @param parent
	 * @return
	 */
	
	private Element getFirstElement(Node parent) {
	    Node n = parent.getFirstChild();
	    while (n != null && Node.ELEMENT_NODE != n.getNodeType()) {
	      n = n.getNextSibling();
	    }
	    if (n == null) {
	      return null;
	    }
	    return (Element) n;
	  }
	
	/**
	 * Get next element
	 * @param el
	 * @return Element
	 */
	
	private Element getNextElement(Element el) {
	    Node nd = el.getNextSibling();
	    while (nd != null) {
	      if (nd.getNodeType() == Node.ELEMENT_NODE) {
	        return (Element) nd;
	      }
	      nd = nd.getNextSibling();
	    }
	    return null;
	  }
	
	/**
	 * Generate MD5 hash of the content
	 * @param content
	 * @return String hash
	 */
	
	private String generateMD5Hash(String content){
		String digest = null;
		try{
			
			MessageDigest mdigest = MessageDigest.getInstance("SHA-256");
			byte[] digestedBytes = mdigest.digest(content.getBytes());
			digest = new String(digestedBytes);
		}catch(NoSuchAlgorithmException nsae){
			LOG.debug("Algorithm not found: "+nsae.getMessage());
		}
		return digest;
	}

	

}
