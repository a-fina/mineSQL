/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.app;

//import net.mineSQL.tomcat.MineBrowser;
import net.mineSQL.tomcat.MineBrowser;
import net.mineSQL.tomcat.MineTomcat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

/**
 *
 * @author alessio.finamore
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Options options = new Options();
		options.addOption("browser", true, "Run with browser.");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		System.out.println("MineSQL: starting TomCat");
		//TODO spostare in ApplicationWatcher fare oggetto statico context
		// shutdown alla chiusura del bbrowser
		MineTomcat mTm = new MineTomcat();
		// Start Tomcat
		mTm.start();
		// Start Browser
		System.out.println("MineSQL: running on http://localhost:8080");

		if (args != null) {
			if (args.length > 0) {
				if (args[0].matches("browser")) {
					System.out.println("MineSQL: running UI Press [CTRL-C] to Exit");
					MineBrowser mBr = new MineBrowser();
					mBr.start();
				}
			} else {
				System.out.println("MineSQL: running NOUI [help:browser] Press [CTRL-C] to Exit");
			}
		}
		// Wait tomcat
		mTm.await();
	}
}
