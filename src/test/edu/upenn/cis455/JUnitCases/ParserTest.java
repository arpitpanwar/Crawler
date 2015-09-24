package test.edu.upenn.cis455.JUnitCases;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis455.xpathengine.model.Element;
import edu.upenn.cis455.xpathengine.model.Tokenizer;
import edu.upenn.cis455.xpathengine.model.XPathElements;

public class ParserTest extends TestCase {

	private Tokenizer tokenizer;
	private XPathElements elements;
	private String xPath="/d               /e/f        [         foo[text()=\"something\"]           ]          [bar]   /   next";
	private String[] xPaths = {
			
			"/catalog]",
			
			"/catalog[",
			
			"/catalog/cd[@title=Empire Burlesque\"]",
			
			"/catalog/cd[@title=\"Empire Burlesque\"][artist=\"Bob Dylan\"]",
			
			"/catalog/cd[@title=Empire Burlesque\"]]",
			
			"/catalog/cd[@year=\"1988\"][@price=\"9.90\"]/country[text()=\"UK\"]]",
			
			"/catalog/cd[[@title=Empire Burlesque\"]",
			
			"/catalog/cd[@year=\"1988\"][[@price=\"9.90\"]/country[text()=\"UK\"]",
			
			"/catalog/!badelem",
			
			"/@frenchbread/unicorns",
			
			"/abc/123bad",
			"/hello world",
			"/check(these)chars",
			"/xmlillegal",
			"/XMLillegal",
			
			"/abc/ab[@,illegalattribute=\"hello\"]",
			
			"/abc/ab[@<illegalattribute=\"hello\"]",
			
			"/abc/ab[text()=\"abc\"  pqr]",
			
			"/abc/ab[@attname\"=\"abc\"]",
			
			"/abc/ab[@=\"hello\"]",
			
			
	};
	
	@Before
	public void setUp() throws Exception {
		tokenizer = new Tokenizer(xPath);
	}

	@Test
	public void testXpathFalse() {
		
		ArrayList<Element> tokens; 
		
		for(String path:xPaths){
			System.out.println("Path: "+path);
			tokenizer = new Tokenizer(path);
			tokens = tokenizer.tokenize();

			elements = new XPathElements(tokens);
						
			
			assertEquals("Is xPath Valid", false, elements.validate());
			
		}
		
	}
	
	@Test
	public void testXpathTrue() {
		
		ArrayList<Element> tokens = tokenizer.tokenize();
		
		elements = new XPathElements(tokens);
		
		assertEquals("Is xPath Valid", true, elements.validate());
		
				
	}

}
