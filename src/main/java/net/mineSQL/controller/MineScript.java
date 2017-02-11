package net.mineSQL.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class MineScript {

    private static final Logger log = Logger.getLogger(MineScript.class);

    static String paramPrefix = "$";

    public MineScript() {
    }

    /*
     * Sovrascrive il prefisso delle variabili
     */
    public MineScript(String prefix) {
    }

    public String getPDF() throws IOException {
        String pdf =
                    new String(Files.readAllBytes(Paths.get("c:\\Users\\a.finamore\\workspace\\template-pdf.html"))); 
            log.info("PDF->" + pdf);
            return pdf;
    }

    public String mergeScriptParameters(HashMap params, String statement) {
        String newQuery = statement;

        try {

            Iterator it = params.keySet().iterator();
            Object key = null;
            while (it.hasNext()) {
                key = it.next();
                newQuery = newQuery.replaceAll("\\" + paramPrefix + key, (String) params.get(key));
            }

        } catch (Exception e) {
            log.error(e);
        }

        return newQuery;
    }

    public void execScript(Connection conn, String stmt) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(stmt.replaceAll("\r", "\n"));
            //st.executeUpdate();
            st.execute();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }


    /**
    * Prepara lo staement ed esegue la query
    * @param query
    * @return ResultSet
    * @see ResultSet
    */ 
    public ResultSet doQuery(Connection con, String query) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs  = null;
        log.debug(" before executed query: " + query+ " Con: " + con);
        ps = con.prepareStatement(query,
                                  ResultSet.TYPE_SCROLL_SENSITIVE,
                                  ResultSet.CONCUR_READ_ONLY);
        log.info(" after executed query: " + query);
        rs = ps.executeQuery();
        return rs;
    }


    /**
    * Restituisce il numero di righe della query
    * @param query SQL statement
    * @return integer
    */ 
    public int getRowsNumber(Connection con, String _query) throws SQLException{
        log.debug(" getRowsNumber before executed query: " + _query+ " Con: " + con);
        MineScript script = new MineScript();

        if ( _query.toLowerCase().startsWith("delete"))
            return 1;

        ResultSet rs = script.doQuery(con, "select count(*) tot from (" + _query + ") as counteggio");

        rs.next();
        return rs.getInt("TOT");
    }
  /**
    * Aggiunge la paginazione alla query
    * @param query
    * @param start
    * @param linit
    */ 
    public String getPaginationOracle(String query, int start, int limit){
		String sql = "select * " +
					"from (select rownum \"#\", a.* " +
						  "from (" +  query + " ) a " +
						  "where  rownum<=" + (start + limit) + ") " +
					"where \"#\" > " + start + " ";
		log.debug(" getPagination sql: " + sql);
		return sql;
    }
    public String getPaginationDB2(String query, int start, int limit){
        String sql = "select * from ( " +
            "select ROW_NUMBER() OVER() as NUM, a.* from " + 
            "( " + query +" )" + 
            " as a) as b where  b.NUM <=" + (start + limit) + " and NUM >" + start + " ";

		log.debug(" getPaginationDB2 sql: " + sql);
		return sql;
    }
    public String getPaginationMySQL(String query, int start, int limit){
		String sql =  query + " LIMIT "+start+", "+limit;
		log.debug(" getPagination sql: " + sql);
		return sql;
    }
    public String getPagination(String query, int start, int limit){
        if ( query.trim().toLowerCase().startsWith("select"))
            return getPaginationDB2(query, start, limit);
        else
            return query;
    }
}
