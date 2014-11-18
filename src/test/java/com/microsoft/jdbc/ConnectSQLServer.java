/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microsoft.jdbc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alessio.finamore
 */
public class ConnectSQLServer {

    public ConnectSQLServer() {
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
    public void hello() {
        Connection connection = null;
        //1.jdbc driver name
        String SQL_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        // 2. Database URL, V_UDAY\FRAMEWORK is ServerName and JSF is DataBase name
        String URL = "jdbc:sqlserver://192.168.100.20:1433;instanceName=SYNERGY;databaseName=emc_inaz";
        //3.Database credentials
        String USERNAME = "SINTECO\\dnlmhl";//UserName
        String PASSWORD = "";//Password
        USERNAME = "SA";//UserName
        PASSWORD = "Pw@SQLSynergy";//Password

        try {
            Class.forName(SQL_JDBC_DRIVER);// Register jdbc driver

            System.out.println("****Connect to Database****");

            //4. open a connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            System.out.println("DataBase connect to: " + connection.getMetaData().getDriverName());
            System.out.println("URL: " + connection.getMetaData().getURL());

            if (connection != null) {
                connection.close();
            }
            System.out.println("Database Connection Closed");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception in getLocalConeection() " + e.getMessage());
        }

    }
}
