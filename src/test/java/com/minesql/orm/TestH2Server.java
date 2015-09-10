package com.minesql.orm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.SQLException;
import junit.framework.TestCase;
import org.h2.tools.Server;

/**
 *
 * @author alessio.finamore
 */
public class TestH2Server extends TestCase {
    
    public TestH2Server(String testName) {
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
    public void testHello() throws SQLException {
        // start the TCP Server
        Server server = Server.createTcpServer().start();
        // stop the TCP Server
        server.stop(); 
    }
}
