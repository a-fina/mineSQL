// vim: ts=4:sw=4:nu:fdc=4:nospell
/***************************************************************************
 *
 * Created:     Fri Apr 17 10:40:30     2009
 * Description: Manage generic CRUD operation on a Table
 *
 * Author:  Finamore Alessio
 *
 ***************************************************************************/

package net.mineSQL.controller;


import java.util.*;
import java.util.Date;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class MineTable {

    private static final Logger log = Logger.getLogger(MineTable.class);
    
    // Prefisso messo sui nomi dei campi del fieldSet per capire
    // che sono stati auto-generati 
    //TODO eventualmente se si volesse rendere piu sicuro e creare
    // una specie di SECURE FORM generare una prefisso criptato e 
    // diverso ad ogni richiesta, quando poi si leggono i campi SUBMITTATI
    // verificare la criptazione
    private static final String submit_prefix = "SUBMIT_";
    private static final String hidden_prefix = "HIDDEN_";

    private String vTable = "";
    private String vDatabase = "";
    private Connection vCon = null;
    private String vType = null;

    protected final String jdbcAttProb = "jdbc/mineSQL";

    /***************************************************************************
     * Constuctor
         */
    public MineTable(Connection con, String nome ) {
        vTable = nome;
        vCon = con;
    }
    public MineTable(Connection con, String table, String database) {
        vTable = table;
        vCon = con;
        vDatabase = database;
    }
    public MineTable(Connection con, String table, String database, String type) {
        vTable = table;
        vCon = con;
        vDatabase = database;
        vType = type;
    }
    /**************************
     * TODO: costruttore con host, db, user, password, si prende da solo la connession
     */
    /***
    * Scan Http POST Request and look for SUBMIT_ parameters
    */
    public HashMap getSubmittedParams(HttpServletRequest req) {
        HashMap submitParams = new HashMap();
          
        String pName = "";
        for(Enumeration e = req.getParameterNames();e.hasMoreElements(); ){
            pName = (String)e.nextElement();
            
            log.debug("SUBMIT_params : " + pName +" -> " +req.getParameter(pName));
            if (pName.contains(submit_prefix)){
                submitParams.put(pName.substring(pName.indexOf(submit_prefix)).replace(submit_prefix,""),
                                 req.getParameter(pName));
                            //TODO controllare anche hidden_prefix
            }
        }
        log.debug("Table: "+ vTable  +" getSubmittedParams: " + submitParams);
        return submitParams;
    }


    /***************************************************************************
     * Insert a row
     */
    public boolean create(HashMap data) {
        String names = " (";
        String values = " VALUES (";

        Iterator it = data.keySet().iterator();
        Object key = null;
        String value = null;
        while (it.hasNext()) {
            key = it.next();
            names += "`"+key.toString()+"`";
            value = (String)data.get(key);
            values += "'" + value.replaceAll("'", "''") + "'";

            if (it.hasNext()) {
            names += ", ";
            values += ", ";
            }
        }
        names += ")";
        values += ")";

        String sqlInsert = "INSERT INTO " + vTable + names + values;

        return _runQuery(sqlInsert);
    }

    /***************************************************************************
     * Update a row
     */
    public boolean update(HashMap data, String condition) {
        return update(data,"",condition);
    }
    
    /***************************************************************************
     * Update a row
     */
    public boolean update(HashMap data, String setClause, String condition) {
        String namesValues = " SET " + setClause + " ";


        if (condition != null )
            condition = " WHERE " + condition;

        Iterator it = data.keySet().iterator();
        Object key = null;
        String value = null;
        
        if (setClause != null && !setClause.equals("") && it.hasNext())
            namesValues += ", ";
        log.info(" ---------------------> Table update");
        while (it.hasNext()) {
            key = it.next();
            value = (String)data.get(key);
            namesValues += key.toString() + "=" + "'" + value.replaceAll("'", "''") + "'" ;

            if (it.hasNext()) {
            namesValues += ", ";
            }
        }
        String sqlUpdate = "UPDATE " + vTable + namesValues + condition;

        return _runQuery(sqlUpdate);
    }

     /***************************************************************************
     * Insert a row
     */
    public boolean replace(HashMap data) {
        String names = " (";
        String values = " VALUES (";

        Iterator it = data.keySet().iterator();
        Object key = null;
        String value = null;
        while (it.hasNext()) {
            key = it.next();
            names += "`"+key.toString()+"`";
            value = (String)data.get(key);
            values += "'" + value.replaceAll("'", "''") + "'";

            if (it.hasNext()) {
            names += ", ";
            values += ", ";
            }
        }
        names += ")";
        values += ")";

        String sqlInsert = "REPLACE INTO " + vTable + names + values;

        return _runQuery(sqlInsert);
    }

    /***************************************************************************
     * Delete a row
     */
    public boolean delete(String condition) {
        String sqlDelete = "DELETE FROM " + vTable; 

        if (condition != null )
            sqlDelete += " WHERE " + condition;

        return _runQuery(sqlDelete);
    }

    /***************************************************************************
     * Read a row
     * @param String data Contiene l'elenco dei campi da leggere
     * @param String condition Contiene la condizione
     *
     * @return ResultSet 
     */
    private ResultSet _read(String data, String condition) {
        Connection con = null;
        ResultSet rs = null;

        try {
           // con = ConnectionManager.getConnection(jdbcAttProb);
            rs = _read(data, condition, vCon );
       // } catch (ConnectionException e) {
       //     e.printStackTrace();
       //     log.info((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } catch (SQLException e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } finally {
            closeConn(con);
            return rs; 
        }
    }
    private ResultSet _read(String data, String condition, Connection con) throws SQLException {
        
        Statement st = null;
        String sqlSelect = null;
        if (data == null)
            sqlSelect = "SELECT * FROM " + vDatabase + "." + vTable;
        else 
            sqlSelect = "SELECT " + data + " FROM " + vDatabase + "." +  vTable;

        if (condition != null )
            sqlSelect += " WHERE " + condition;

        log.info(" _read MARK_LIMIT Read " + sqlSelect );
        st = con.createStatement();
        ResultSet rsSelect = st.executeQuery(sqlSelect);

        return rsSelect;
    }

    /***************************************************************************
     * Get an Ext.dta.JsonReader of the requested Table
     * Convention:
     *
     */
    public JSONObject getJsonReader(){
        Connection con = null;
        JSONObject jsonReader = new JSONObject();
        String uuid = UUID.randomUUID().toString();

        try{
            //con = ConnectionManager.getConnection(jdbcAttProb);
            log.debug("getJsonReader");
            // MySQL ResultSet resSet  = _read(null, "1=1 LIMIT 1", vCon);
            // DB2 ResultSet resSet  = _read(null, "1=1 FETCH FIRST 1 ROW ONLY", vCon);
            ResultSet resSet  = _read(null, "1=1 LIMIT 1", vCon);

            ResultSetMetaData rsMd = resSet.getMetaData();

            jsonReader.put("root", "row");
            jsonReader.put("totalProperty", "totalCount");

            JSONArray items_array = new JSONArray();
            int colo = rsMd.getColumnCount();   

            for (int i = 1; i <= colo; i++){
                JSONObject item = new JSONObject();
                item.put("name",uuid+submit_prefix + rsMd.getColumnName(i));
                items_array.add(item);

            }   
            jsonReader.put("fields", items_array);
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        }finally { 
            closeConn(con);
            return jsonReader;
        }
    }

    /***************************************************************************
     * Genera un Extjs.fieldSet leggendo una stringa CSV
     * Convention TODO: 
     * - i campi che hanno prefisso NOUI_ non sono visuallizzati
     * - i campi che hanno prefisso SUBMIT_ vengono submittati
     * - i campi che hanno prefisso HIDDEN_SUBMIT_ vengono submittati 
     *   ma non visualizzti
     */
    public JSONObject getFieldSetFromCSV(String CSVfields){
        return getFieldSetFromCSV(CSVfields,"");
    }

    public JSONObject getFieldSetFromCSV(String CSVfields, String defaultValue){

        String [] lineTwoSplit = (CSVfields+",@").split(",");
        String [] finalResult = new String [lineTwoSplit.length-1];
        System.arraycopy(lineTwoSplit, 0, finalResult, 0, lineTwoSplit.length-1);

        JSONObject fieldSet = getFormFieldset();
        JSONObject formFields = new JSONObject();

        JSONArray items_array = new JSONArray();

        for (int i = 0; i < finalResult.length; i++) {
            //log.debug(finalResult[i]);
            JSONObject item = getFieldsetItem(finalResult[i], defaultValue);

            items_array.add(item);
        };
        fieldSet.put("items", items_array);

        formFields.put("items",fieldSet);
        formFields.put("reader", getJsonReader());

        return formFields;
    }
    /***************************************************************************
     * Get an Extjs.form.fieldSet of the requested Table
     * from a field of the Table
     */
    public String getFieldSetFromField(String field, String condition){

        String elencoCSV = "";
        //viene escluso dal fieldSet 
        //Connection con = null;
        JSONObject fieldSet = new JSONObject();

        try{
            log.debug("getFieldSetFromField connection: " + vCon);
            //con = ConnectionManager.getConnection(jdbcAttProb);
            
            ResultSet resSet  = _read(field, condition, vCon );

            while (resSet.next()) {
                elencoCSV =resSet.getString(field);
            }

            fieldSet = getFieldSetFromCSV(elencoCSV);
        } catch (Exception e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        }finally { 
         //   closeConn(con);
            log.debug("I Campi da visualizzare nella form: " + elencoCSV);
            return fieldSet.toString().replaceAll("\"@@","").replaceAll("@@\"","");
        }
    }
    public String getDefaultFieldSet(){
        return getDefaultFieldSet("");
    }
    public String getDefaultFieldSet(String defaultValue){
        log.debug("getDefaultFieldSet");
        return getFieldSetFromCSV("textarea-testo",defaultValue).toString() /*c'é anche xtype: panel*/
                .replaceAll("\"@@","").replaceAll("@@\"","");
    }
    
    /**
     * Costruisce il fieldSet
     */
    public JSONObject getFormFieldset(){
            JSONObject fieldSet = new JSONObject();

            fieldSet.put("title", "Filter Information");
            fieldSet.put("xtype","fieldset");
            fieldSet.put("autoHeight", "true");
            fieldSet.put("defaultType", "textfield");
            fieldSet.put("defaults","{width: '100%'}");

            return fieldSet;
    }

    /***************************************************************************
     * Get an Extjs.form.fieldSet of the requested Table
     * from Metadata
     */
    public String getFieldSetFromMetadata(){
        //viene escluso dal fieldSet 
      //  Connection con = null;
        JSONObject fieldSet = null;
        JSONObject formFields = null;
        String uuid = UUID.randomUUID().toString();

        try{
            log.debug("getFieldSetFromMetadata 1");
            //con = ConnectionManager.getConnection(jdbcAttProb);
            ResultSet resSet  = _read(null, "1=1 LIMIT 1", vCon);
            ResultSetMetaData rsMd = resSet.getMetaData();
            /** TODO: 
                centralizzare e utilizzare il metodo getFieldSetFromCSV 
                passandogli un CSV 
            **/
            formFields = new JSONObject();

            fieldSet = getFormFieldset();
            log.debug("getFieldSetFromMetadata 2");
            JSONArray items_array = new JSONArray();
            int colo = rsMd.getColumnCount();   

            for (int i = 1; i <= colo; i++){
                JSONObject item = new JSONObject();
                item.put("fieldLabel", rsMd.getColumnName(i));
                item.put("name",uuid+submit_prefix + rsMd.getColumnName(i));
                items_array.add(item);
            }   
            fieldSet.put("items", items_array);

            formFields.put("items",fieldSet);
            formFields.put("reader", getJsonReader());
            /** TODO fine **/
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        }finally { 
           // closeConn(con);
            return formFields.toString();
        }
    }

    /**
     *
     * Splitta il nome del paramentro e costruisce un item del fieldSet
     * della form. Come separatore viene usato il trattio "-"
     *
     * Convenzioni sul nome del parametro:
     * <prefisso del nome>-<nome del parametro>
     *
     * prefisso del nome     |  tipo di item
     * textfield-               textfield 
     * textarea-                textarea
     * numberfield-             numberfield
     * datefield-               datefield 
     * hidden-                  hidden
     * combo-<nome_tabella>-    combobox che carica i valori via ajax da <nome_tabella>
     *
     * @param String fieldName
     * @return JSONObject item  
     * 
     **/
    public JSONObject getFieldsetItem( String fieldString ){
        return getFieldsetItem( fieldString, "" );
    }
    public JSONObject getFieldsetItem( String fieldString, String defaultValue ){
        JSONObject item = new JSONObject();
        
        String [] lineTwoSplit = (fieldString+"-@").split("-");
        String [] finalResult = new String [lineTwoSplit.length-1];
        System.arraycopy(lineTwoSplit, 0, finalResult, 0, lineTwoSplit.length-1);

        String fieldType = finalResult[0];
        String fieldName = finalResult[finalResult.length-1];

        String uuid = UUID.randomUUID().toString();
        log.debug("getFieldsetItem");
        // Il combo é un campo particolare perché legato ad un tabella
        // da cui caricare i valori
        if ( fieldType.equals("combo") ){
            String tableName = finalResult[1];
            //NOTA: @@ serve per la rimozione degli apicetti di troppo
            String text = "@@new Ext.data.JsonStore({ url:\'combos/combos.jsp?table="+tableName+"\', root: \'topics\', fields: [ \'ID\', \'NOME\' ]})@@";

            item.put("xtype", "combo");
            item.put("fieldLabel", fieldName );
            // Nome submittato dalla form
            item.put("name",fieldName );
            item.put("id",fieldName );
            item.put("displayField", "NOME");
            item.put("valueField", "ID");
            item.put("hiddenName", uuid+submit_prefix + fieldName);
            item.put("tpl", "@@tplCombo@@");
            item.put("store",text);
            item.put("allowBlank", new Boolean(true));
            item.put("editable", new Boolean(false));
            item.put("disabled", new Boolean(false));
            item.put("typeAhead", new Boolean(true));
            item.put("triggerAction", "all");
            item.put("emptyText", "Scegli...");
            item.put("selectOnFocus", "true");
        }else{
            // Tipo di item
            item.put("xtype",fieldType );
            // Nome visualizzato in form
            item.put("fieldLabel", fieldName );
            // Nome submittato dalla form
            item.put("name",uuid+submit_prefix + fieldName );
            item.put("id",uuid+submit_prefix + fieldName );
            if (defaultValue.length() > 0)
                item.put("value", defaultValue  );
        }

        return item;
    }
     /**
     * Legge il valore del campo, ritorna solo un valore, l'ultimo
     * @return String
     */
    public String select(String data, String condition) {
        //Connection con = null;
        ResultSet rs = null;
        String field = "";

        try {
            //con = ConnectionManager.getConnection(jdbcAttProb);
            log.debug("MARK_1");
            rs = _read(data, condition, vCon );
            log.debug("MARK_2 data: " + data);
            while (rs.next()) {
                field = rs.getString(data);
            }
            log.debug("select " + data+" -> "+field);
    //    } catch (ConnectionException e) {
    //        e.printStackTrace();
    //        log.info((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } catch (SQLException e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } finally {
    //        closeConn(con);
            return field;
        }
    }
    /**
     * Legge il valore dei campi
     * @return json
     */
    public String read(String data, String condition) {
        //Connection con = null;
        ResultSet rs = null;
        JSONObject row = new JSONObject();
        log.debug("read 1");
        try {
            //con = ConnectionManager.getConnection(jdbcAttProb);
            rs = _read(data, condition, vCon );

            MineGrid jsonGrid = new MineGrid(rs);
            row.put("row",jsonGrid.getJSON() );
            
    //    } catch (ConnectionException e) {
    //        e.printStackTrace();
    //        log.info((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } catch (SQLException e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } finally {
    //        closeConn(con);
            return row.toString(); 
        }
    }
    public boolean runQuery(String statement) {
        return _runQuery(statement);
    }
    /***************************************************************************
    * Execute the statement and close the connection
    */
    private boolean _runQuery(String statement) {
        Statement st = null;
        //Connection con = null;
        Date before = new Date();
        boolean ret = false;

        try {
            log.info("Table: "+ vTable +"execute SQL statement: " + statement);
           // con = ConnectionManager.getConnection(jdbcAttProb);
            st = vCon.createStatement();
            ret = st.execute(statement);
            if (!ret) ret=(st.getUpdateCount()>0?true:false);

            Date after = new Date();
            long diff = after.getTime() - before.getTime();
            log.info("Table: "+ vTable +" return: "+ ret +" execution time: " + diff+"ms" );

            st.close();

       // } catch (ConnectionException e) {
       //     e.printStackTrace();
       //     log.info((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } catch (SQLException e) {
            e.printStackTrace();
            log.error((e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
        } finally {
            //closeConn(con);

        }
        return ret;
    }

    /***************************************************************************
     * Close current connection to DB
     */
    private boolean closeConn(Connection con) {
        try {
            if (con != null) {
            con.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(" ------------------------------------< "+(e.getMessage().replaceAll("\"", "'")).replaceAll("\n", " "));
            return false;
        }
    }
}
