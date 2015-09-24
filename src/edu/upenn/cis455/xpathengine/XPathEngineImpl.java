package edu.upenn.cis455.xpathengine;

import org.w3c.dom.Document;

/**
 * Implementation of XPath Engine Interface
 * @author cis455
 *
 */


public class XPathEngineImpl implements XPathEngine {
	
	String[] xPaths;
	boolean[] validXPaths;
	
  public XPathEngineImpl() {
	  
  }

  /**
   * Argument: String Array
   * Return: void
   * 
   * Sets the string array to the class variable
   */
  
  public void setXPaths(String[] s) {
	this.xPaths = s;
	this.validXPaths = new boolean[this.xPaths.length];
  }

  
  /**
   * Returns if the ith xpath is valid or not
   * Input: integer i
   * Output: boolean isValid
   */
  
  public boolean isValid(int i) {
	  if(this.validXPaths==null)
		  return false;
	  else
		  return this.validXPaths[i];
  }
  
  /**
   * Evaluates the document with respect to the  the xpaths
   * Input: Document d
   * Output: Array of booleans
   */
	
  public boolean[] evaluate(Document d) { 
	  
	  XPathEvaluator evaluator = new XPathEvaluator();
	  this.validXPaths = evaluator.evaluate(this.xPaths, d);
	  
	  return this.validXPaths; 
  }
        
}
