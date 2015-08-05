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
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import net.mineSQL.connection.ORMLite;
import net.mineSQL.ormlite.model.Report;
import org.h2.store.fs.FileUtils;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author alessio.finamore
 */
public class TestReport {

    String DATABASE_URL = ORMLite.DATABASE_URL;

    public TestReport() {
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

    @Test
    public void dropAndCreate() throws SQLException {

        // Oggetto
	    Dao<Report, Integer> reportDao;
		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
		reportDao = DaoManager.createDao(connectionSource, Report.class);
		
		TableUtils.dropTable(connectionSource, Report.class, true);
        // Crea Tabella
		TableUtils.createTableIfNotExists(connectionSource, Report.class);

    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void inserisciErileggi() throws SQLException, Exception {


        File fpath = new File(ORMLite.path);

        fpath.mkdirs();
        //FileUtils recursiveDelete(fpath);

        Server server = Server.createTcpServer();
        server.start();
   //     DriverManager.getConnection(connection);

        //String[] args = {DATABASE_URL};
        //DATABASE_URL = "jdbc:h2:~/test";
        
        // Oggetto
	    Dao<Report, Integer> reportDao;
		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
		reportDao = DaoManager.createDao(connectionSource, Report.class);
		
        // Crea Tabella
		TableUtils.createTableIfNotExists(connectionSource, Report.class);

        // Inserisco una nuova query
		String name = "elenco anagrafica " + System.currentTimeMillis();
        name = "O";
		String sql = "select * from TABLE";
		Report repo = new Report(name, sql);
        repo.setUtente("utento");
        repo.setDescrizione("utento descriziono");
        reportDao.create(repo);

        // Rileggo la query appena inserita
		List<Report> repos = reportDao.queryForAll();
        int last = repos.size() -1;
        assertEquals(name,  repos.get(last).getNome() );
      
        // Read Loop
        for (Report repo2 : repos) {
                System.out.println(" Repo ID: " + repo2.getId() );
                System.out.println(" Repo Name: " + repo2.getNome() );
                assertEquals(repo.getNome(), reportDao.queryForId(repo2.getId()).getNome());
                System.out.println(" Repo Text: " + repo2.getDescrizione());
        }
       connectionSource.close();
       
        // stop the TCP Server
        server.stop();

    }

}
