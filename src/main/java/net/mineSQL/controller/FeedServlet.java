package net.mineSQL.controller;

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
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

            feed.setTitle("MOVEO - AEM RSS");
            feed.setDescription("News abodut Adobe Experience Manager and Tech");
            feed.setLink("https://boma-0.herokuapp.org/slide/introduzione/");
            feed.setAuthor("Alessio Finamore");

            // creiamo la entry realtiva ad un nuovo Articolo
            java.util.List<SyndEntry> entries = new ArrayList<SyndEntry>();

            /***************************************
            ************************************/
            SyndEntry entry;
            SyndContent description;
            entry = new SyndEntryImpl();
            entry.setTitle("Talk 1");     //Titolo Contenuto
            entry.setLink("https://boma-0.herokuapp.org/slide/introduzione/");    //Hyperlink associato al contenuto  
            entry.setPublishedDate(new Date()); //Data di pubblicazione articolo
            description = new SyndContentImpl();
            description.setType("text/plain");  //Content-Type del contenuto 
            description.setValue("Talk 1 - Introduzione BOMA");    //Breve descrizione del mio articolo
            entry.setDescription(description);

            // aggiungiamo l'articolo alla lista complessiva
            entries.add(entry);

            // qui potremmo aggiungere gli altri articoli...
            feed.setEntries(entries);

            PrintWriter out = response.getWriter();
            out.println(new SyndFeedOutput().outputString(feed));
        } catch (FeedException ex) {
            log.error(ex);
        }

    }

}
