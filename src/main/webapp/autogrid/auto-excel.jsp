<%@ page import="java.util.Date" %>
<%@ page import="net.mineSQL.connection.ConnectionManager" %>
<%@ page import="net.mineSQL.controller.MineGrid" %>
<%@ page import="net.mineSQL.util.Format" %>
<%@ page import="net.mineSQL.util.Format" %>
<%@ page import="net.mineSQL.controller.Excel" %>
<%@ page import="java.sql.Types" %>
<%@ page import="org.xhtmlrenderer.pdf.ITextRenderer" %>
<%@ page import="org.xml.sax.SAXParseException" %>
<%@ page import="org.apache.jasper.JasperException" %>
<%@ include file="auto-lib.jsp"%><%
String excel = null;

try {
	
    // Recupero lo statement paginato e completo di filtri
    String[] hiddenColumns = {};
    //TODO: l'id ed il nome sono cambiati, sono univoci per ogni form. Idea: Intercettarli dal prefisso SUBMIT_hidden_
    // request.getParameter("hidden_columns").split(",");

    /**    
    getFinalQuery(request, request.getParameter("tableName") ); 	

    log.info("Query di export EXCEL: " + query);
        
    if ( tableName.equals("msq_SCRIPT_T") ){
        MineScript script = new MineScript();
        MineTable table = new MineTable(tableName);
        log.debug("- - - - > MAR_OLD_QUERY: " + query ); 
        query = script.mergeScriptParameters(table.getSubmittedParams(request),
                                             query); 
        log.debug("- - - - > MAR_NEW_QUERY: " + query ); 
    } 
    **/
    // Prendo i parametri
    doGet(request);


    // JSON Completo
    Date before = new Date();
    log.info("Connected to: " + hostName +":"+databaseName + " action: " + action+ " export: " + exportType);
 
    // Applico il filtro la query
    String filter = Utilita.getFilterCondition(con, request, "", "", "");

    MineScript script = new MineScript();
    MineTable table = new MineTable(con, tableName);

    // Se submit dalla FORM eseguo lo SCRIPT e successivamente eseguo lo SCRIPT_OUTPUT
    if ( action.equals("runScript") ){
        HashMap formParams = table.getSubmittedParams(request);
        // testo e' la textarea di default
        query = script.mergeScriptParameters(formParams, DEFAULT_TESTO);  
        //Definito in auto-lib.jsp, non produce output ed seguo lo SCRIPT riga per riga
        script.execScript(con, query); 

        Date after = new Date();
        long diff = after.getTime() - before.getTime();
        
        log.info(" Run script : " + query);	
        log.info(" Execution time: " + diff + "ms");	
    }

    // Ai successivi caricamenti della pagina, che sono quelli che partono
    // quando si ricarica solo la griglia, lo SCRIPT non viene piu eseguito ma
    // si eseguo solo SCRIPT_OUTPUT che ricarica i dati della griglia
    // Creare sul DB una nova tabella in cui mettere delle query che estraggono
    // solo i dati di l'output. 
    // Serve per forza un query di estrazione perché le colonne della griglia
    // devono essere cablate in qualche modo
    // TODO modificare Autogrid.js in modo da diversificare il nome di tableName
    // con i parametri di POST

    if ( action.equals("runQuery")  )
    {
    //    Connection conForsavedScript = ConnectionManager.getConnection("localhost","mineSQL");
    //    MineTable runQuery = new MineTable(conForsavedScript, "msq_SCRIPT_T"); 
    //   query = runQuery.select("testo","ID = '"+idQuery+"'");

        HashMap formParams = table.getSubmittedParams(request);
        query = script.mergeScriptParameters(formParams, query);  // testo e' la textarea di default
        log.debug(" MARK_runQuery database query:" + query);
    }
    else if ( action.equals("runDefaultScript") )
    {
            // Preparo la query con Paginazione
            String db_table = databaseName+"."+tableName; 
            query = "SELECT * FROM "+ db_table +" WHERE 1=1";
            log.debug(" MARK_runDefaultScriptquery database:" +  databaseName +" tablename:" + tableName);
            //TODO gestire la query a test libero, viene postata la variabuile query_body
    } 
     else if ( action.equals("runSavedScript") )
    {
            /*
             * TODO: qui volendo si puo mettere un controllo incrociato su databaseName + hostname
             * e i valori salvati nel DB insieme al testo 
             */
            Dao<Report, Integer> reportDao;
          
            ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
            reportDao = DaoManager.createDao(connectionSource, Report.class);
            query = reportDao.queryForId(new Integer(idQuery).intValue()).getDescrizione();
            log.debug(" MARK_runSavedScript database query:" + query);

            if (connectionSource != null) {
                connectionSource.close();
            }

    }

        /*
        * Filtri in sessione
        */
        //log.debug("4.1.1 query paginata MARK_FILTER : " + query + " con:" + con + " hiddenCol: " + hiddenColumns );
        if (filter.length() > 0){
            query = "select * from (" + query + ") WHERE 1=1 " + filter;
            request.getSession().setAttribute("filter", filter);
            request.getSession().setAttribute("searchCondition", "");
            //log.info("3 - - - ->getQueryStatement MARK_FILTER query: " +  query );
        }else{
            request.getSession().removeAttribute("searchCondition");
            request.getSession().removeAttribute("filter");
            //log.info("3.1 - - - ->getQueryStatement MARK_FILTER query reset filter: " + query);
        }

        log.debug("4.1.2 query paginata MARK_FILTER : " + query + " con:" + con + " hiddenCol: " + hiddenColumns );

        Excel tsExcel = new Excel();
        excel = tsExcel.getFile(con, query, hiddenColumns);

        if ( exportType.equals("excel") ){
            // Esporta in formato excel	: nomeQuery viene festito in auto-lib.jsp TODO sistemare
            response.addHeader("Content-Type","application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=\""+databaseName+"-"+tableName+".xls\"");
            // Paginazione
            out.print(excel);
        } 
        else if ( exportType.equals("pdf") ){
   
            response.addHeader("Content-Disposition", "attachment;filename=\""+databaseName+"-"+tableName+".pdf\"");
            response.setContentType("application/pdf");

            OutputStream o = response.getOutputStream();
            ITextRenderer renderer = new ITextRenderer();
                
            renderer.setDocumentFromString(excel);
            renderer.layout();
            
            renderer.createPDF(o);
            
            o.flush();
            o.close();
        }
/*
} catch ( SAXParseException axe ){
    response.addHeader("Content-Type","application/vnd.ms-excel");
    response.addHeader("Content-Disposition", "attachment;filename=\""+databaseName+"-"+tableName+".xls\"");
    if ( excel != null) out.print(excel);
**/	
} catch (Exception sqle) {
    log.error(sqle+" "+ query);
} finally {
    try {
        con.close();
    }
    catch (Exception ex) {
        ex.printStackTrace();
    }
}
%>