/*
	File : ApplicationWatcher.java
*/
package net.mineSQL.util;
import java.util.Locale;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.time.DateFormatUtils;

public class ApplicationWatcher implements ServletContextListener	{

	public static String applicationStarDate  =	"";
	public static String hostName =	"";
	public static String version  =	"";
	public static String tmpPath  = "";
	public static String dateformat = "dd-MMM-yy HH:mm:ss";
    public static Locale locale  = Locale.ENGLISH;
    public static String NASDir = "";

	/* Application Startup Event */
	public void	contextInitialized(ServletContextEvent ce) {
		ServletContext sc = ce.getServletContext();
		
        try {
			hostName = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        applicationStarDate =  DateFormatUtils.formatUTC(System.currentTimeMillis(), dateformat, locale);
        version = sc.getInitParameter("DM_VERSION");
        NASDir = sc.getInitParameter("NAS_DIR");
	}

	/* Application Shutdown	Event */
	public void	contextDestroyed(ServletContextEvent ce) {}
}
