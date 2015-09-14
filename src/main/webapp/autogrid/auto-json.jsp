<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@page import="java.util.Date" %>
<%@ page import="net.mineSQL.connection.ConnectionManager" %>
<%@ page import="net.mineSQL.connection.ORMLite" %>
<%@page import="net.mineSQL.controller.MineGrid" %>
<%@include file="auto-lib.jsp"%>

<%
    try {

        // Prendo i parametri
        doGet(request);

        // JSON Completo
        JSONObject griglia = new JSONObject();
        Date before = new Date();

        log.info("Connected to: " + hostName + ":" + databaseName);

        // Applico il filtro la query
        String filter = Utilita.getFilterCondition(con, request, "", "", "");
        //TODO: questa pagina auto-json.jsp viene richiamata sia quando si submita la form
        //      con i parmaetri dello script, sia quando si mettono i filtri sulla griglia. 
        MineScript script = new MineScript();
        MineTable table = new MineTable(con, databaseName, tableName, dbType );

        PreparedStatement ps = null;
        ResultSet rs = null;
        int rowCounter = 10;

        log.debug(" MARK_ENTRY database:" + databaseName + " tablename:" + tableName + " action: " + action);
        if (action.equals("runScript") || action.equals("saveScript")) {
            HashMap formParams = table.getSubmittedParams(request);
            query = script.mergeScriptParameters(formParams, DEFAULT_TESTO);  // testo e' la textarea di default
            log.debug(" MARK_runScript query: " + query + " params: " + formParams + " querySel: " + querySel);
            //Definito in auto-lib.jsp, non produce output ed seguo lo SCRIPT riga per riga
            script.execScript(con, query);

            Date after = new Date();
            long diff = after.getTime() - before.getTime();

            log.info(" Run script : " + query);
            log.info(" Execution time: " + diff + "ms");
            griglia.put("Script", "Successfully finished in " + diff + "ms");
        }
        else if ((action.equals("runQuery")) || action.equals("runDefaultScript") || action.equals("runSavedScript")) {

            // Se submit dalla FORM eseguo lo SCRIPT e successivamente eseguo lo SCRIPT_OUTPUT
             if (action.equals("runQuery")) {

                // quando si ricarica solo la griglia, lo SCRIPT non viene piu eseguito ma
                // si eseguo solo SCRIPT_OUTPUT che ricarica i dati della griglia
                // Creare sul DB una nova tabella in cui mettere delle query che estraggono
                // solo i dati di l'output. 
                // Serve per forza un query di estrazione perché le colonne della griglia
                // devono essere cablate in qualche modo
                // TODO modificare Autogrid.js in modo da diversificare il nome di tableName
                // con i parametri di POST
                // Testo libero TODO
/****************
                Connection conForsavedScript = ConnectionManager.getConnection("localhost", "mineSQL");
                MineTable runQuery = new MineTable(conForsavedScript, "msq_SCRIPT_T");
                query = runQuery.select("testo", "ID = '" + idQuery + "'");

                HashMap formParams = table.getSubmittedParams(request);
                query = script.mergeScriptParameters(formParams, query);  // testo e' la textarea di default

**********/
                HashMap formParams = table.getSubmittedParams(request);
                query = script.mergeScriptParameters(formParams, DEFAULT_TESTO);  // testo e' la textarea di default
                log.debug(" MARK_runScript query: " + query + " params: " + formParams + " querySel: " + querySel);
            } else if (action.equals("runDefaultScript")) {
                // Preparo la query con Paginazione
                String db_table = databaseName + "." + tableName;
                query = "SELECT * FROM " + db_table + " WHERE 1=1";
                //getQueryStatement(request,tableName, where);
                log.debug(" MARK_runDefaultScriptquery database:" + databaseName + " tablename:" + tableName);
                //TODO gestire la query a test libero, viene postata la variabuile query_body
            } else if (action.equals("runSavedScript")) {
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
            // Costruzione query coni filri utente
            if (filter.length() > 0) {
                query = "select FILT_AUX.* from (" + query + ") as FILT_AUX WHERE 1=1 " + filter;
                request.getSession().setAttribute("filter", filter);
                request.getSession().setAttribute("searchCondition", "");
                log.info("3 - - - ->getQueryStatement MARK_FILTER query: " + query);
            } else {
                request.getSession().removeAttribute("searchCondition");
                request.getSession().removeAttribute("filter");
                log.info("3.1 - - - ->getQueryStatement MARK_FILTER query reset filter: " + query);
            }

            // TODO qui si può salvare la query con i FILTRI 
            if (action.equals("saveScript")) {

                String DATABASE_URL = ORMLite.DATABASE_URL;
                Dao<Report, Integer> reportDao;
                ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);

                reportDao = DaoManager.createDao(connectionSource, Report.class);

                Report repo = new Report(request.getParameter("NOME"), request.getParameter("NOTE"));
                repo.setDescrizione(query);
                repo.setHost(hostName);
                repo.setDatabase(databaseName);
                repo.setUtente("0");
                reportDao.create(repo);

                int res = reportDao.create(repo);
                if (connectionSource != null) {
                    connectionSource.close();
                }
            }

            // Paginazione
            rowCounter = script.getRowsNumber(con, query);
            query = script.getPagination(query, start, limit);
            log.debug("4.2 query paginata MARK_FILTER : " + query);

            // Eseguo effettivamente la query
            log.debug(query);
            ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            Date after = new Date();
            long diff = after.getTime() - before.getTime();

            ResultSetMetaData rsMd = rs.getMetaData();
            int numberOfColumns = rsMd.getColumnCount();

            log.info(" Run report query : " + query);
            log.info(" Execution time: " + diff + "ms");
            rs.last();
            int countRow = rs.getRow();
            rs.beforeFirst();

            //TODO Crere un nuovo constructor in MineGrid.java che prende in input
            // il testo della query già completa di filtri vari
            // Colonne
            if (request.getParameter("column") != null) {
                //TODO qui si puo richiamare MineGrid.java -> jsonColumeModel() 
                JSONArray array_colo = new JSONArray();
                for (int i = 1; i <= numberOfColumns; i++) {
                    JSONObject colonna = new JSONObject();
                    String valore = rsMd.getColumnLabel(i);
                    colonna.put("id", valore);
                    colonna.put("header", valore);
                    colonna.put("dataIndex", valore);

                    if (valore.equals("IDTASK")) {
                        colonna.put("renderer", "renderTask");
                    }
                    if (!valore.equals("#")) {
                        array_colo.add(colonna);
                    }
                } // return array_colo
                griglia.put("column", array_colo);
            }

            // Fields
            if (request.getParameter("meta") != null) {
                //TODO qui si puo richiamare MineGrid.java -> jsonResultSet() 
                JSONObject metaData = new JSONObject();
                JSONArray array_colo = new JSONArray();
                for (int i = 1; i <= numberOfColumns; i++) {
                    JSONObject colonna = new JSONObject();
                    colonna.put("name", rsMd.getColumnLabel(i));
                    if (!colonna.equals("#")) {
                        array_colo.add(colonna);
                    }
                }
                metaData.put("totalProperty", "results");
                metaData.put("root", "rows");
                //metaData.put("id",rsMd.getColumnLabel(1));
                metaData.put("fields", array_colo); // return metaData
                griglia.put("metaData", metaData);
            }
            // Rows
            if (request.getParameter("data") != null) {
                griglia.put("results", "" + rowCounter);

                MineGrid jsonGrid = new MineGrid(rs);
                boolean render = true;
                griglia.put("rows", jsonGrid.getJSON(render));
            }

            // JSON output
            out.print(griglia);
            rs.close();
            ps.close();
        }

    } catch (SQLException sqle) {
        JSONObject error = new JSONObject();
        // Loop through the SQL Exceptions TODO portare nel compending e idea
        error.put("error", sqle + "<br/> Controllare la query: " + query);
        while (sqle != null) {
            log.error("State  : " + sqle.getSQLState());
            log.error("Message: " + sqle.getMessage());
            log.error("Error  : " + sqle.getErrorCode());

            sqle = sqle.getNextException();
        }
        out.print(error);
    } finally {
        try {
            con.close();
        } catch (Exception ex) {
            JSONObject error = new JSONObject();
            error.put("error", ex.toString());
            out.print(error);
        }
    }
%>
