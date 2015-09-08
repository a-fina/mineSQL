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

	@DatabaseField(columnName = "NAME", canBeNull = true)
	private String name;

	@DatabaseField(columnName = "TYPE", canBeNull = false)
	private String type;



	Datasource() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

    public Datasource(String name, String type, String host, String source){
        this.setName(name);
        this.setType(type);
        this.setHost(host);
        this.setSource(source);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
        hash = 71 * hash + (this.source!= null ? this.source.hashCode() : 0);
        hash = 71 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 71 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 71 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 71 * hash + (this.showAll != null ? this.showAll.hashCode() : 0);
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Database{" + "id=" + id + ", source=" + source+ ", host=" + host + ", user=" + user + ", password=" + password + ", showAll=" + showAll + ", name=" + name + ", type=" + type + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Datasource other = (Datasource) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
            return false;
        }
        if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
            return false;
        }
        if ((this.user == null) ? (other.user != null) : !this.user.equals(other.user)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if ((this.showAll == null) ? (other.showAll != null) : !this.showAll.equals(other.showAll)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        return true;
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