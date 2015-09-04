<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="auto-lib.jsp"%>

<%@page import="net.mineSQL.controller.MineTable"%>
<%@page import="net.mineSQL.ormlite.controller.CRUDFactory"%>
<%
String result = "";
try{    
    // Prendo i parametri
    doGet(request);

    log.info("Connected to: " + hostName + ":" + databaseName);

    String crud_operation = request.getParameter("crudOperation");

    // Istanzio il gestore della tabella
    //---- MineTable dmTable = new MineTable(con, request.getParameter("tableName") );
    MineTable dmTable = new MineTable(con, tableName);

    // Gestione CRUD
    if (crud_operation.equals("fieldSet")) //TODO sincronizzare con jsonReader
    {
        String id = "";
        String action = "";

        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }
        if (request.getParameter("idScript") != null) {
            id = request.getParameter("idScript");
        }

        if (id.equals("") || action.equals("runDefaultScript")) {
            result = dmTable.getDefaultFieldSet();
        } else if (!id.equals("") && action.equals("runSavedScript")) {
            // Carico il testo della query
            Dao<Report, Integer> reportDao;
           
            ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
            reportDao = DaoManager.createDao(connectionSource, Report.class);
            String query = reportDao.queryForId(new Integer(id).intValue()).getDescrizione();
            result = dmTable.getDefaultFieldSet(query);

            if (connectionSource != null) {
                connectionSource.close();
            }

        } else {
            result = dmTable.getFieldSetFromField("PARAMETRI", "ID=" + id);
        }
        // Ricavo i metaData dei campi
        //result = dmTable.getFieldSetFromMetadata();
        log.debug("before dmTable.getFieldSetFromField result: " + result);
    } else if (crud_operation.equals("jsonReader")) //TODO sincronizzare con fieldSet
    {
        // Ricavo i metaData dei campi
        result = dmTable.getJsonReader().toString();
    } 
    else if (crud_operation.equals("read")) 
    {
        result = dmTable.read(null, "ID = " + request.getParameter("ID"));
    } 
    else if (crud_operation.equals("create")) 
    {
        // Recupero lo statement NON paginato (TODO) e completo di filtri
        String[] hiddenColumns = null;
        if (request.getParameter("hidden_columns") != null) 
        {
            hiddenColumns = request.getParameter("hidden_columns").split(",");
        }

        String filter = Utilita.getFilterCondition(con, request, "", "", "");
        HashMap formParams = dmTable.getSubmittedParams(request);
        MineScript script = new MineScript();
        
        // testo e' la textarea di default
        query = script.mergeScriptParameters(formParams, DEFAULT_TESTO);  
        if (filter.length() > 0) {
            query = "select FILT_AUX.* from (" + query + ") as FILT_AUX WHERE 1=1 " + filter;
        }
        log.debug(" MARK FUNZA salva merged query: " + query + " params: " + formParams);
        // Inserimento nuova riga nella tabella // TODO Generalizzare entity
        // Oggetto
        int res = 0;
        CRUDFactory crf = new CRUDFactory("report", request, query); 
        res = 1;
        /*******************************************************
        Dao<Report, Integer> reportDao;
        ConnectionSource connectionSource = new JdbcConnectionSource(ORMLite.DATABASE_URL);
        reportDao = DaoManager.createDao(connectionSource, Report.class);

        Report repo = new Report(request.getParameter("NOME"), request.getParameter("NOTE"));
        repo.setDescrizione(query);
        repo.setHost(hostName);
        repo.setDatabase(databaseName);
        repo.setUtente("0");
        int res = reportDao.create(repo);
        if (connectionSource != null) {
            connectionSource.close();
        }
        ******************************************************/

        /**
         * ********************** if (! scriptTable.create( nuovoFiltro ) )   ******************
         */
        if (res == 0) {
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore aggiornamento.\"}";
        } else {
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Aggiornamento effettuato.\"}";
        }
    } else if (crud_operation.equals("delete")) {
        // Cancellazione di una riga nella tabella
        if (dmTable.delete("ID = " + request.getParameter("ID"))) {
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore cancellazione.\"}";
        } else {
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Cancellazione effettuata.\"}";
        }
    } else if (crud_operation.equals("update")) {
        // Recupero lo statement NON paginato (TODO) e completo di filtri
        String[] hiddenColumns = null;
        if (request.getParameter("hidden_columns") != null) {
            hiddenColumns = request.getParameter("hidden_columns").split(",");
        }

        getFinalQuery(request);

        log.info(" Saving filterd query :" + query);
        // Inserimento nuova riga nella tabella
        // TODO Generalizzare entity
        HashMap nuovoFiltro = new HashMap();
        nuovoFiltro.put("IDUTENTE", session.getAttribute("IDUTENTE").toString());
        nuovoFiltro.put("NOME", request.getParameter("NOME"));
        nuovoFiltro.put("IDFLUSSO", request.getParameter("IDFLUSSO"));
        nuovoFiltro.put("NOTE", request.getParameter("NOTE"));
        nuovoFiltro.put("DESCRIZIONE", query.replaceAll("'", "''"));

        if (dmTable.update(nuovoFiltro, "ID = " + request.getParameter("ID"))) {
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore aggiornamento.\"}";
        } else {
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Aggiornamento effettuato.\"}";
        }
    }

} catch (Exception ex) {
    result = "{\"success\":false,\"valid\":true,\"reason\":\""+ex.getMessage()+","+ ex.getCause()+"\"}";
    ex.printStackTrace();
}finally{
    log.debug("final result: " + result);
    out.write(result);
}
%>
