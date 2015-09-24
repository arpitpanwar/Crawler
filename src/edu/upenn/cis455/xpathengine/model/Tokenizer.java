package edu.upenn.cis455.xpathengine.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.upenn.cis455.xpathengine.utils.TokenConstants;


/**
 * Class for Tokenizing the given path
 * @author cis455
 *
 */

public class Tokenizer {

	Logger LOG = Logger.getLogger(Tokenizer.class);
	 
	private String testString;
	private ArrayList<Element> tokens;
	private int counter;
	
	/**
	 * Constructor for the class
	 * @param testString
	 */
	
	
	public Tokenizer(String testString){
		this.testString = testString.trim();
		tokens = new ArrayList<>();
		counter = 0;
	}
	
	/**
	 * Tokenizer for the string set through the constructor
	 * @return ArrayList of tokens
	 */
	
	
	public ArrayList<Element> tokenize(){
		
		int length = this.testString.length();
		
		while(counter<length){
			
			if(!getNextToken()){
				return null;
			}
						
		}
			
		
		return tokens;
	}
	
	/**
	 * Returns if a next token was found.
	 * The token is inserted in a global token list
	 * @return boolean
	 */
	
	
	private boolean getNextToken(){
	
	try{	
		char c = this.testString.charAt(counter);
		
		switch(c){
			
		case('['):
		{
			Element elem = new Element();
			elem.setElementType(TokenConstants.OPEN_BRACKET);
			tokens.add(elem);
			counter++;
			break;
		}
		
		case(']'):
		{
			Element elem = new Element();
			elem.setElementType(TokenConstants.CLOSING_BRACKET);
			tokens.add(elem);
			counter++;
			break;
			
		}
		
		case('/'):
		{
			Element elem = new Element();
			elem.setElementType(TokenConstants.AXIS);
			tokens.add(elem);
			counter++;
			break;
		}
		
		case('t'):
		{
			String temp = this.testString.substring(counter);
			Pattern pattern = Pattern.compile(TokenConstants.TEXT_REGEX);
			Matcher match = pattern.matcher(temp);
			
			if(match.find()){
				
				Element elem = new Element();
				elem.setElementType(TokenConstants.TEXT);
				elem.setValue(match.group(1));
				tokens.add(elem);
				counter = counter+match.end();
				break;
			}
						
			
		}
		
		case('c'):
		{
			String temp = this.testString.substring(counter);
			Pattern pattern = Pattern.compile(TokenConstants.CONTAINS_REGEX);
			Matcher match = pattern.matcher(temp);
			
			if(match.find()){
				
				Element elem = new Element();
				elem.setElementType(TokenConstants.CONTAINS);
				elem.setValue(match.group(1));
				elem.setName(TokenConstants.TEXT);
				tokens.add(elem);
				counter = counter+match.end();
				break;
			}
			
						
		}
		case('@'):
		{
			String temp = this.testString.substring(counter);
			Pattern pattern = Pattern.compile(TokenConstants.ATTRIBUTE_REGEX);
			Matcher match = pattern.matcher(temp);
			
			if(match.find()){
				
				Element elem = new Element();
				elem.setElementType(TokenConstants.ATTRIBUTE);
				elem.setValue(match.group(2));
				elem.setName(match.group(1));
				tokens.add(elem);
				counter = counter+match.end();
				break;
			}
			
			
		}
		
		default:
		{
			if(Character.isWhitespace(c)){
				counter++;
			}else{
				
			String temp = this.testString.substring(counter).trim();
			if(temp.length()!=0)
			{
			int bracIndex = temp.indexOf(TokenConstants.OPEN_BRACKET);
			int axisIndex = temp.indexOf(TokenConstants.AXIS);
			boolean bracFirst=false;
			if(axisIndex!=-1 && bracIndex !=-1){
				if(bracIndex<axisIndex)
					bracFirst = true;
			}else{
				if(axisIndex==-1)
					bracFirst=true;
			}
						
			if(temp.indexOf(TokenConstants.OPEN_BRACKET)!=-1 & bracFirst){
								
				String val = temp.substring(0,temp.indexOf(TokenConstants.OPEN_BRACKET));
				Element elem = new Element();
				
				elem.setElementType(TokenConstants.ELEMENTTYPE_NODE);
				elem.setName(val.trim());
				counter = counter + val.length();
				tokens.add(elem);
			}else{
				if(temp.indexOf(TokenConstants.AXIS)!=-1 & !bracFirst){
					
					String val = temp.substring(0,temp.indexOf(TokenConstants.AXIS));
					Element elem = new Element();
					
					elem.setElementType(TokenConstants.ELEMENTTYPE_NODE);
					elem.setName(val.trim());
					counter = counter + val.length();
					tokens.add(elem);
				}
				else{
					if(temp.indexOf(TokenConstants.CLOSING_BRACKET)!=-1){
						
						String val = temp.substring(0,temp.indexOf(TokenConstants.CLOSING_BRACKET));
						Element elem = new Element();
						
						elem.setElementType(TokenConstants.ELEMENTTYPE_NODE);
						elem.setName(val.trim());
						counter = counter + val.length();
						tokens.add(elem);
					}else{
						String val = temp.substring(0);
						Element elem = new Element();
						elem.setElementType(TokenConstants.ELEMENTTYPE_NODE);
						elem.setName(val.trim());
						counter = counter + val.length();
						tokens.add(elem);
					}
				}
			}
			}
		}
		}
		}
		
	}catch(Exception e){
		LOG.debug("Unexpected exception during token parsing: "+e.getMessage());
		return false;
	}	
		
		
		return true;
	}
	
	
	
	
}
