/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.menu.report;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import net.mineSQL.controller.dao.Menu;
import net.mineSQL.model.dao.Report;
import net.sf.json.JSONArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alessio.finamore
 */
public class CRUDReport {
    
    public CRUDReport() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    /****************
    @Test
    public void inserisci() throws SQLException {
    
        String path = "Z:/Finamore/";
        String dbName = "minesql_report";
        String DATABASE_URL = "jdbc:h2:file:" + path + dbName;
        // Oggetto
	    Dao<Report, Integer> reportDao;
		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
		reportDao = DaoManager.createDao(connectionSource, Report.class);
        
        // Inserisco una nuova query
		String name = "elenco anagrafica " + System.currentTimeMillis();
		String sql = "select * from TIESSEFIL.EXTAN00F";
		Report repo = new Report(name, sql);
        repo.setHost("HOST");
        repo.setDatabase("dbName");
        repo.setDescrizione(sql);
        repo.setNome(name);
        repo.setNote("NOTE: " +name);
        repo.setUtente("utente");
        
        int res = reportDao.create(repo);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void leggiElenco() throws SQLException {
        Menu menu = new Menu();
        JSONArray elenco_filtri = menu.getFiltersList(null, null);

        System.out.println(elenco_filtri);
    }
    **********/
}
