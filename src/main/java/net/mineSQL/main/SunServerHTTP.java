/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.main;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *
 * @author afinamore
 */
public class SunServerHTTP {

	public static final String remoteAddress = "http://host.it";
	public static final int localPort = 8000;

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(SunServerHTTP.localPort), 0);
		server.createContext("/", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Server running, remoteAddress: " + SunServerHTTP.remoteAddress 
						+ " localPort: " + SunServerHTTP.localPort);
	}

	static class MyHandler implements HttpHandler {

		public void handle(HttpExchange t) throws IOException {
			byte[] responseBody = null;
			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();
			// Create a method instance.
			GetMethod method = new GetMethod(SunServerHTTP.remoteAddress + t.getRequestURI().toString());

			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));

			try {
				// Execute the method.
				int statusCode = client.executeMethod(method);

				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + method.getStatusLine());
				}
				// Read the response body.
				responseBody = method.getResponseBody();

				// Deal with the response.
				// Use caution: ensure correct character encoding and is not binary data
				System.out.println("OK request: " + t.getRequestURI().toString());

			} catch (HttpException e) {
				System.err.println("Fatal protocol violation: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Fatal transport error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				// Release the connection.
				method.releaseConnection();
			}
			OutputStream os = t.getResponseBody();
			t.sendResponseHeaders(200, responseBody.length);
			os.write(responseBody);
			os.close();
		}
	}
}
