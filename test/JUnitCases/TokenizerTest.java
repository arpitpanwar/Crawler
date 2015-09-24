package JUnitCases;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.upenn.cis455.xpathengine.model.Element;
import edu.upenn.cis455.xpathengine.model.Tokenizer;
import edu.upenn.cis455.xpathengine.utils.TokenConstants;

public class TokenizerTest {

	private Tokenizer tokenize;
	String testString = "/d/e/f";
	@Before
	public void setUp() throws Exception {
		tokenize = new Tokenizer(this.testString);
	}

	@Test
	public void testTokenize() {
		
		ArrayList<Element> tokens = tokenize.tokenize();
		
		Element elem = tokens.get(0);
		
		assertEquals("Expected Name", TokenConstants.AXIS, elem.getElementType());
		assertEquals("Token Count", 6, tokens.size());		
	}

}
