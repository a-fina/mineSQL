/******************************************************************************
 * Questa classe controlla una griglia con paginazione.
 */
package net.mineSQL.controller;

import net.mineSQL.util.*;
import net.mineSQL.connection.*;

import java.sql.*;
import java.util.*;
import java.util.Date;

//import oracle.sql.CLOB;
//import oracle.jdbc.OracleTypes;

import net.sf.json.*;
import org.apache.log4j.Logger;

public class MineGrid {

    private final String jdbcDefect="mineSQL";
    private Connection vCon;

    //TODO fare attributi pubblici?
    private String vTableName; 
    private String vSearchCondition; 
    private String vFilterCondition; 
    private String vSorting; 
    private String vDirection;
    private int vStart;
    private int vStop;
    private int vLimit;

    private int vRowCounter; 
    private String vQueryStatement;

    private PreparedStatement vStatement;
    private ResultSet vResultSet;

    private static final Logger log = Logger.getLogger(MineGrid.class);
    private static final boolean debugActive = false;


    /**
    * Costruttore che prende in input una query
    * @param ResultSet rs 
    */
	public MineGrid(ResultSet rs)
    {
        vResultSet = rs;  
    }

    /**
    * Costruttore che prende in input una query
    * @param String queryStatement
    */
	public MineGrid(String queryStatement)
    { 
        vQueryStatement = queryStatement;
    }
    
    /**
    * Costruttore
    * @param tableName tabella da paginare
    */  	
	public MineGrid(String tableName, String searchCondition,
                String filterCondition, String sorting, String direction,
                int start, int limit, Connection con)
    { 

        vTableName = tableName; 
        vSearchCondition = searchCondition; 
        vFilterCondition = filterCondition; 
        vSorting = sorting; 
        vDirection = direction;
        vLimit = limit;
        vStart = start;
        vCon = con;
    }

    /**
    * Attivata stampe
    */
    private void printLog(String str){
        if (debugActive)
            log.debug(str);
    }  

