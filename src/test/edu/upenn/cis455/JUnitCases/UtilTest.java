package test.edu.upenn.cis455.JUnitCases;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import edu.upenn.cis455.xpathengine.utils.Utils;

public class UtilTest extends TestCase {
	
	private String validName = "testName";
	private String invalidName = "@someName";
	
	@Test
	public void testIsValidName() {
		assertEquals("Check if valid name", true,Utils.isValidName(validName));
		assertEquals("Check if valid name", false,Utils.isValidName(invalidName));

	}

}
