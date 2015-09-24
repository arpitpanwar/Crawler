package test.edu.upenn.cis455;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Cases for the entire Codebase
 * @author cis455
 *
 */
public class RunAllTests extends TestCase 
{
  public static Test suite() 
  {
    try {
      Class[]  testClasses = {
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.ParserTest"),
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.TokenizerTest"),
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.XPathValidatorTest"),
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.UtilTest"),
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.UserEntityTest"),
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.CrawlerTest"),
    		  Class.forName("test.edu.upenn.cis455.JUnitCases.CrawlerLauncherTest")
      };   
      
      return new TestSuite(testClasses);
    } catch(Exception e){
      e.printStackTrace();
    } 
    
    return null;
  }
}
