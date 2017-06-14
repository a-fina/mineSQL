/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.util.List;
import net.mineSQL.connection.ORMLite;
import java.sql.SQLException;
import net.mineSQL.ormlite.model.FeedRSS;
import org.apache.log4j.Logger;

/**
 *
 * @author alessio.finamore
 */
public class Blog {

    private static final Logger log = Logger.getLogger(Blog.class);

    public Blog() {
    }

    public String getBlogPost() {
        ConnectionSource connectionSource;
        StringBuilder sb = new StringBuilder();
        
        try {
            connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
            Dao<FeedRSS, Integer> feedDao;
            feedDao = DaoManager.createDao(connectionSource, FeedRSS.class);
            List<FeedRSS> allFeeds = feedDao.queryForAll();

            /**
             * *********** Start Loop ***********************
             */
            for (FeedRSS myFeed : allFeeds) {
                sb.append("<div class=\"post-preview\">")
                .append("<a href=\"").append(myFeed.getLink()).append("\">")
                .append("<h2 class=\"post-title\">").append(myFeed.getTitle()).append("</h2>")
                .append("<h3 class=\"post-subtitle\">")
                .append(myFeed.getDescription())
                .append("</h3>")
                .append("</a>")
                .append("<p class=\"post-meta\">Posted by <a href=\"#\">Ax</a><!-- on September 24, 2017 --></p>")
                .append("</div>")
                .append("<hr>");
            }
        } catch (SQLException ex) {
            log.error(ex);
        }

        return sb.toString();
    }

}
