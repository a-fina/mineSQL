/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minesql.orm;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.mineSQL.connection.ORMLite;
import net.mineSQL.ormlite.model.Datasource;
import net.mineSQL.ormlite.model.Report;
import org.h2.jdbc.JdbcSQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alessio.finamore
 */
public class TestH2Client {

    public TestH2Client() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(ORMLite.DATABASE_URL);

        Statement stat = conn.createStatement();

        // this line would initialize the database
        // from the SQL script file 'init.sql'
        // stat.execute("runscript from 'init.sql'");
        try {
            stat.execute("create table test(id int primary key, name varchar(255))");
            stat.execute("insert into test values(1, 'Hello')");
        } catch (JdbcSQLException ex) {
            System.out.println("Table exist");
        }

        ResultSet rs;
        rs = stat.executeQuery("select * from test");
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        stat.close();

        // add application code here
        conn.close();
    }

    @Test
    public void insertDatasource() throws SQLException {
        ConnectionSource connectionSource2 = new JdbcConnectionSource(ORMLite.DATABASE_URL);

        TableUtils.createTableIfNotExists(connectionSource2, Report.class);
        TableUtils.createTableIfNotExists(connectionSource2, Datasource.class);

        // Creo la connesione al DB H2 interno
        Dao<Datasource, Integer> databaseDao;
        databaseDao = DaoManager.createDao(connectionSource2, Datasource.class);

        Datasource db = new Datasource( "MineSQL", "h2", "localhost", "file", "");
        db.setShowAll("true");
        db.setUrl(ORMLite.DATABASE_URL);
        databaseDao.createIfNotExists(db);

        connectionSource2.close();
    }

    @Test
    public void selectSchema() throws SQLException, ClassNotFoundException{

        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(ORMLite.DATABASE_URL);

        Statement stat = conn.createStatement();

        ResultSet rs;
        rs = stat.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES ");
        rs = stat.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLLATIONS WHERE 1=1 LIMIT 1 ");
        while (rs.next()) {
            System.out.println("-------->" + rs.getString(1) );
        }
        stat.close();

        // add application code here
        conn.close();
    }
}
