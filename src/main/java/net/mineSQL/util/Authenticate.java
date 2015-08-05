/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.util;

import java.io.IOException;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.jackrabbit.core.TransientRepository;

/**
 * First hop example. Logs in to a content repository and prints a
 * status message.
 */
public class Authenticate implements Filter  {

    private static final Logger log = Logger.getLogger(Authenticate.class);

  
    @Override
    public void init(FilterConfig fc) throws ServletException {
        log.info("Authenticate init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Repository repository = new TransientRepository();
        Session session = null;

        try {
//            session = repository.login();
     
//            String user = session.getUserID();
//            String name = repository.getDescriptor(Repository.REP_NAME_DESC);

//            log.info("-------------> Logged in as "+ user +" to a " + name + "repository.");

//            session.logout();

        } catch (Exception ex) {
            log.error("Autenticate Session error ");
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
