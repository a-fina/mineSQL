/*
 File : ApplicationWatcher.java
 */
package net.mineSQL.util;

import java.util.Locale;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import net.mineSQL.quartz.jobs.MineScheduler;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import org.apache.log4j.Logger;

public class ApplicationWatcher implements ServletContextListener {

    private static final Logger log = Logger.getLogger(ApplicationWatcher.class);

    public static String applicationStarDate = "";
    public static String hostName = "";
    public static String version = "";
    public static String tmpPath = "";
    public static String dateformat = "dd-MMM-yy HH:mm:ss";
    public static Locale locale = Locale.ENGLISH;
    public static String NASDir = "";

    // Grab the Scheduler instance from the Factory
    public static Scheduler scheduler;

    /* Application Startup Event */
    @Override
    public void contextInitialized(ServletContextEvent ce) {
        ServletContext sc = ce.getServletContext();

        log.info("Application Watcher START");
        log.debug("Application Watcher START");

        try {
            hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            log.error(e.getStackTrace());
        }
        applicationStarDate = DateFormatUtils.formatUTC(System.currentTimeMillis(), dateformat, locale);
        version = sc.getInitParameter("DM_VERSION");
        NASDir = sc.getInitParameter("NAS_DIR");

    }

    public static void stopScheduler() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException ex) {
            log.error(ex.getStackTrace());
        } finally {
            log.info("Scheduler SHUTDOWN");
        }
    }

    public static void startScheduler() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            // and start it off
            scheduler.start();

            // Schedule all jobs available
            MineScheduler ms = new MineScheduler();
            ms.scheduleAllJobs(scheduler);




        } catch (SchedulerException ex) {
            log.error(ex.getStackTrace());
        } finally {
            log.info("Scheduler START");
        }
    }

    /* Application Shutdown	Event */
    public void contextDestroyed(ServletContextEvent ce) {

    }
}
