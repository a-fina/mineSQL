package net.mineSQL.app;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import net.mineSQL.ormlite.controller.CRUDFactory;

public class Boot extends HttpServlet {

    Logger log = Logger.getLogger(Boot.class);

    @Override
    public void init() throws ServletException {
        // Start Logger
        loggerInit();

        // Start Scheduler
        //    ApplicationWatcher.startScheduler();

        // Create MineSQL Internal Datatabase
       databaseInit(); 

        super.init();
    }

    @Override
    public void destroy() {
        
        // Start Scheduler
        //     ApplicationWatcher.stopScheduler();

        super.destroy();
    }

    private void loggerInit() {

        String prefix = getServletContext().getRealPath("/");
        String propertiesFile = prefix + getInitParameter("log4j-init-file");
        String logFile = prefix + getInitParameter("log4j-log-file");

        // if the log4j-init-propertiesFile is not set, then no point in trying
        try {
            System.setProperty("Log4jLogFile", logFile);

            if (propertiesFile != null) {
                PropertyConfigurator.configure(propertiesFile);
            }

        } catch (Exception ex) {
            return;
        }

        log.debug("Test Livello DEBUG");
        log.info("Test Livello INFO");
        log.warn("Test Livello WARNING");
        log.error("Test Livello ERROR");
        log.fatal("Test Livello FATAL");
    }

    
    /*
    * Setup MineSQL Internal Database
    */
    private void databaseInit() {
        /*
        * MineSQL
        */
        CRUDFactory.setupMineSQLTables();
        log.info("MineSQL Internal Database Ready");
        // TODO: configure module custom for each Aapplication/Customer
        /*
        * Moveo
        */
    }


}
