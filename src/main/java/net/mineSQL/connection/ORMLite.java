/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.connection;

/**
 *
 * @author alessio.finamore
 */
public class ORMLite {
        public static String path = System.getProperty("user.dir");
        public static String dbName = "minesql_report";
        public static String DATABASE_URL = "jdbc:h2:file:" + path + dbName;

}
