/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.model.hip;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;

/**
 *
 * @author alessio.finamore
 */
@DatabaseTable(tableName = "ORDINI")
public class Ordini {
	@DatabaseField(generatedId = true, columnName = "NUMERO_ORDINE")
    private int numeroOrdine;

	@DatabaseField(columnName = "DATA", canBeNull = true)
	private String data;
    
	@DatabaseField(columnName = "NOTE", canBeNull = true)
	private String note;

    @ForeignCollectionField
    ForeignCollection<ListinoProdotti> prodotti;

    @DatabaseField(canBeNull = false, foreign = true)
    private AnagraficaAgenti agente;

    @DatabaseField(canBeNull = false, foreign = true)
    private AnagraficaClienti cliente;

    public Ordini() {
    }

    
    public Ordini(String data, String note, ForeignCollection<ListinoProdotti> prodotti, AnagraficaAgenti agente, AnagraficaClienti cliente) {
        this.data = data;
        this.note = note;
        this.prodotti = prodotti;
        this.agente = agente;
        this.cliente = cliente;
    }

    public int getNumeroOrdine() {
        return numeroOrdine;
    }

    public void setNumeroOrdine(int numeroOrdine) {
        this.numeroOrdine = numeroOrdine;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ForeignCollection<ListinoProdotti> getProdotti() {
        return prodotti;
    }

    public void setProdotti(ForeignCollection<ListinoProdotti> prodotti) {
        this.prodotti = prodotti;
    }

    public AnagraficaAgenti getAgente() {
        return agente;
    }

    public void setAgente(AnagraficaAgenti agente) {
        this.agente = agente;
    }

    public AnagraficaClienti getCliente() {
        return cliente;
    }

    // http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Foreign-Objects
    public void setCliente(AnagraficaClienti cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.numeroOrdine;
        hash = 97 * hash + Objects.hashCode(this.data);
        hash = 97 * hash + Objects.hashCode(this.note);
        hash = 97 * hash + Objects.hashCode(this.prodotti);
        hash = 97 * hash + Objects.hashCode(this.agente);
        hash = 97 * hash + Objects.hashCode(this.cliente);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ordini other = (Ordini) obj;
        if (this.numeroOrdine != other.numeroOrdine) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.note, other.note)) {
            return false;
        }
        if (!Objects.equals(this.prodotti, other.prodotti)) {
            return false;
        }
        if (!Objects.equals(this.agente, other.agente)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        return true;
    }

}
