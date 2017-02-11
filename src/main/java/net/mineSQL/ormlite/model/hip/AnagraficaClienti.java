/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mineSQL.ormlite.model.hip;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;
import net.mineSQL.ormlite.model.User;

/**
 * Example account object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "ANAGRAFICA_CLIENTI")
public class AnagraficaClienti {

	@DatabaseField(generatedId = true, columnName = "CODICE_CLIENTE")
	private int id;

	@DatabaseField(columnName = "COGNOME")
	private String cognome;

	@DatabaseField(columnName = "NOME", canBeNull = false)
	private String nome;

	@DatabaseField(columnName = "AREA", canBeNull = true)
	private String area;

	@DatabaseField(columnName = "DESCRIZIONE", canBeNull = true)
	private String descrizione;


	AnagraficaClienti() {
    }


    public AnagraficaClienti(String nome, String cognome, String area, String descrizione) {
        this.nome = nome;
        this.cognome = cognome;
        this.area = area;
        this.descrizione = descrizione;
    }

    public AnagraficaClienti(int codiceCliente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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