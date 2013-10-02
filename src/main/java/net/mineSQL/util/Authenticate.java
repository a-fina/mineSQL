/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.jcr.Credentials;

import org.apache.log4j.Logger;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * First hop example. Logs in to a content repository and prints a status
 * message.
 */
public class Authenticate implements Filter {

	private static final Logger log = Logger.getLogger(Authenticate.class);

	@Override
	public void init(FilterConfig fc) throws ServletException {
		log.info("Authenticate init");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		try {

			TimeLog tl = new TimeLog();

			// H2
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test");
			log.info("-------------> Logged catalog " + conn.getCatalog() );
			conn.close();

		} catch (ClassNotFoundException ex) {
			log.error("Autenticate Session error " + ex.getMessage());
			ex.printStackTrace();
		} catch (SQLException ex) {
			log.error("Autenticate Session error " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {
		log.info("Authenticate destroy");
	}
}
