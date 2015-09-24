package test.edu.upenn.cis455.JUnitCases;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.upenn.cis455.xpathengine.model.Launcher;

public class CrawlerLauncherTest {

	@Test
	public void testValidate() {
		String[] args = {"https://www.google.com","/usr/db","20"};
		Launcher launch = new Launcher(args);
		assertEquals("Are arguments valid", true, launch.validate());
		
	}

}
