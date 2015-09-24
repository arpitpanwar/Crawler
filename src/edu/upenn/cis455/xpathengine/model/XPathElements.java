package edu.upenn.cis455.xpathengine.model;

import java.util.ArrayList;

import edu.upenn.cis455.xpathengine.utils.TokenConstants;
import edu.upenn.cis455.xpathengine.utils.Utils;

/**
 * Class validates if the xpath is a valid xpath according to the grammar specified in the 
 * assignment 
 * @author cis455
 *
 */

public class XPathElements {

	private ArrayList<Element> elementList;
	private int index;
	private int counter;
	private int length;
	private int level;
	
	/**
	 * Constructor taking in a List of tokens
	 * @param tokens
	 */
	
	public XPathElements(ArrayList<Element> tokens){
		
		this.elementList = tokens;
		index =0;
		counter=0;
		level =0;
		length = this.elementList.size();
	}
	
	public ArrayList<Element> getElementList(){
		return this.elementList;
	}
	
	/**
	 * Returns the next element from the token list based on the current index.
	 * @return
	 */
	
	private Element getNextElement(){
			if(this.elementList.size()>=(index+1))
				return this.elementList.get(index++);
			else
				return null;
	}
	
	/**
	 * Validates the xPath by calling in Axis and Step functions
	 * @return
	 */
	
	
	public boolean validate(){
		boolean isValid = false;
		
		isValid = axis(false) & step(false,false);
				
		return isValid;
	}
	
	/**
	 * Validates the xPath axis
	 * @param fromStep
	 * @return boolean isValid
	 */
	
	public boolean axis(boolean fromStep){
		boolean isValid = false;
		
		if(index==length)
			return true;
		
		Element elem = getNextElement();
		
		if(elem==null)
		{
			return false;
		}
		if(elem.getElementType().equalsIgnoreCase(TokenConstants.AXIS))
			isValid=true;
		else
			isValid = false;
		
		return isValid;
	}
	
	/**
	 * Validates the step part of the grammar.
	 * It keeps track of the fact that it is called from step or test
	 * @param fromStep
	 * @param fromTest
	 * @return
	 */
	
	
	public boolean step(boolean fromStep,boolean fromTest){
		boolean isValid = false;
		boolean test=true;
		int ctr=0;
		if(index==length)
			return true;
		
		
			Element elem = getNextElement();
			if(elem==null)
				return false;
			
			String type = elem.getElementType();
						
			
			switch(type){
				
			case(TokenConstants.OPEN_BRACKET):
			{
				if(ctr==0 && !fromStep)
					return false;
				
				level++;
				isValid = test();
				testLoop:while(true & isValid){
					Element tempelem = getNextElement();
					if(tempelem!=null)
					{	
					
						if(tempelem.getElementType().equalsIgnoreCase(TokenConstants.OPEN_BRACKET)){
							isValid=test();
						}else{
							index--;
							break testLoop;
						}
					}else{
						break testLoop;
						
					}
				}
				
				if(index!=length){
					isValid = step(true, fromTest);
				}
												
				break;
			}
			
			case(TokenConstants.ELEMENTTYPE_NODE):
			{
				ctr++;
				if(!Utils.isValidName(elem.getName()))
					return false;
				
				if(fromTest && (getNextElement().getElementType().equalsIgnoreCase(TokenConstants.CLOSING_BRACKET)))
				{	index--;
					return true;
				}
				else{
					if(fromTest)
						index--;
					isValid = step(true,fromTest);
				}
				break;
			}
			case(TokenConstants.AXIS):
			{
				if(ctr==0 && !fromStep)
					return false;
				
				if(!fromStep)
					isValid=false;
				else
					isValid = step(fromStep,false);
				
				break;
			}
			case(TokenConstants.CLOSING_BRACKET):{
				if(ctr==0)
					return false;
				
				return false;
				
			}
			
			default:
				isValid = false;
					
			}
		
		
				
		return isValid;
	}
	
	/**
	 * Validates the test part of the grammar
	 * @return boolean isValid
	 */
	
	
	public boolean test(){
		boolean isValid = false;
		
		Element elem = getNextElement();
		
		if(elem==null)
			return false;
		
		String type = elem.getElementType();
		
		switch(type){
		
		case(TokenConstants.TEXT):{
			if(getNextElement().getElementType().equalsIgnoreCase(TokenConstants.CLOSING_BRACKET))
				isValid = true;
			else
				isValid = false;
			
			break;
		}
		
		case(TokenConstants.CONTAINS):{
			if(getNextElement().getElementType().equalsIgnoreCase(TokenConstants.CLOSING_BRACKET))
				isValid = true;
			else
				isValid = false;
			
			break;
		}
		
		case(TokenConstants.ATTRIBUTE):{
			
			if(!Utils.isValidName(elem.getName()))
				return false;
			
			if(getNextElement().getElementType().equalsIgnoreCase(TokenConstants.CLOSING_BRACKET))
				isValid = true;
			else
				isValid = false;
			
			break;
			
		}
		
		default:{
				index--;
				isValid = step(false,true) && getNextElement().getElementType().equalsIgnoreCase(TokenConstants.CLOSING_BRACKET);
			}
		
	}
		return isValid;
	}
	
	
	
	
}
