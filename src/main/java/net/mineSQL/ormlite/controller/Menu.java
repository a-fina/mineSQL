/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;
import net.mineSQL.connection.ConnectionManager;
import net.mineSQL.connection.ORMLite;
import net.mineSQL.ormlite.model.Report;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author alessio.finamore
 */
public class Menu {

    /**
     * Esegue la query e restituisce l'elenco dei filtri per una specifica entiy
     */
    public JSONArray getFiltersList(String entity, String idUtente) throws SQLException {
        JSONArray elenco_filtri = new JSONArray();

        Dao<Report, Integer> reportDao;

        ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
        reportDao = DaoManager.createDao(connectionSource, Report.class);  
    
		TableUtils.createTableIfNotExists(connectionSource, Report.class);
        
        List<Report> repos = reportDao.queryForAll();
        for (Report repo : repos) {
            //if ( isAutorized( rs.getString("IDRUOLO"), session.getAttribute("IDRUOLO").toString()  ) ) {
            // Costruzione singolo elemento del sottoMenu
            String id = repo.getId()
                    + "##" + repo.getDatabase()
                    + "##" + "non_serve"
                    + "##" + repo.getHost();

            String title = repo.getNome();//filtri_map.get("nome") + " - " + rs.getString("NOME");
            String tooltip = repo.getNote();
            //if (rs.wasNull())
            //   tooltip = "";
            String text = "<span ext:qtip=\"" + tooltip + "\">" + title + "</span>";

            elenco_filtri.add(getSubMenuItem(id, title, text));
            //}
        }

        if (connectionSource != null) {
            connectionSource.close();
        }

        return elenco_filtri;
    }

    /**
     * Costruisce un nodo del sottoMenu
     */
    private JSONObject getSubMenuItem(String id, String title, String text) {
        return getSubMenuItem(id, title, text, "false");
    }

    /**
     * Costruisce un nodo del sottoMenu
     */
    private JSONObject getSubMenuItem(String id, String title, String text, String disabled) {
        JSONObject sub = new JSONObject();
        sub.put("text", text);
        sub.put("id", id);
        sub.put("title", title);
        sub.put("cls", "file");
        sub.put("iconCls", "none");
        sub.put("leaf", "false");
        sub.put("disabled", disabled);

        return sub;
    }
}
