/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orm.ormlite;

import java.io.File;
import java.sql.SQLException;
import org.h2.store.fs.FileUtils;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author alessio.finamore
 */
public class TestSimple {

    public TestSimple() {
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
    // DISABLED @Test
    public void hello() throws SQLException, Exception {

        String path = "Z:/Finamore/";
        File fpath = new File(path);

        fpath.mkdirs();
        //FileUtils recursiveDelete(fpath);

        String dbName = "tata";
        String connection = "jdbc:h2:file:" + path + dbName;

        Server server = Server.createTcpServer();

        server.start();
   //     DriverManager.getConnection(connection);

        String[] args = {connection};

        connection = "jdbc:h2:~/test";
        SimpleMain.main(args);

        // stop the TCP Server
        server.stop();

    }
}
