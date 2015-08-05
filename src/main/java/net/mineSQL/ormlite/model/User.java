/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author alessio.finamore
 */
@DatabaseTable(tableName = "USER")
public class User {

	@DatabaseField(id = true)
    private String name;

    @ForeignCollectionField
    ForeignCollection<Report> reports;
    public ForeignCollection<Report> getReports() {
        return reports;
    }

    public void setReports(ForeignCollection<Report> reports) {
        this.reports = reports;
    }

    @DatabaseField(canBeNull = false)
    private String password;


    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
