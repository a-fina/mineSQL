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
@DatabaseTable(tableName = "LISTINO_PRODOTTI")
public class ListinoProdotti {

	@DatabaseField(generatedId = true, columnName = "CODICE_PRODOTTO")
	private int id;

	@DatabaseField(columnName = "NOME", canBeNull = false)
	private String nome;

	@DatabaseField(columnName = "DESCRIZIONE", canBeNull = true)
	private String descrizione;

	@DatabaseField(columnName = "PREZZO", canBeNull = true)
	private int prezzo;

    @DatabaseField(canBeNull = false, foreign = true)
    private Ordini ordine;

	ListinoProdotti() {
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

    public ListinoProdotti(int id, String database, String host) {
    }
    public ListinoProdotti(String nome, String descrizione, int prezzo) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

  
}