package net.mineSQL.controller;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet che gestisce le richieste dalle pagine HTML/JSP.
 * Utilizza il pattern J2EE "Front Controller".
 * @author dluciano
 */
public class FeedServlet extends HttpServlet {	

    HttpServletResponse resp = null;
    HttpServletRequest req = null;    

	
    private static final Logger log = Logger.getLogger(FeedServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");
            feed.setTitle("test-title");
            feed.setDescription("test-description");
            feed.setLink("https://example.org");
            
            PrintWriter out = response.getWriter();
            out.println(new SyndFeedOutput().outputString(feed));
        } catch (FeedException ex) {
            log.error(ex);
        }

    }

	
	
	

}
