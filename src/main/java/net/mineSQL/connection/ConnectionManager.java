package net.mineSQL.connection;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class ConnectionManager {

    private static final Logger log = Logger.getLogger(ConnectionManager.class);
    // Dichiarare l'hashtable
    private static HashMap hcm;
    // Dichiarare DataSource
    private static HashMap hds;
    private static Thread currentThread;
    private static int currentConnection;
    private static String separator = "###";



    public ConnectionManager() {
    }

    public static int getCurrentConnection() {
        return currentConnection;
    }

    public static synchronized DataSource getDataSource(String sDSName) throws ConnectionException {

        if (hds == null) {
            hds = (HashMap) Collections.synchronizedMap(new HashMap());

        }

        DataSource ds = (DataSource) hds.get(sDSName);

        if (ds == null) {
            log.debug(" "
                    + "ConnectionManager [getDataSource] for source: " + sDSName);
            try {
                log.debug("Trying to connect to " + sDSName);
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                ds = (DataSource) env.lookup("jdbc/" + sDSName);
                hds.put(sDSName, ds);
            } catch (NamingException e) {
                throw new ConnectionException(e);
            }
        }
        return ds;
    }

    public static synchronized Connection getConnection(
            String host,
            String database) throws ConnectionException, SQLException {

        Connection c = null;
        String host_key = "" + currentThread.getName() + "_" + host + "_";

        // Search already open connection to the same host
        
        Iterator it = hcm.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            log.debug("K: " + k + " host: " +  host + " start: " + k.startsWith(host_key));
            if (k.startsWith(host_key)){
                c = (Connection) getConnection(k);
                if ( c == null ){
                    String user = k.split(separator)[1];
                    String password = k.split(separator)[2];
                    c = getConnection(host, database, user, password);
                    log.debug("ConnectionManager [getConnection] new c:" + c);
                }
                break;
            }
        }
        log.debug("Found connection for key: " + host_key);
        // Exception ex = new Exception("Cannot find connection for key: " + key);
        // throw new ConnectionException(ex);
        return c;
    }
    
    public static synchronized Connection getConnection(String key) throws SQLException{
        Connection c = null;

        if (hcm == null) {
            log.debug("Creating new Connection Map");
            hcm = new HashMap();
        }

        log.debug("Opened connection: " + hcm );
        c = (Connection) hcm.get(key);

        if ( c != null )
            if ( c.isClosed() || ! c.isValid(2)){
                releaseConnection(c);
                c = null;
            }
     
        return c;
    }

    public static synchronized Connection getConnection(
            String host,
            String database,
            String userName,
            String password) throws ConnectionException, SQLException {

            return    getConnection(host, database, userName, password, "mysql", "com.mysql.jdbc.Driver");
    }
    public static synchronized Connection getFilemakerConnection(
            String host,
            String database,
            String userName,
            String password) throws ConnectionException, SQLException {

            return    getConnection(host, database, userName, password, "filemaker", "com.filemaker.jdbc.Driver");
    }
    public static synchronized Connection getConnection(
            String host,
            String database,
            String userName,
            String password,
            String dbType,
            String driver) throws ConnectionException, SQLException {

        Connection c = null;
        currentThread = Thread.currentThread();
        String key = "" + currentThread.getName()
                + "_" + host + "_" + database
                + separator+userName+separator+password;

        c = getConnection(key);
        while (c == null) {
          
            try {
                //c = new MyConnection(getDataSource(sDSName).getConnection());
               /*
                String userName = "root";
                String password = "root";
                String db = "mineSQL";
                String host = "localhost";
                */
                String url = "jdbc:"+ dbType +"://" + host + "/" + database;
                log.debug("ConnectionManager get new connection url: " + url);
                Class.forName(driver).newInstance();
                c = DriverManager.getConnection(url, userName, password);
                log.debug("ConnectionManager get new connection connection:" + c);

                currentConnection++;
            } catch (InstantiationException ex) {
                log.debug(ex);
            } catch (IllegalAccessException ex) {
                log.debug(ex);
            } catch (ClassNotFoundException ex) {
                log.debug(ex);
            } catch (SQLException ex) {
                // Non ci sono risorse disponibili, quindi
                // si attende che venga rilasciata una connessione
                log.debug("ConnectionManager [getConnection] WAIT for thread key:" + key);
                log.debug(ex);
                try {
                    ConnectionManager.class.wait();
                } catch (InterruptedException ie) {
                    log.debug("ConnectionManager [getConnection] FAILED WAIT for thread key:" + key
                            + " - " + ie.getMessage());
                }
                log.debug("ConnectionManager [getConnection] END WAIT for thread key:" + key);
            }
            if (c != null) {
                hcm.put(key, c);
                log.debug("ConnectionManager add new connection [getConnection] for thread key:" + key);
            }
        }
        
        return c;
    }



    public static synchronized void releaseConnection(Connection con) throws SQLException {

        Iterator it = hcm.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            if (con.equals(hcm.get(k))) {
                con.close();
                log.debug("ConnectionManager [releaseConnection] remove for thread key: " + k);
                hcm.remove(k);
                currentConnection--;
                // Notifica la disponibilitdi una connessione
                ConnectionManager.class.notify();
                log.debug("ConnectionManager [releaseConnection] NOTIFY for thread key:" + k);
                break;
            }
        }
    }
}
