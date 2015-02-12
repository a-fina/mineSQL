/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mineSQL.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;

/**
 * Example account object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "REPORT")
public class Report {

	// for QueryBuilder to be able to find the fields
	public static final String NAME_FIELD_NAME = "name";
	public static final String PASSWORD_FIELD_NAME = "passwd";

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(columnName = "DATABASE")
	private String database;

	@DatabaseField(columnName = "HOST")
	private String host;

	@DatabaseField(columnName = "UTENTE", canBeNull = false)
	private String utente;

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

	@DatabaseField(columnName = "NOTE", canBeNull = false)
	private String note;

	@DatabaseField(columnName = "NOME", canBeNull = false)
	private String nome;

	@DatabaseField(columnName = "DESCRIZIONE", canBeNull = false)
	private String descrizione;

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
        hash = 41 * hash + Objects.hashCode(this.database);
        hash = 41 * hash + Objects.hashCode(this.host);
        hash = 41 * hash + Objects.hashCode(this.note);
        hash = 41 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Report other = (Report) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.database, other.database)) {
            return false;
        }
        if (!Objects.equals(this.host, other.host)) {
            return false;
        }
        if (!Objects.equals(this.note, other.note)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Report(int id, String database, String host) {
        this.id = id;
        this.database = database;
        this.host = host;
    }
    public Report(String nome, String note) {
        this.nome = nome;
        this.note = note;
    }

	Report() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}
}