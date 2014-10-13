<%@page import="com.j256.ormlite.dao.DaoManager"%>
<%@page import="com.j256.ormlite.support.ConnectionSource"%>
<%@page import="com.j256.ormlite.jdbc.JdbcConnectionSource"%>
<%@page import="com.j256.ormlite.dao.Dao"%>
<%@page import="net.mineSQL.model.dao.Report"%>
<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="auto-lib.jsp"%>

<%@page import="net.mineSQL.controller.MineTable"%>
<%
     // Prendo i parametri
    doGet(request);

    log.info("Connected to: " + hostName +":"+databaseName);

    String crud_operation = request.getParameter("crudOperation");

    // Istanzio il gestore della tabella
    MineTable dmTable = new MineTable(con, request.getParameter("tableName") );
    String result = "";

    // Gestione CRUD
    if (crud_operation.equals("fieldSet")) //TODO sincronizzare con jsonReader
    {
        String id = "";
        String action = "";

        if ( request.getParameter("action") != null)
            action = request.getParameter("action");
        if ( request.getParameter("idScript") != null)
            id = request.getParameter("idScript");

        if ( id.equals("") || action.equals("runDefaultScript") ){
			result = dmTable.getDefaultFieldSet();
        } else if ( ! id.equals("") && action.equals("runSavedScript") ){
            Connection conForsavedScript = ConnectionManager.getConnection("localhost","mineSQL");
            MineTable savedScript = new MineTable(conForsavedScript, "msq_FILTRI_T"); 
            String query = savedScript.select("DESCRIZIONE","ID = '"+id+"'");
			result = savedScript.getDefaultFieldSet(query);
		}else
			result = dmTable.getFieldSetFromField("PARAMETRI", "ID="+id);

        // Ricavo i metaData dei campi
		//result = dmTable.getFieldSetFromMetadata();
		log.debug("before dmTable.getFieldSetFromField result: " + result);
    }
    else if (crud_operation.equals("jsonReader")) //TODO sincronizzare con fieldSet
    {
        // Ricavo i metaData dei campi
        result = dmTable.getJsonReader( ).toString();
    }
    else if (crud_operation.equals("read"))
    {
        result = dmTable.read(null,"ID = " + request.getParameter("ID") );
    }
    else if (crud_operation.equals("create"))
    {
        // Recupero lo statement NON paginato (TODO) e completo di filtri
        String[] hiddenColumns = null;
        if (request.getParameter("hidden_columns") != null)
            hiddenColumns = request.getParameter("hidden_columns").split(",");
        

        Connection conScript = ConnectionManager.getConnection("localhost","mineSQL");
        MineTable scriptTable = new MineTable(conScript, "msq_FILTRI_T" );
        MineTable runQuery= new MineTable(conScript,"msq_SCRIPT_T");
        log.debug(" MARK_runQuery query database:" +  databaseName +" tablename:" + tableName+" idQuery: ");
        query = runQuery.select("testo","ID = '"+idQuery+"'");

        HashMap formParams = scriptTable.getSubmittedParams(request);
        log.debug(" MARK_create query: " +  query+ " params: " + formParams + " querySel: " + querySel);
        MineScript script = new MineScript();
        
        // TODO non mi ricordo cazzo volevo fare? query = script.mergeScriptParameters(formParams, query);
            
        query = script.mergeScriptParameters(formParams, DEFAULT_TESTO);  // testo e' la textarea di default
            
        log.debug(" MARK_create query: " +  query+ " params: " + formParams + " querySel: " + querySel);
        // Inserimento nuova riga nella tabella
        // TODO Generalizzare entity
        // int nextId = Utilita.nextId(con,"FILTRI");

        // nuovoFiltro.put("IDFLUSSO", request.getParameter("IDFLUSSO") );
        
        // Inserisco una nuova query
        // Oggetto
        String path = "Z:/Finamore/";
        String dbName = "minesql_report";
        String DATABASE_URL = "jdbc:h2:file:" + path + dbName;
	    Dao<Report, Integer> reportDao;
		ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
		reportDao = DaoManager.createDao(connectionSource, Report.class);

		Report repo = new Report(request.getParameter("NOME"), request.getParameter("NOTE"));
        repo.setDescrizione(query);
        repo.setHost( hostName);
        repo.setDatabase( databaseName);
        repo.setUtente("0"/*TODO session.getAttribute("IDUTENTE").toString() */ );
        reportDao.create(repo);

        int res = reportDao.create(repo);

  //      if (! scriptTable.create( nuovoFiltro ) )
        if ( res == 1 )
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore aggiornamento.\"}";
        else
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Aggiornamento effettuato.\"}";
    }
    else if (crud_operation.equals("delete"))
    {
        // Cancellazione di una riga nella tabella
        if ( dmTable.delete( "ID = " + request.getParameter("ID")) )
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore cancellazione.\"}";
        else
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Cancellazione effettuata.\"}";
    }
    else if (crud_operation.equals("update"))
    {
        // Recupero lo statement NON paginato (TODO) e completo di filtri
        String[] hiddenColumns = null;
        if (request.getParameter("hidden_columns") != null)
            hiddenColumns = request.getParameter("hidden_columns").split(",");
        
        getFinalQuery(request); 	
        
        log.info(" Saving filterd query :" + query);
        // Inserimento nuova riga nella tabella
        // TODO Generalizzare entity
        HashMap nuovoFiltro = new HashMap();
        nuovoFiltro.put("IDUTENTE",session.getAttribute("IDUTENTE").toString() );
        nuovoFiltro.put("NOME", request.getParameter("NOME"));
        nuovoFiltro.put("IDFLUSSO", request.getParameter("IDFLUSSO") );
        nuovoFiltro.put("NOTE", request.getParameter("NOTE"));
        nuovoFiltro.put("DESCRIZIONE", query.replaceAll("'", "''"));

        if ( dmTable.update( nuovoFiltro, "ID = " + request.getParameter("ID") ) ){
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore aggiornamento.\"}";
        }else{
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Aggiornamento effettuato.\"}";
        }
    }

	log.debug("final result: "+  result);
    out.write( result );
%>
