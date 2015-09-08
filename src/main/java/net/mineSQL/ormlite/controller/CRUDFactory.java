/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.controller;

import net.mineSQL.ormlite.model.Report;
import net.mineSQL.ormlite.model.Datasource;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import net.mineSQL.connection.ORMLite;

/**
 *
 * @author alessio.finamore
 */
public class CRUDFactory {

    private static final Logger log = Logger.getLogger(CRUDFactory.class);
    private ConnectionSource connectionSource;
   
    public CRUDFactory(String model, HttpServletRequest request, String ... strings ) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);

        if ( model.toLowerCase().equals("report") ){
            createReport(request, strings[0]);
        }else if ( model.toLowerCase().equals("database") ) {
            createDatabase(request);
        }

        connectionSource.close();
    }
    public static  void setupMineSQLTables()  { 
        
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
            // if you need to create the table
            TableUtils.createTableIfNotExists(connectionSource, Datasource.class);
            TableUtils.createTableIfNotExists(connectionSource, Report.class);
            
            connectionSource.close();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    public void createDatabase(HttpServletRequest request) throws SQLException { 
        Dao<Datasource, Integer> databaseDao;
        databaseDao = DaoManager.createDao(connectionSource, Datasource.class);

        Datasource db =  new Datasource(
                request.getParameter("NOME"),
                request.getParameter("TYPE"),
                request.getParameter("HOST"),
                request.getParameter("SOURCE")
        );
        db.setPassword(request.getParameter("PASSWORD"));
        db.setUser(request.getParameter("USER"));
        db.setShowAll(request.getParameter("SHOW_ALL_DATABASE"));

        int res = databaseDao.create(db);
        log.info("Database successfully saved ("+ res +"): " +  db.toString() );
    }

    public void createReport(HttpServletRequest request, String query ) throws SQLException { 
        Dao<Report, Integer> reportDao;
        reportDao = DaoManager.createDao(connectionSource, Report.class);

        Report repo = new Report(request.getParameter("NOME"), request.getParameter("NOTE"));
        repo.setHost(request.getParameter("hostName"));
        repo.setDatabase(request.getParameter("databaseName"));
        repo.setUtente("0");
        repo.setDescrizione(query);
        
        int res = reportDao.create(repo);
        log.info("Report successfully saved (" + res + "): " +  repo.toString() );
    }
}
