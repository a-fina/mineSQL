/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author alessio.finamore
 */
@DatabaseTable(tableName = "FEEDRSS")
public class FeedRSS {

	@DatabaseField(generatedId = true)
    private int id;

	@DatabaseField(columnName = "TITLE")
	private String title;
	@DatabaseField(columnName = "LINK")
	private String link;
	@DatabaseField(columnName = "DESCRIPTION")
	private String description;
    
    public FeedRSS() {
    }

    public FeedRSS(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesxcription() {
        return description;
    }

    public void setDesxcription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FeedRSS{" + "id=" + id + ", title=" + title + ", link=" + link + ", description=" + description + '}';
    }

}
