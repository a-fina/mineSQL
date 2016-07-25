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
import net.mineSQL.ormlite.model.Timesheet;

/**
 *
 * @author alessio.finamore
 */
public class CRUDFactory {

    private static final Logger log = Logger.getLogger(CRUDFactory.class);
    private ConnectionSource connectionSource;

    public CRUDFactory(String model, HttpServletRequest request, String... strings) throws SQLException {
        connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);

        if (model.toLowerCase().equals("report")) {
            createReport(request, strings[0]);
        } else if (model.toLowerCase().equals("database")) {
            createDatabase(request);
        } else if (model.toLowerCase().equals("timesheet")) {
            createTimesheet(request);
        }

        connectionSource.close();
    }

    public static void setupMineSQLTables() {

        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);

            // Datasource Table
            TableUtils.createTableIfNotExists(connectionSource, Datasource.class);

            // Creo la connesione al DB H2 interno
            Dao<Datasource, Integer> databaseDao;
            databaseDao = DaoManager.createDao(connectionSource, Datasource.class);

            if ( databaseDao.queryForEq("NAME", "MineSQL").isEmpty() )
            {
                Datasource db = new Datasource("MineSQL", "h2", "localhost", "file", "");
                db.setShowAll("true");
                db.setUrl(ORMLite.DATABASE_URL);
                databaseDao.create(db);
            }

            /*
            * Report Table
            */
            TableUtils.createTableIfNotExists(connectionSource, Report.class);
            /*
            * Moveo Table
            */
            TableUtils.createTableIfNotExists(connectionSource, Timesheet.class);

            
            connectionSource.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error(ex);
        }
    }

    public void createDatabase(HttpServletRequest request) throws SQLException {
        Dao<Datasource, Integer> databaseDao;
        databaseDao = DaoManager.createDao(connectionSource, Datasource.class);

        Datasource db = new Datasource(
                request.getParameter("NAME"),
                request.getParameter("TYPE"),
                request.getParameter("HOST"),
                request.getParameter("SOURCE"),
                request.getParameter("URL")
        );
        db.setPassword(request.getParameter("PASSWORD"));
        db.setUser(request.getParameter("USER"));
        db.setShowAll(request.getParameter("SHOW_ALL_DATABASE"));

        int res = databaseDao.create(db);
        log.info("Database successfully saved (" + res + "): " + db.toString());
    }

    public void createReport(HttpServletRequest request, String query) throws SQLException {
        Dao<Report, Integer> reportDao;
        reportDao = DaoManager.createDao(connectionSource, Report.class);

        Report repo = new Report(request.getParameter("NOME"), request.getParameter("NOTE"));
        repo.setHost(request.getParameter("hostName"));
        repo.setDatabase(request.getParameter("databaseName"));
        repo.setUtente("0");
        repo.setDescrizione(query);

        int res = reportDao.create(repo);
        log.info("Report successfully saved (" + res + "): " + repo.toString());
    }

    public void createTimesheet(HttpServletRequest request) throws SQLException {
        Dao<Timesheet, Integer> timesheetDao;
        timesheetDao = DaoManager.createDao(connectionSource, Timesheet.class);

        Timesheet ts  = new Timesheet(
                request.getParameter("DAY"),
                request.getParameter("HOUR"),
                request.getParameter("USER"),
                request.getParameter("ACTIVITY")
        );

        int res = timesheetDao.create(ts);
        log.info("Database successfully saved (" + res + "): " + ts.toString());
    }
}
