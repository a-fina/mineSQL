/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mineSQL.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Example account object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "DATASOURCE")
public class Datasource {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = "SOURCE", canBeNull = false)
	private String source;

	@DatabaseField(columnName = "HOST", canBeNull = false)
	private String host;

	@DatabaseField(columnName = "USER", canBeNull = false)
	private String user;

	@DatabaseField(columnName = "PASSWORD", canBeNull = true)
	private String password;

	@DatabaseField(columnName = "SHOW_ALL_DATABASE", canBeNull = true)
	private String showAll;

	@DatabaseField(columnName = "NAME", canBeNull = false)
	private String name;

	@DatabaseField(columnName = "TYPE", canBeNull = false)
	private String type;

	@DatabaseField(columnName = "URL", canBeNull = true)
	private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

	Datasource() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

    public Datasource(String name, String type, String host, String source,  String user){
        this.setName(name);
        this.setType(type);
        this.setHost(host);
        this.setSource(source);
        this.setUser(user);
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShowAll() {
        return showAll;
    }

    public void setShowAll(String showAll) {
        this.showAll = showAll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}