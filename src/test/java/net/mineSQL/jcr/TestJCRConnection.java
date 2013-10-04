package net.mineSQL.jcr;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.mineSQL.util.TimeLog;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 * Unit test for simple App.
 */
public class TestJCRConnection
		extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public TestJCRConnection(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestJCRConnection.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() throws RepositoryException {
		TimeLog tl = new TimeLog();
		/*
		Repository repos = JcrUtils.getRepository("http://localhost:4502/crx/server");

		Credentials cd = new SimpleCredentials("admin", "admin".toCharArray());

		Session session = repos.login(cd);

		System.out.println("Hello Session UserID: " + session.getUserID());
		System.out.println(tl.getTime());
		*/
		assertTrue(true);
	}
}
