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
import java.util.ArrayList;
import net.mineSQL.ormlite.model.Post;
import org.apache.log4j.Logger;

/**
 *
 * @author alessio.finamore
 */
public class Blog {

    private static final Logger log = Logger.getLogger(Blog.class);

    public Blog() {

    }

    public Post getBlogPost(String link) {
        ConnectionSource connectionSource;
        StringBuilder sb = new StringBuilder();
        List<Post> post= new ArrayList<>();

        try {
            connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
            Dao<Post, Integer> feedDao;
            feedDao = DaoManager.createDao(connectionSource, Post.class);
            post = feedDao.queryForEq("LINK", link);
    
            log.debug("POST queryForEq LINK:" + link + " found: " + post);
            
            if (post.size() != 1)
                throw new SQLException("Invalid TITLE, more than one post was found");

        } catch (SQLException ex) {
            log.error(ex);
        }

        return post.get(0);
    }


    public String getBlogPosts() {
        ConnectionSource connectionSource;
        StringBuilder sb = new StringBuilder();
        
        try {
            connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
            Dao<Post, Integer> feedDao;
            feedDao = DaoManager.createDao(connectionSource, Post.class);
            List<Post> allFeeds = feedDao.queryForAll();

            /**
             * *********** Start Loop ***********************
                TODO:
                https://github.com/spullara/mustache.java
             */
            for (Post myFeed : allFeeds) {
                sb.append("<div class=\"post-preview\">")
                .append("<a href=\"post.html?link=").append(myFeed.getLink()).append("\">")
                .append("<h2 class=\"post-title\">").append(myFeed.getTitle()).append("</h2>")
                .append("<h3 class=\"post-subtitle\">")
                .append(myFeed.getDescription())
                .append("</h3>")
                .append("</a>")
                .append("<p class=\"post-meta\">Posted by <a href=\"#\">&nbsp;")
                .append(myFeed.getAuthor())
                .append("</a>")
                .append(myFeed.getCreationDate())
                .append("</p>")
                .append("</div>")
                .append("<hr>");
            }
        } catch (SQLException ex) {
            log.error(ex);
        }

        return sb.toString();
    }

}
