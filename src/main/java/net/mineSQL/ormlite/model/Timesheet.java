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
@DatabaseTable(tableName = "TIMESHEET")
public class Timesheet {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = "DAY", canBeNull = false)
	private String day;

	@DatabaseField(columnName = "HOUR", canBeNull = false)
	private String  hour;

	@DatabaseField(columnName = "USER", canBeNull = false)
	private String user;

	@DatabaseField(columnName = "ACTIVITY", canBeNull = true)
	private String activity;

    Timesheet() {
    }

    
    public Timesheet(String day, String hour, String user, String activity) {
        this.day = day;
        this.hour = hour;
        this.user = user;
        this.activity = activity;
    }


    
    
    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getUser() {
        return user;
    }

    public String getActivity() {
        return activity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "Timesheet{" + "id=" + id + ", day=" + day + ", hour=" + hour + ", user=" + user + ", activity=" + activity + '}';
    }

}