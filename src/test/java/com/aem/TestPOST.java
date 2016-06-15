package com.aem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

/**
 *
 * @author alessio.finamore
 */
public class TestPOST extends TestCase {

	private static final String PROTOCOL = "http";
	private static final int PORT = 4502;
	private static final String HOST = "localhost";
	private static final String COOKIE_NAME = "login-token";

	public TestPOST(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testHello() throws IOException {
		String username = "admin";
		String password = "admin";
		String specialPath = "/content/hm";

		HttpClient client = new HttpClient();

		String token = getToken(username, password, client);

		if (token == null) {
			System.err.println("No login cookie set.");
			return;
		}
		System.out.println("token = " + token);

		GetMethod get = new GetMethod(String.format("%s://%s:%s%s", PROTOCOL, HOST, PORT, specialPath));
		get.addRequestHeader("Cookie", String.format("%s=%s", COOKIE_NAME, token));

		int status = client.executeMethod(get);
		if (status == 200) {
			System.out.println(get.getResponseBodyAsString());
		} else {
			System.err
				.println("Unexcepted response code " + status + "; msg: " + get.getResponseBodyAsString());
		}
	}

	private static String getToken(String username, String password, HttpClient client) throws IOException,
		HttpException {
		String token = null;

		PostMethod authRequest = new PostMethod(String.format("%s://%s:%s/j_security_check", PROTOCOL, HOST, PORT));
		authRequest.setParameter("j_username", username);
		authRequest.setParameter("j_password", password);
		authRequest.setParameter("j_validate", "true");

		int status = client.executeMethod(authRequest);
		if (status == 200) {
			Header[] headers = authRequest.getResponseHeaders("Set-Cookie");
			for (Header header : headers) {
				String value = header.getValue();
				if (value.startsWith(COOKIE_NAME + "=")) {
					int endIdx = value.indexOf(';');
					if (endIdx > 0) {
						token = value.substring(COOKIE_NAME.length() + 1, endIdx);
					}
				}
			}
		} else {
			System.err
				.println("Unexcepted response code " + status + "; msg: " + authRequest.getResponseBodyAsString());
		}
		return token;
	}
}
