/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.main;

/**
 *
 * @author afinamore
 */
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.catalina.startup.Tomcat;
import org.h2.tools.Server;

public class Main {

	public static void main(String[] args) throws Exception {


		//Start H2
		// start the TCP Server
		Server server = Server.createTcpServer(args).start();
		// stop the TCP Server
		//server.stop();

		String webappDirLocation = "src/main/webapp/";
		Tomcat tomcat = new Tomcat();

		//The port that we should run on can be set into an environment variable
		//Look for that variable and default to 8080 if it isn't there.
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}

		tomcat.setPort(Integer.valueOf(webPort));

		tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

		tomcat.start();
		tomcat.getServer().await();
	}
}
