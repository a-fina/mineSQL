package net.mineSQL.connection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
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
    private static Thread currentThread = Thread.currentThread();
    private static int currentConnection;
    private static String separator = "###";


    public static String DB2 = "db2";
    public static String POSTGRES = "postgresql";
    public static String MYSQL = "mysql";

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
    public static synchronized ConnectionSource getMineConnection(Class objclass) throws SQLException{
        String DATABASE_URL = ORMLite.DATABASE_URL;

        return new JdbcConnectionSource(DATABASE_URL);
    }

    /*
     *  Search for already opened connection to the same host
    */
    public static synchronized Connection getConnection(
            String host,
            String database) throws ConnectionException, SQLException {

        log.debug("Connection  host: " +  host );
        Connection c = null;
        String host_key = "" + currentThread.getName() + "_" + host + "_";

        // Search already open connection to the same host
        
        Iterator it = hcm.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            log.debug("Connection Key: " + k + " host: " +  host + " start: " + k.startsWith(host_key));
            if (k.startsWith(host_key)){
                c = (Connection) getConnection(k);
                if ( c == null ){
                    String user = k.split(separator)[1];
                    String password = k.split(separator)[2];
                    String dbType = k.split(separator)[3];
                    // TODO: propagare nuovo parametro dbType
                    c = getConnection(host, database, user, password, dbType);
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

        log.debug("Opened connection DEBUG: " + hcm );
        c = (Connection) hcm.get(key);

        if ( c != null ){
        //TODO DB2 non va isValid    if ( c.isClosed() || ! c.isValid(2)){
            if ( c.isClosed() ){
                releaseConnection(c);
                c = null;
            }
        }
     
        return c;
    }

    /*
    * Connection Factory
    */
    public static synchronized Connection getConnection(

            String host,
            String database,
            String userName,
            String password,
            String dbType) throws ConnectionException, SQLException {

            log.info("Get Connection request: "  + host + " " + database + " " + userName + " " + password+ " "+dbType);

            if (dbType.equals(DB2) || dbType.equals("as400") ){
                return    getConnection(host, database, userName, password, "as400", "com.ibm.as400.access.AS400JDBCDriver");
            }
            else if (dbType.equals(POSTGRES)){
                return    getConnection(host, database, userName, password, "postgresql", "org.postgresql.Driver");
            }
            else if (dbType.equals(MYSQL)){
                return    getConnection(host, database, userName, password, "mysql", "com.mysql.jdbc.Driver");
            }

            throw new SQLException("MineSQL Database Type: <"+ dbType +"> non supportato");

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
            String driver) throws ConnectionException {

        Connection c = null;

        // Connection Key
        String key = "" + currentThread.getName()
                + "_" + host +"_" + "_" + database
                + separator+userName+separator+password+separator+dbType;

        try {
            c = getConnection(key);
        } catch (SQLException ex) {
            throw new ConnectionException(ex);
        }
        while (c == null) {
          
            try {
                String url = "jdbc:"+ dbType +"://" + host + "/" + database;
                log.debug("ConnectionManager loading Driver: "+ driver);
                Class.forName(driver).newInstance();
                log.debug("ConnectionManager getConnection for: "+ url);
                c = DriverManager.getConnection(url, userName, password);
                log.info("ConnectionManager OK connection to "+ url + " conn: " + c);

                currentConnection++;
            /**************
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
                log.debug(ex.getCause());
                log.debug(ex.getMessage());
                try {
                    ConnectionManager.class.wait();
                } catch (InterruptedException ie) {
                    log.debug("ConnectionManager [getConnection] FAILED WAIT for thread key:" + key
                            + " - " + ie.getMessage());
                }
                log.debug("ConnectionManager [getConnection] END WAIT for thread key:" + key);
                throw new SQLException(ex);
            }
            *************************/
            }catch (Exception ex) {
                log.debug(ex);
                throw new ConnectionException(ex);
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
