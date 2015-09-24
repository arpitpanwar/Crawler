package edu.upenn.cis455.xpathengine.model;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.upenn.cis455.xpathengine.utils.TokenConstants;

/**
 * Validates the presence of xpath in the document
 * @author cis455
 *
 */


public class XPathValidator {
	
	private String rule;
	private Document doc;
	private ArrayList<ElementListModel> elementList;
	private ArrayList<Element> tokens;
	
	/**
	 * Constructor for the class
	 * @param rule
	 * @param doc
	 */
	
	public XPathValidator(String rule, Document doc){
		this.rule = rule;
		this.doc = doc;
		elementList = new ArrayList<ElementListModel>();
		tokens = new Tokenizer(this.rule).tokenize();
	}
	
	/**
	 * Validates the xpath 
	 * @return boolean isValid
	 */
	
	
	public boolean validate(){
		boolean isValid = false;
		boolean root=true;
		int childCount=0;
		Node node= doc.getChildNodes().item(childCount),origNode=null;
		
		if(node.getNodeType()!=Node.ELEMENT_NODE){
			
			while(node.getNodeType()!=Node.ELEMENT_NODE){
				node = doc.getChildNodes().item(++childCount);
			}
			
		}
		
		
		ElementListModel elemList = new ElementListModel();
		ArrayList<Element> clonedList = new ArrayList<Element>();
		clonedList.addAll(this.tokens);
		elemList.setElementList(clonedList);
		elemList.setNode(node);
		elemList.setVisitedList(new ArrayList<Element>());
		
		elementList.add(elemList);
		
		while(!elementList.isEmpty()){
		
		ElementListModel tempList = elementList.remove(elementList.size()-1);
		ArrayList<Element> elements = tempList.getElementList();
		ArrayList<Element> visited =tempList.getVisitedList();
		node = tempList.getNode();
		int len = elements.size();
		outer: for(int i=0;i<len;i++){
			Element elem = elements.remove(0);
			
			
			if(elem.getElementType().equalsIgnoreCase(TokenConstants.AXIS)){
				visited.add(elem);
				continue;
			}
			
			if(root){
							
				if(elem.getElementType().equalsIgnoreCase(TokenConstants.ELEMENTTYPE_NODE)){
					if(!node.getNodeName().equalsIgnoreCase(elem.getName())){
						return false;
					}else{
						visited.add(elem);
					}
				}else{
					return false;
				}
				
				root=false;
				continue;
			}
			
			String type = elem.getElementType();
			
			switch(type){
				case(TokenConstants.ATTRIBUTE):
				{
					NamedNodeMap nodemap = node.getAttributes();
					Node attrib = nodemap.getNamedItem(elem.getName());
					if(attrib==null){
						
					}else{
						if(attrib.getNodeValue().equalsIgnoreCase(elem.getValue())){
							visited.add(elem);
						}
					}
				}
				
				case(TokenConstants.AXIS):
				{
					visited.add(elem);
					continue;
				}
				case(TokenConstants.ELEMENTTYPE_NODE):
				{
					
					NodeList list = node.getChildNodes();
					boolean found = false;
					for(int j=0;j<list.getLength();j++){
						Node curr = list.item(j);
						if(curr.getNodeName().equalsIgnoreCase(elem.getName())){
							found =true;
							ElementListModel tempModel = new ElementListModel();
							ArrayList<Element> cloneList = new ArrayList<Element>();
							cloneList.addAll(elements);
							tempModel.setElementList(cloneList);
							ArrayList<Element> cloneVisitedList = new ArrayList<Element>();
							cloneVisitedList.addAll(visited);
							cloneVisitedList.add(elem);
							tempModel.setVisitedList(cloneVisitedList);
							tempModel.setNode(curr.cloneNode(true));
							elementList.add(tempModel);
						}
					}
					
					if(!found){
						break outer;
					}else{
						visited.add(elem);
					}
					break;
				}
				case(TokenConstants.OPEN_BRACKET):
				{
					origNode = node;
					visited.add(elem);
					ElementListModel tempModel = new ElementListModel();
					ArrayList<Element> cloneList = new ArrayList<Element>();
					cloneList.addAll(elements);
					tempModel.setElementList(cloneList);
					ArrayList<Element> cloneVisitedList = new ArrayList<Element>();
					cloneList.addAll(visited);
					tempModel.setVisitedList(cloneVisitedList);
					tempModel.setNode(node.cloneNode(true));
					elementList.add(tempModel);
					break;
				}
				case(TokenConstants.CLOSING_BRACKET):
				{
					node = origNode;
					visited.add(elem);
					break;
				}
				case(TokenConstants.CONTAINS):
				{
					String text = node.getTextContent();
															
					if(!text.contains(elem.getValue())){
						break outer;
					}else{
						visited.add(elem);
					}
					break;
				}
				case(TokenConstants.TEXT):
				{
					String content = node.getTextContent();
					if(content == null){
						content = getNodeTextContent(node);
					}
					String text = content.trim();
							
					
					if(!text.equals(elem.getValue()))
						break outer;
					else
						visited.add(elem);
					
					break;
				}
				
				default:
					return false;
			
			
			}
			
			}
		if(isSolved(visited))
			return true;
		
		}
				
		return false;
			
	}
	
	/**
	 * Checks if the list is solved.
	 * @param visitedList
	 * @return boolean solved
	 */
	
	private boolean isSolved(ArrayList<Element> visitedList){
		boolean solved=false;
		
		if(visitedList.size()!=this.tokens.size()){
			solved= false;
		}else{
			int ctr=0;
			
		loop:for(Element elem:visitedList){
				
				Element temp = this.tokens.get(ctr);
				
				if(!temp.getElementType().equalsIgnoreCase(elem.getElementType())){
					solved = false;
					break loop;
				}
				
				if(temp.getName()==null){
					if(elem.getName()!=null){
						solved = false;
						break loop;
					}
				}else{
					
					if(elem.getName()==null){
						solved = false;
						break loop;	
					}else{
						if(!temp.getName().equalsIgnoreCase(elem.getName())){
							solved = false;
							break loop;
						}
					}
					
				}
				
				if(temp.getValue()==null){
					if(elem.getValue()!=null){
						solved = false;
						break loop;
					}
				}else{
					
					if(elem.getValue()==null){
						solved = false;
						break loop;	
					}else{
						if(!temp.getValue().equalsIgnoreCase(elem.getValue())){
							solved = false;
							break loop;
						}
					}
					
				}
				
				if(ctr==this.tokens.size()-1){
					solved=true;
				}
				ctr++;
				
			}
						
		}
				
		
		return solved;
	}
	
	private String getNodeTextContent(Node node){
		
		StringBuilder sb = new StringBuilder();
		
		NodeList childNodes = node.getChildNodes();
		
		for(int i=0;i<childNodes.getLength();i++){
			
			Node currNode = childNodes.item(i);
			
			if(currNode.getNodeType() == Node.TEXT_NODE){
				sb.append(currNode.getNodeValue());
			}
			
		}
		
		
		return sb.toString();
	}
	
	
}
