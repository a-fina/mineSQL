/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mineSQL.ormlite.model.hip;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Example account object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "ANAGRAFICA_AGENTI")
public class AnagraficaAgenti {

	@DatabaseField(generatedId = true, columnName = "CODICE_AGENTE")
	private int id;

	@DatabaseField(columnName = "COGNOME")
	private String cognome;

	@DatabaseField(columnName = "NOME", canBeNull = false)
	private String nome;

	@DatabaseField(columnName = "AREA", canBeNull = true)
	private String area;

	@DatabaseField(columnName = "DESCRIZIONE", canBeNull = true)
	private String descrizione;


	AnagraficaAgenti() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

    public AnagraficaAgenti(int id){
    }

    public AnagraficaAgenti(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "AnagraficaClienti{" + "id=" + id + ", cognome=" + cognome + ", nome=" + nome + ", area=" + area + ", descrizione=" + descrizione + '}';
    }

    
  
}