    /**
    * Esegue lo statement SQL
    * @param query
    * @return ResultSet
    * @see ResultSet
    */ 
    private ResultSet doQuery(String query) throws SQLException{
        Date before = new Date();
        log.info("Grid view, SQL execute statement: " + query);
        PreparedStatement ps = vCon.prepareStatement(query,
                                  ResultSet.TYPE_SCROLL_SENSITIVE,
                                  ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = ps.executeQuery();
        Date after = new Date();
        long diff = after.getTime() - before.getTime();
        log.info("Grid view execution time: " + diff+"ms" );
        return rs;
    }

    /**
    * Restituisce il numero di righe della query
    * @param query SQL statement
    * @return integer
    */ 
    private int getRowsNumber() throws SQLException{
        
        ResultSet rs = doQuery("select count(*) tot from (" + vQueryStatement + ")");
        rs.next();
        return rs.getInt("TOT");
    }
    
    /**
    * Scandisce la hashMap e costruisce la stringa della clausula WHERE
    * @param hm hashMap[nome, valore]
    * @return   stringa con la clausula where 
    * @see String
    */      
    private String addWhere(String query, HashMap hm){
        String whereClause = " WHERE ";
	    Iterator it = hm.keySet().iterator();
	    Object key = null;
        while ( it.hasNext()  )  {  
            key = it.next();	
            log.info( key.toString()+ " : " + hm.get(key) );
            whereClause += " " +key.toString()+ " LIKE  '" +hm.get(key)+ "' ";
            if ( it.hasNext() )
                whereClause += "AND";
        }
        if (! whereClause.equals(" WHERE "))
            query = "select * from (" + query + ") " + whereClause;
        return query;
    }

    /**
    * Aggiunge la paginazione alla query
    * @param query
    * @param start
    * @param linit
    */ 
    private String getPaginationOracle(String query){
        return  "select * " +
                "from (select a.*, rownum rnum " +
                      "from (" +  query+ " ) a " +
                      "where  rownum<=" + (vStart + vLimit) + ") " +
                "where rnum > " + vStart + " ";
    }

    public String getPagination(String query){
        String sql =  query + " LIMIT "+vStart+", "+vLimit;
        log.debug(" getPagination sql: " + sql);
        return sql;
    }
    /**
    * Assembla i parametri e costruisce lo statement SQL
    */ 
	private String getQuery(){ 

        if (vSearchCondition.length() > 0)
            vSearchCondition = " and " + vSearchCondition;

		String query = "select * from " + vTableName + 
                       " where 1 = 1 " + vSearchCondition + vFilterCondition +
                       " order by " + vSorting +" "+vDirection; 

        return query;
    }
   
    /**
    * Scandisce il resultSet e costruisce un array JSON di topics
    * @param render attiva un particolare formattazione dei campi
    */
    public JSONArray getJSON(boolean render) throws SQLException {
        return getJSON(vResultSet, render);
    } 
    public JSONArray getJSON() throws SQLException {
        return getJSON(vResultSet, false);
    } 
    public JSONArray getJSON(ResultSet rs) throws SQLException {
        return getJSON(rs, false);
    } 
	public JSONArray getJSON(ResultSet rs, boolean render ) throws SQLException { 
        ResultSetMetaData rsMd = rs.getMetaData();
        int numberOfColumns = rsMd.getColumnCount();

        JSONArray rows = new JSONArray();
        while (rs.next()) {
            JSONObject row = new JSONObject();
            // Righe
            for (int i = 1 ; i <= numberOfColumns ; i++) {
                String strValue = "-";

                /**
                if(rsMd.getColumnType(i)==OracleTypes.CLOB){
                    CLOB value= (CLOB) rs.getClob(i);
                    strValue=MakeTableForQuery.inputStreamAsString(value); 
                }else{
                    strValue=rs.getString(rsMd.getColumnLabel(i));
                }
                **/
                strValue=rs.getString(rsMd.getColumnLabel(i));

                if(strValue==null)strValue="-";
                // Elaborioni visuali del campo
                if (render) 
                    strValue = render( rsMd.getColumnLabel(i), strValue );

                row.put(rsMd.getColumnLabel(i), strValue);
                //log.debug(" getJSON row.put:" +rsMd.getColumnLabel(i) + " value: " +  strValue);
            }
            rows.add(row);
        }
        return rows;
	}
    // Elaborazioni grafiche pre-visualizzazione
    // TODO creare una classe apposita
    private String render(String column, String value){
        if ( column.equals("IDTASK") || column.equals("TASK") )
            value = "<b><a href=\"#nogo\" onclick=\"javascript:viewTask('"+ value +"');\">"
                    + value +"</a></b>";
        if ( column.equals("IDPROBLEM") || column.equals("IDPRBL") )
            value = "<b><a href=\"#nogo\" onclick=\"javascript:viewProblem('"+ value +"');\">"
                    + value +"</a></b>";
        if ( column.equals("IDDEFECT") || column.equals("IDDEF") || column.equals("DFCT") ||
             column.equals("DEFECT")  )
            value = "<b><a href=\"#nogo\" onclick=\"javascript:viewDefect('"+ value +"');\">"
                        + value +"</a></b>";
        if ( column.equals("IDWORKAROUND") || column.equals("IDWA") )
            value = "<b><a href=\"#nogo\" onclick=\"javascript:viewWorkaround('"+ value +"');\">"
                    + value +"</a></b>";
        if ( column.equals("AGE") || column.equals("AGEING") || column.equals("AGE_E2E")) {
            try {
        	float floatValue = (new Float(value)).floatValue();
        	value = Utilita.formatAge(floatValue);
            }
            catch (NumberFormatException e) {
        	// Non e' un valore numerico, non faccio nulla
            }
        }
        return value; 
    }
    
    /**
    * Count rows and prepare the full query statement.
    * @return ResultSet
    */  
    public ResultSet getResulSet(String jdbcResource) throws ConnectionException, SQLException {
    
           // vCon = ConnectionManager.getConnection(jdbcResource);
            // Il corpo della query
            vQueryStatement = getQuery();

            // Conteggio del numero totale di righe
            vRowCounter = getRowsNumber();

            // Query con paginazione
            vQueryStatement = getPagination(vQueryStatement);

            /** TODO Serve? 
            vResultSet.last();
            int countRow = vResultSet.getRow(); //Questo dovrebbe essere 20, il numero di rige per pagina
            vResultSet.beforeFirst();
            /*****/

            // Eseguo effettivamente la query
            return doQuery(vQueryStatement);
    }

    /**
    * Stampa l'oggetto JSON completo per la griglia con totalCount e topics
    */ 
	public String jsonResultSet(){
        try {
            // Get data from DB
            vResultSet = getResulSet(jdbcDefect);    

            // JSON Completo
            JSONObject griglia = new JSONObject();
            griglia.put("totalCount", "" + vRowCounter);
            griglia.put("topics",getJSON(vResultSet));
                
            // JSON printLog
            vResultSet.close();
            return griglia.toString();
        
        } catch (Exception ex) {
            log.error("Metodo jsonResultSet", ex);
            return Utilita.getJSONerror("Grid.java", ex.toString() + "<br/> Controllare la query: " + vQueryStatement );
        } finally {
          //  try{
          //      vCon.close();
          //  }catch( SQLException sqlE){
          //      log.error("Chiusura connessione jsonResultSet", sqlE);
          //      return Utilita.getJSONerror("Grid.java", "<br/> Errore chiudendo la connessione" + sqlE.toString()  );
          //  }
        }
    }
}
