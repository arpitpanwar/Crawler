package test.edu.upenn.cis455.JUnitCases;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import edu.upenn.cis455.xpathengine.model.XPathValidator;

public class XPathValidatorTest extends TestCase {

	private Document doc;
	private String fileName = "test/JUnitCases/text.xml";
	private String rule="/a/b[text()=\"test\"] ";
	
	@Before
	public void setUp() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setValidating(false);
	    factory.setExpandEntityReferences(false);

	    doc = factory.newDocumentBuilder().parse(new File(fileName));
	}

	@Test
	public void testValidate() {
		
		XPathValidator validator = new XPathValidator(rule, doc);
		
		System.out.println(validator.validate());
		
		
	}

}
