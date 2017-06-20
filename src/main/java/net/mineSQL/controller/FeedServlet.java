package net.mineSQL.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.syndication.feed.rss.Content;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.mineSQL.connection.ORMLite;
import net.mineSQL.ormlite.model.Post;

import org.apache.log4j.Logger;

/**
 * Servlet che gestisce le richieste dalle pagine HTML/JSP. Utilizza il pattern
 * J2EE "Front Controller".
 *
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

            feed.setTitle("MOVEO - AEM RSS 1.0");
            feed.setDescription("News abodut Adobe Experience Manager and Tech");
            feed.setLink("https://boma-0.herokuapp.org/slide/introduzione/");
            feed.setAuthor("Alessio Finamore");

            // creiamo la entry realtiva ad un nuovo Articolo
            java.util.List<SyndEntry> entries = new ArrayList<SyndEntry>();

            ConnectionSource connectionSource;
            try {
                connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
                Dao<Post, Integer> feedDao;
                feedDao = DaoManager.createDao(connectionSource, Post.class);
                List<Post> allFeeds = feedDao.queryForAll();

                /************* Start Loop ************************/
                for (Post myFeed : allFeeds) {
                    SyndContent description = new SyndContentImpl();
                    SyndEntry entry = new SyndEntryImpl();

                    description.setType("text/plain");  //Content-Type del contenuto 
                    description.setValue(myFeed.getDescription());    //Breve descrizione del mio articolo
                    entry.setDescription(description);
                    
                    entry.setTitle(myFeed.getTitle());     //Titolo Contenuto
                    entry.setLink(myFeed.getLink());    //Hyperlink associato al contenuto  
                    entry.setPublishedDate(myFeed.getCreationDate());
                    entry.setAuthor(myFeed.getAuthor());
                    List<Content> lsc = new ArrayList<Content>();
                    entry.setContents(lsc);

                    // aggiungiamo l'articolo alla lista complessiva
                    entries.add(entry);
                }

                /**
                 * ********** End Loop **********************
                 */
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(FeedServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            // qui potremmo aggiungere gli altri articoli...
            feed.setEntries(entries);

            PrintWriter out = response.getWriter();
            out.println(new SyndFeedOutput().outputString(feed));
        } catch (FeedException ex) {
            log.error(ex);
        }

    }

}
