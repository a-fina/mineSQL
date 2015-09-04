/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ormlite.controller;

import net.mineSQL.ormlite.model.Report;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import net.mineSQL.connection.ORMLite;

/**
 *
 * @author alessio.finamore
 */
public class CRUDFactory {

    private static final Logger log = Logger.getLogger(CRUDFactory.class);
    
    public CRUDFactory(String model, HttpServletRequest request, String ... strings ) throws SQLException {

        if ( model.toLowerCase().equals("report") ){
            createReport(request, strings[0]);
        }else if ( model.toLowerCase().equals("database") ) {
            
        }
    }

    public void createReport(HttpServletRequest request, String query ) throws SQLException { 
        Dao<Report, Integer> reportDao;
        ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
        reportDao = DaoManager.createDao(connectionSource, Report.class);

        Report repo = new Report(request.getParameter("NOME"), request.getParameter("NOTE"));
        repo.setHost(request.getParameter("hostName"));
        repo.setDatabase(request.getParameter("databaseName"));
        repo.setUtente("0");
        repo.setDescrizione(query);
        
        int res = reportDao.create(repo);
        connectionSource.close();

        log.info("Report successfully saved: " +  query);
        
    }
}
