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
import java.util.List;
import net.mineSQL.connection.ORMLite;
import net.mineSQL.ormlite.model.Report;
import net.mineSQL.ormlite.model.Datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import net.mineSQL.connection.ConnectionException;
import net.mineSQL.connection.ConnectionManager;
import net.mineSQL.controller.MineSQL;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 *
 * @author alessio.finamore
 */
public class Menu {

    String host;
    String database;
    String user;
    String password;
    String showAllDB;
    String dbType;

    public Menu() {
    }

    Menu(String host, String database, String user, String password, String showAllDB, String dbType) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.showAllDB = showAllDB;
        this.dbType = dbType;
    }

    private static final Logger log = Logger.getLogger(Menu.class);

    public JSONArray getConnectionTableList(
            JSONArray connectionDBmenu,
            String host,
            String database,
            String user,
            String password,
            String showAllDB,
            String dbType,
            String url) throws ConnectionException, SQLException {

        Connection dbCon = null;
        dbCon = ConnectionManager.getConnection(host, database, user, password, dbType, url);

        return getConnectionTableListCALL(connectionDBmenu, dbCon, dbType, database, host);
    }

    public JSONArray getConnectionTableList(JSONArray connectionDBmenu, String host, String database, String user, String password, String showAllDB, String dbType) throws ConnectionException, SQLException {

        Connection dbCon = null;
        dbCon = ConnectionManager.getConnection(host, database, user, password, dbType);

        return getConnectionTableListCALL(connectionDBmenu, dbCon, dbType, database, host);
    }

    /*
     * Recupera l'elenco delle TABELLE di un DATABASE e costruisce un nodo del Menu
     */
    public JSONArray getConnectionTableListCALL(JSONArray connectionDBmenu, Connection dbCon, String dbType, String database, String host)
            throws ConnectionException, SQLException {

        List menuItems = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        String currentDB = "";

        String sqlSCHEMA = MineSQL.getSchemaList(dbType, false, database);

        ps = dbCon.prepareStatement(sqlSCHEMA);
        log.debug("Database DB2 menu: " + sqlSCHEMA);
        //ResultSet.TYPE_SCROLL_SENSITIVE,
        //ResultSet.CONCUR_READ_ONLY);
        rs = ps.executeQuery();

        // Mappo tutti i menuItems
        String title = "";
        String tip = "";
        int idRow = 0;
        List savedTable = new ArrayList();

        while (rs.next()) {

            HashMap anagraf_map = new HashMap();
            anagraf_map.put("id", "idRow" + idRow);
            log.debug(" row: " + rs.toString());
            //Gli spazi &nbsp; servono per fissare un Bug di CSS di firefox
            title = rs.getString(1) + "&nbsp;&nbsp;&nbsp;&nbsp;";
            tip = host + " - " + dbType + " - " + database; 
            title = "<span ext:qtip=\"" + tip + "\">" + title + "</span>";

            anagraf_map.put("nome", title);
            menuItems.add(anagraf_map);
            savedTable.add(rs.getString(1));
            idRow++;
        }
        rs.close();
        ps.close();

        // Scandisco i menuItems e prendo le queries
        Iterator iter = menuItems.iterator();
        Iterator tablesIter = savedTable.iterator();

        // Ciclo di costruzione menu principale, cicla sull'elenco DB
        while (iter.hasNext()) {
            HashMap anagraf_map = new HashMap();
            anagraf_map = (HashMap) iter.next();

            currentDB = (String) tablesIter.next();

            String sqlTABLE = MineSQL.getTableList(dbType, currentDB);
            //if (currentDB.contains("-"));
            //    continue;
            ps = dbCon.prepareStatement(sqlTABLE, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();

            JSONArray gruppo_menu = new JSONArray();
            String text = "";
            // Ciclo di costruzione sottoMenu, per ogni tabella fa elenco dei campi
            int idUniq = 0;
            String id = "";
            String text2 = "";
            String textTip = "";

            while (rs.next()) {
                // Attacco elenco dei campi del database
                id = idUniq + "##" + currentDB + "##" + rs.getString(1) + "##" + host;
                //MySQL text2 = rs.getString("Tables_in_" + currentDB);
                textTip = "<span ext:qtip=\"" + rs.getString(2) + "\">" + rs.getString(1) + "</span>";
                gruppo_menu.add(getSubMenuItem(id, rs.getString(1), textTip));
                //log.debug(" gruppo_menu=" + getMenuItem(text2, date_grafico, null) );
                idUniq++;
            }
            rs.close();
            ps.close();

            // Attacco elenco dei databases
            if (gruppo_menu.size() > 0) {
                text = anagraf_map.get("nome").toString();
                connectionDBmenu.add(getMenuItem(text, gruppo_menu, null));
            }

            log.debug(" currentDB 3: " + currentDB);
        }
        if (dbCon != null) {
            dbCon.close();
        }

        return connectionDBmenu;
    }

    /**
     * Check if current user role is autorized for the menu
     *
     * @param String roles "1,2,3,4,5,...".
     * @param String user "1,2,3,4,5,...".
     * @return true o false
     */
    private boolean isAutorized(String roles, String user) {
        boolean autorized = false;

        if (roles == null) {
            roles = "";
        }

        String[] ro = roles.split(",");

        for (int i = 0; i < ro.length; i++) {
            if (ro[i].equals(user)) {
                autorized = true;
            }
        }

        return autorized;
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

    /**
     * Costruisce un nodo del mainMenu
     */
    public JSONObject getMenuItem(String text, JSONArray children, String id) {
        return getMenuItem(text, children, id, "false");
    }

    /**
     * Costruisce un nodo del mainMenu
     */
    public JSONObject getMenuItem(String text, JSONArray children, String id, String disabled) {
        JSONObject menu = new JSONObject();
        menu.put("text", text);
        menu.put("expanded", "false");
        menu.put("iconCls", "none");
        menu.put("disabled", disabled);

        if (id != null) {
            menu.put("id", id);
        }
        if (children != null) {
            menu.put("children", children);
        }

        return menu;
    }

    /**
     * Torna il sottomenu specificato nel parametro path a partire dalla root
     * del menu. Il path ha un formato del tipo /Menu liv 1/Menu liv 2/...
     */
    private JSONArray getSubMenu(JSONArray menu, String path) {
        JSONArray currentMenu = menu;
        JSONObject currentNode = null;

        String[] nodes = path.replaceAll("//*/", "/").split("/");
        // Se il path e' vuoto esco subito
        if (path.length() == 0 || nodes.length == 0) {
            return currentMenu;
        }

        int liv = 1;
        if (currentMenu.isEmpty()) // Se il menu e' vuoto, inutile fare ricerche, aggiungo 
        // il nodo di primo livello indicato nel path
        {
            currentMenu.add(getMenuItem(nodes[liv] + "&nbsp;&nbsp;&nbsp;&nbsp;", new JSONArray(), null));
        }

        // Ciclo di ricerca che effettua il best matching del path ricercato
        // nella struttura di menu esistente, scendendo uno alla volta i livelli
        // di menu partendo dal primo.
        boolean isMatching = true;
        while (isMatching && liv < nodes.length) {
            int itemProgr = 0;
            boolean found = false;
            if (currentMenu != null) {
                // Se il sottomenu corrente non e' vuoto si scorrono gli items
                // per verificare l'esistenza del nodo di livello LIV indicato nel path
                while (!found && itemProgr < currentMenu.size()) {
                    currentNode = (JSONObject) currentMenu.get(itemProgr);
                    String node = (String) currentNode.get("text");
                    if (node.equals(nodes[liv] + "&nbsp;&nbsp;&nbsp;&nbsp;")) {
                        found = true;
                    }
                    itemProgr++;
                }
            }
            if (!found) {
                // Se non viene trovato il nodo di livello LIV indicato nel path
                // bisogna interrompere il ciclo di ricerca, inutile scendere di livello
                isMatching = false;
            } else {
                // Se viene trovato il nodo di livello LIV indicato nel path,
                // si passa a considerare il sottomenu di livello LIV + 1
                currentMenu = (JSONArray) currentNode.get("children");
                liv++;
            }
        }

        // Il best-matching ha verificato l'esistenza della struttura menu fino a
        // livello LIV. Si passa a creare i livelli di profondita' maggiore mancanti.
        for (int i = liv; i < nodes.length; i++) {
            currentMenu.add(getMenuItem(nodes[i] + "&nbsp;&nbsp;&nbsp;&nbsp;", new JSONArray(), null));
            currentNode = (JSONObject) currentMenu.get(currentMenu.size() - 1);
            currentMenu = (JSONArray) currentNode.get("children");
        }

        return currentMenu;
    }

    /**
     * *****
     * public JSONArray getFiltersListOLDSQL(String entity, String idUtente)
     * throws SQLException { // Database init PreparedStatement ps = null;
     * ResultSet rs = null;
     *
     * ps = con.prepareStatement("select * from msq_FILTRI_T where idutente = "
     * + idUtente /*+ " AND IDFLUSSO = " + entity* /); rs = ps.executeQuery();
     * JSONArray elenco_filtri = new JSONArray();
     *
     * // Ciclo di costruzione sottoMenu, elenco delle queries associate ad
     * ogni reparto while (rs.next()) { //if ( isAutorized(
     * rs.getString("IDRUOLO"), session.getAttribute("IDRUOLO").toString() ) ) {
     * // Costruzione singolo elemento del sottoMenu String id =
     * rs.getString("ID") + "##" + rs.getString("DATABASE") + "##" + "non_serve"
     * + "##" + rs.getString("HOST");
     *
     * String title = rs.getString("NOME"); //filtri_map.get("nome") + " - " +
     * rs.getString("NOME");
     *
     * String tooltip = rs.getString("NOTE"); if (rs.wasNull()) { tooltip = "";
     * } String text = "<span ext:qtip=\"" + tooltip + "\">" + title +
     * "</span>";
     *
     * elenco_filtri.add(getSubMenuItem(id, title, text)); //} }
     *
     * rs.close(); ps.close(); return elenco_filtri; } **********************
     */
    /**
     * Esegue la query e restituisce l'elenco dei grafi TODO: per utente
     */
    public JSONArray getGraphList(String userGroup) throws SQLException, ConnectionException {
        // Database init
        PreparedStatement ps = null;
        ResultSet rs = null;


        /* String sqlChartMenu = " select sto.idgrafico, ana.nome, ana.descrizione, ana.titolo, " +
         " tip.nome as tipo, sto.data_elaborazione " +
         " from dm_stat_anagrafica_grafici_t ana, dm_stat_grafici_t sto, "+
         " dm_gruppo_t gro, dm_stat_anagr_grafici_gruppo_r gra, " + 
         " dm_stat_anagr_grafici_gruppo_r gra, " + 
         " dm_stat_tipi_grafici_t tip " +
         " where ana.idanagrafica =  sto.idanagrafica and " +
         " ana.idanagrafica = gra.idanagrafica and " + 
         " ana.idtipo = tip.idtipo "; // and
         " gro.idgruppo = gra.idgruppo and gro.nome like '" + userGroup + "'";
         */
        String sqlChartMenu
                = " select sto.idgrafico, ana.nome, ana.descrizione, ana.titolo,  tip.nome as tipo, sto.data_elaborazione  "
                + " from dm_stat_anagrafica_grafici_t ana, dm_stat_grafici_t sto,    dm_stat_tipi_grafici_t tip  "
                + " where ana.idanagrafica =  sto.idanagrafica  and  ana.idtipo = tip.idtipo ";

        log.debug(" sqlChartMenu: " + sqlChartMenu);

        // TODO: da provare nuova connession
        Connection con = ConnectionManager.getConnection(host, database, user, password, dbType);
        ps = con.prepareStatement(sqlChartMenu);
        rs = ps.executeQuery();
        JSONArray elenco_filtri = new JSONArray();

        // Ciclo di costruzione sottoMenu, elenco delle queries associate ad ogni reparto
        while (rs.next()) {
            //if ( isAutorized( rs.getString("IDRUOLO"), session.getAttribute("IDRUOLO").toString()  ) ) {
            // Costruzione singolo elemento del sottoMenu
            String id = rs.getString("IDGRAFICO") + "##" + rs.getString("TIPO");
            String title = rs.getString("NOME"); //filtri_map.get("nome") + " - " + rs.getString("NOME");
            String tooltip = rs.getString("DESCRIZIONE") + " - " + rs.getString("DATA_ELABORAZIONE");
            if (rs.wasNull()) {
                tooltip = "";
            }
            String text = "<span ext:qtip=\"" + tooltip + "\">" + title + "</span>";

            elenco_filtri.add(getSubMenuItem(id, title, text));
            //}
        }

        rs.close();
        ps.close();
        return elenco_filtri;
    }

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

    public JSONArray getDatabaseList() throws SQLException {
        JSONArray mainMenu = new JSONArray();

        Dao<Datasource, Integer> datasourceDao;

        ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
        datasourceDao = DaoManager.createDao(connectionSource, Datasource.class);

        TableUtils.createTableIfNotExists(connectionSource, Report.class);

        List<Datasource> datasource = datasourceDao.queryForAll();
        for (Datasource ds : datasource) {

            try {
                // Connesione tramite URL o tramite CREDENZIALI
                if ( ds.getUrl() != null && !ds.getUrl().isEmpty()) {

                    mainMenu = getConnectionTableList(
                            mainMenu,
                            ds.getHost(),
                            ds.getSource(),
                            ds.getUser(),
                            ds.getPassword(), ds.getShowAll(), ds.getType(), ds.getUrl());
                } else {
                    mainMenu = getConnectionTableList(
                            mainMenu,
                            ds.getHost(),
                            ds.getSource(),
                            ds.getUser(),
                            ds.getPassword(), ds.getShowAll(), ds.getType());
                }

            } catch (Exception ex) {
                log.error(ex);
                mainMenu.add(getMenuItem("Impossibile connettersi a " + ds.getHost() + " " + ds.getSource(), null, "disableMe"));
            }
            // Stampo l'errore di accesso negato
            if (mainMenu.isEmpty()) {
                mainMenu.add(getMenuItem("Non sono presenti connesioni ", null, "disableMe"));
            }

        }

        return mainMenu;
    }
}
