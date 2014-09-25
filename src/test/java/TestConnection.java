/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.TestCase;
import net.mineSQL.connection.ConnectionException;
import net.mineSQL.connection.ConnectionManager;

/**
 *
 * @author alessio.finamore
 */
public class TestConnection extends TestCase {
    
    public TestConnection(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws ConnectionException, SQLException {
        Connection con = ConnectionManager.getConnection("192.168.10.243", "TIESSEFIL", "TNFIAL", "ale2014");

        System.out.println("URL: " + con.getMetaData().getURL());

    }
}
