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
public class ReadReport {

    public ReadReport() {
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
    public void inserisciErileggi() throws SQLException, Exception {

        String path = "Z:/Fina/";
        String dbName = "minesql_report";
        String DATABASE_URL = "jdbc:h2:file:" + path + dbName;

//        File fpath = new File(path);

   //     fpath.mkdirs();
        //FileUtils recursiveDelete(fpath);

   //     Server server = Server.createTcpServer();
   //     server.start();
   //    DriverManager.getConnection(connection);

        //String[] args = {DATABASE_URL};
        //DATABASE_URL = "jdbc:h2:~/test";
        
        // Oggetto
	    Dao<Report, Integer> reportDao;
		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
		reportDao = DaoManager.createDao(connectionSource, Report.class);
		
        // Rileggo la query appena inserita
		List<Report> repos = reportDao.queryForAll();
        int last = repos.size() -1;
        for (Report repo : repos) {
                System.out.println(" Repo ID: " + repo.getId() );
                System.out.println(" Repo Name: " + repo.getNome() );
                assertEquals(repo.getNome(), reportDao.queryForId(repo.getId()).getNome());
                System.out.println(" Repo Text: " + repo.getDescrizione());
        }

        
       connectionSource.close();
        
        // stop the TCP Server
     //   server.stop();

    }

}
