package edu.upenn.cis455.xpathengine;

import java.util.ArrayList;

import org.w3c.dom.Document;

import edu.upenn.cis455.xpathengine.model.Element;
import edu.upenn.cis455.xpathengine.model.Tokenizer;
import edu.upenn.cis455.xpathengine.model.XPathElements;
import edu.upenn.cis455.xpathengine.model.XPathValidator;

/**
 * Calls the evaluation routines to validate and evaluate the xpath
 * @author cis455
 *
 */

public class XPathEvaluator {
	
	/**
	 * Evaluates the rules by first checking if they are valid and then checking if they are present in the document
	 * @param rules
	 * @param d
	 * @return boolean[]
	 */
	
	public boolean[] evaluate(String[] rules, Document d){
		
		boolean[] results = new boolean[rules.length];
		int ctr=0;
		for(String rule:rules){
			
			results[ctr] = this.validate(rule) && this.evaluate(rule,d);
			
			ctr++;
		}
		
		return results;
	}
	
	/**
	 * Validates of the input rule is a valid xpath rule
	 * @param rule
	 * @return
	 */
	
	
	private boolean validate(String rule){
		
		Tokenizer tokens = new Tokenizer(rule);
		
		ArrayList<Element> elements = tokens.tokenize();
		
		boolean result = new XPathElements(elements).validate();
				
		
		return result;
	}
	
	/**
	 * Evaluates if the rule is present in the document d
	 * @param rule
	 * @param d
	 * @return boolean
	 */
	
	private boolean evaluate(String rule,Document d){
		
		boolean isValid = false;
		
		XPathValidator validator = new XPathValidator(rule, d);
		isValid = validator.validate();
		
		return isValid;
		
	}
	
	

}
