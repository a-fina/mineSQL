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
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import net.mineSQL.model.dao.Report;
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
public class TableCreateDrop {
    
    public TableCreateDrop() {
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
    @Test
    public void hello() throws SQLException {
        String path = "Z:/Fina/";
        String dbName = "minesql_report";
        String DATABASE_URL = "jdbc:h2:file:" + path + dbName;

        // Oggetto
	    Dao<Report, Integer> reportDao;
		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
		reportDao = DaoManager.createDao(connectionSource, Report.class);
		
		TableUtils.dropTable(connectionSource, Report.class, true);
        // Crea Tabella
		TableUtils.createTableIfNotExists(connectionSource, Report.class);

    }
}
