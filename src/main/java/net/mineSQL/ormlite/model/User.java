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
    private String id;

	@DatabaseField(columnName = "NOME")
	private String nome;
	@DatabaseField(columnName = "EMAIL")
	private String email;
	@DatabaseField(columnName = "PASSWORD")
	private String password;
    
    @ForeignCollectionField
    ForeignCollection<Report> reports;


    public User() {
    }

    public User(String nome, String password) {
        this.nome = nome;
        this.password = password;
    }

    public ForeignCollection<Report> getReports() {
        return reports;
    }

    public void setReports(ForeignCollection<Report> reports) {
        this.reports = reports;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
