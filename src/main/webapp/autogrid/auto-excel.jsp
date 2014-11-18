<%@page import="java.util.Date" %>
<%@ page import="net.mineSQL.connection.ConnectionManager" %>
<%@page import="net.mineSQL.controller.MineGrid" %>
<%@include file="auto-lib.jsp"%>

<%
try {
	
    // Recupero lo statement paginato e completo di filtri
    String[] hiddenColumns = request.getParameter("hidden_columns").split(",");

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

    // Esporta in formato excel	: nomeQuery viene festito in auto-lib.jsp TODO sistemare
    response.addHeader("Content-Type","application/vnd.ms-excel");
    response.addHeader("Content-Disposition", "attachment;filename=\""+databaseName+"-"+tableName+".xls\"");

    // JSON Completo
    Date before = new Date();

    log.info("Connected to: " + hostName +":"+databaseName + " action: " + action);
 
    // Applico il filtro la query
    String filter = Utilita.getFilterCondition(con, request, "", "", "");

    MineScript script = new MineScript();
    MineTable table = new MineTable(con, tableName);

        // Se submit dalla FORM eseguo lo SCRIPT e successivamente eseguo lo SCRIPT_OUTPUT
        if ( /* tableName.equals("msq_SCRIPT_T") && action.equals("dummy") */ action.equals("runScript") ){
            /****************
            log.debug(" tableName.equals(\"msq_SCRIPT_T\") && action.equals(\"dummy\")  idQuery: " + idQuery);
            // Preparo la query con Paginazione
            getQueryStatement(request, tableName, where);

            log.debug("- - - - > MAR_OLD_QUERY: " + query ); 
            query = script.mergeScriptParameters(table.getSubmittedParams(request), query); 
            log.debug("- - - - > MAR_NEW_QUERY: " + query ); 
            **********/
            HashMap formParams = table.getSubmittedParams(request);
            query = script.mergeScriptParameters(formParams, DEFAULT_TESTO);  // testo e' la textarea di default
            //Definito in auto-lib.jsp, non produce output ed seguo lo SCRIPT riga per riga
            execScript(con, query); 

            Date after = new Date();
            long diff = after.getTime() - before.getTime();
            
            log.info(" Run script : " + query);	
            log.info(" Execution time: " + diff+"ms");	
            //griglia.put("Script", "Successfully finished in "+diff+"ms");
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

        PreparedStatement ps = null;
        ResultSet rs = null;
        int rowCounter = 10;

	if ( action.equals("runQuery") ){
            Connection conForsavedScript = ConnectionManager.getConnection("localhost","mineSQL");
            MineTable runQuery = new MineTable(conForsavedScript, "msq_SCRIPT_T"); 
			log.debug(" MARK_runQuery query database:" +  databaseName +" tablename:" + tableName+" idQuery: ");
            query = runQuery.select("testo","ID = '"+idQuery+"'");

		HashMap formParams = table.getSubmittedParams(request);
		log.debug(" MARK_runQuery 1 query: " +  query+ " params: " + formParams + " querySel: " + querySel);
		query = script.mergeScriptParameters(formParams, query);  // testo e' la textarea di default
		log.debug(" MARK_runQuery 2 query: " +  query+ " params: " + formParams + " querySel: " + querySel);
	}else if ( action.equals("runDefaultScript") ){
			// Preparo la query con Paginazione
			String db_table = databaseName+"."+tableName; 
			query = "SELECT * FROM "+ db_table +" WHERE 1=1";
			//getQueryStatement(request,tableName, where);
			log.debug(" MARK_runDefaultScriptquery database:" +  databaseName +" tablename:" + tableName);
			//TODO gestire la query a test libero, viene postata la variabuile query_body
	} else if ( action.equals("runSavedScript") ){
            Connection conForsavedScript = ConnectionManager.getConnection("localhost","mineSQL");
            MineTable savedScript = new MineTable(conForsavedScript, "msq_FILTRI_T"); 
			log.debug(" MARK_runSavedScript 3 database:" +  databaseName +" tablename:" + tableName+" idQuery: ");
            query = savedScript.select("DESCRIZIONE","ID = '"+idQuery+"'");
			log.debug(" MARK_runSavedScript database query:" + query);
            /*
             * TODO: qui volendo si puo mettere un controllo incrociato su databaseName + hostname
             * e i valori salvati nel DB insieme al testo 
             */
        }


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

        log.debug("4.1 query paginata MARK_FILTER : " + query );
        // Paginazione
	ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

	rs = ps.executeQuery();
	ResultSetMetaData rsMd = rs.getMetaData();
	int numberOfColumns = rsMd.getColumnCount();

	rs.last();
	int countRow = rs.getRow();
	rs.beforeFirst();
	if (countRow == 0) {
		%>
		<table id="tabella-report" border="1">
		<thead>	
		<tr>
			<th>Messaggio</th>
		</tr>
		</thead>
		<tr>
			<td width="150"><p>Nessuna corrispondenza</p></td>
		</tr>
		</table>
		<%
    } else {
        %>
        <table id="tabella-report" border="1">
        <thead>	
        <tr>
        <%
            for (int i = 1 ; i <= numberOfColumns ; i++) {
                String valore = rsMd.getColumnLabel(i);
                boolean skip = false;
                for(int l = 0; l<hiddenColumns.length; l++){
                    if ( valore.equalsIgnoreCase(hiddenColumns[l])  ){
                        skip = true;
                        break;
                    }
                }
                if (! skip){
                    %><th class="intestazione" style="font-size: 10"><%=valore%></th><%
                }
            }
        %>
        </tr>
        </thead>
        <%
          while (rs.next()) {
            %> <tr> <%
                for (int i = 1 ; i <= numberOfColumns ; i++) {
                    String strValue = "-";

                    /*if(rsMd.getColumnType(i)==OracleTypes.CLOB){
                        CLOB value= (CLOB) rs.getClob(i);
                        strValue=MakeTableForQuery.inputStreamAsString(value);
                    }else*/ if (rsMd.getColumnLabel(i).equalsIgnoreCase("AGE") ||
                	          rsMd.getColumnLabel(i).equalsIgnoreCase("AGEING") ||
                	    	  rsMd.getColumnLabel(i).equalsIgnoreCase("AGE_E2E")) {
                	    try {
                		   if (rs.getString(rsMd.getColumnLabel(i)) != null) {
                	          float floatValue = (new Float(rs.getString(rsMd.getColumnLabel(i)))).floatValue();
                	          strValue=Utilita.formatAge(floatValue);
                		   }
                	    }
                	    catch (NumberFormatException e) {
                		   // Non e' un valore numerico, quindi lo prendo come stringa
                		   // Vuol dire che la query configurata, gia' lo tira fuori formattato
                		   strValue = rs.getString(rsMd.getColumnLabel(i));
                	    }
                    }else{
                        strValue=rs.getString(rsMd.getColumnLabel(i));
                    }
                    if(strValue==null)strValue="-";
                    // UtilitaNew.escapeHTML(strValue)
                    boolean skip = false;
                    for(int l = 0; l<hiddenColumns.length; l++){
                        if ( rsMd.getColumnLabel(i).equalsIgnoreCase(hiddenColumns[l])  ){
                            skip = true;
                            break;
                        }
                    }
                    if (! skip){
                        %> <td class="acqua"><%= strValue.replaceAll("<", "&lt;") %></td><%
                    }
                }
            %> </tr> <%
                }
           %></table><%
                rs.close();
                ps.close();
        }
	
	} catch (SQLException sqle) {
		switch (sqle.getErrorCode()) {
        default: log.error(sqle+" "+ query); break;
	}
} finally {
	
	try {
		con.close();
	}
	catch (Exception ex) {
		ex.printStackTrace();
	}
}


%>
</body>
</html>
