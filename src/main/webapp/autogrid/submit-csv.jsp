<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.Ostermiller.util.ExcelCSVParser"%>
<%-- @ include file="../common/submit.jsp" --%>
<%@ include file="auto-lib.jsp" %>

<%@ page import="net.mineSQL.controller.MineTable"%>
<%@ page import="net.mineSQL.controller.NASHandler"%>

<%
    response.setContentType("text/html; charset=iso-8859-1");

  // Prendo i parametri
    doGet(request);

    // JSON Completo
    JSONObject griglia = new JSONObject();
    Date before = new Date();

    log.info("Connected to: " + hostName +":"+databaseName+ " update: " + tableName);
    Logger log = Logger.getLogger("submit-csv.jsp");

//TODO: leggo il file CSV e setto i parametri nella request simulando
// 	il submit di una form
//
    String result = "";
    String reason = "";
    Integer rowCounter = 0;
    Integer updatedRows = 0;

    if( request.getParameter("COMMIT_CSV") == null ){
        //TODO: mettere qui eventuali controlli sul formato del file
        result = "{\"row\":[{\"success\":false,\"valid\":true,\"reason\":\"valido.\"}]}";
        
    }else{
        HashMap csvRow = new HashMap();
        MineTable listaComp = new MineTable(con, tableName );
        String whereCondition = "";
        String setClause = "";
        String note="";
        
        // Upload request parameters
        String id = "0_0" /*session.getAttribute("username").toString()+"_"+
                    session.getAttribute("groupname").toString()*/;
          
        //String userIdGruppo = session.getAttribute("IDGRUPPO").toString();
        
        String fileName = NASHandler.getDestPath("ImportCSV", id, "") +
                          File.separator + 
                          NASHandler.getFileName(request.getParameter("fileName"));
        /*request.getParameter("TRANSACTIONID")*/;

        log.info("MARK_0: fileName->" + fileName);
        // POST parameters capture
        BufferedReader fileR = null;

        /*
         * Ciclo di lettura delfile CSV
        */
            try {
                String row = null;
                FileReader file = new FileReader(fileName);
                fileR = new BufferedReader(file);
                String[] headers = null;

                while (true) {
                    row = fileR.readLine();
                    if (row == null) {
                        break;
                    } else {
                        rowCounter += 1;

                        //TODO : la prima riga contiene l'header rowcounter == 1
                        String[][] values = ExcelCSVParser.parse(
                            new StringReader(row),','
                        );
                      
                        // Field names
                        log.debug("MARK values CSV: " + values.toString()+ " row: " + row);

                        if ( rowCounter == 1 ){
                            for (int i=0; i<values.length; i++){//righe
                                headers = new String[values[i].length];
                                
                                for (int j=0; j<values[i].length; j++){//colonne
                                    headers[j] = new String(values[i][j]);
                                }
                            }
                        }else{
                            for (int i=0; i<values.length; i++){//righe
                                for (int j=0; j<values[i].length; j++){//colonne
                                    csvRow.put(headers[j], values[i][j]);
                                    //csvRow = CSVtoMap(request, n); 
                                }
                            }

                            log.debug(csvRow);
                            /***********************
                            HashMap gruppo = new HashMap();
                            gruppo.put("IDGRUPPO","");
                            // Decodifica ID in Nome Gruppo	
                            String idGruppo = new Table("CP_GRUPPO_T").read(gruppo,
                                                      " lower(NOME)=lower('" + csvRow.get("IDGRUPPO")+"')",false);
                            log.info(" -------------->idgruppo: " + idGruppo);
                            if (idGruppo.equals("")||idGruppo.contains(",")) {
                                reason += " IDCOMPONENTE: " + csvRow.get("IDCOMPONENTE")+" non aggiornato perchè gruppo non presente </br>";
                                continue;
                            }
                            csvRow.put("IDGRUPPO",idGruppo);

                            whereCondition = "IDCOMPONENTE = "+ csvRow.get("IDCOMPONENTE")+
                                                 " AND BLOCCATO<>1 " +
                                                 " AND (IDGRUPPO="+userIdGruppo+" OR "+userIdGruppo+"=0) "+
                                                 " AND ( NVL(IDDEFECT,-1) <>'"+csvRow.get("IDDEFECT")+"' OR "+
                                                 " IDGRUPPO <>"+csvRow.get("IDGRUPPO")+")";
                                     
                                     setClause = "DATAIMPORT_CSV=SYSDATE";
                                   
                                   if (csvRow.get("NOTE")!=null) {
                                       note = csvRow.get("NOTE").toString();
                                       setClause+=", NOTE='"+note+"'||NOTE";
                                   }
                                   csvRow.remove("NOTE");
                            ***********************/



                            log.info("riga:" + rowCounter);
                            if ( ! listaComp.replace( csvRow ) ){
                                reason += " Riga :" +  updatedRows + " " + csvRow +" error <br/>";
                            }else{
                                updatedRows += 1;
                            }
                        }
                }
           }

      
            /*PostMap pm = new PostMap( csvRow);
            result = CommonOperation.switchOperation("LISTA_COMPONENTI", 
                                  pm.toHashMap(), 
                                  request.getParameter("IDCOMPONENTE"),
                                  pm.onlyCheck() );
            */
        }
        catch (FileNotFoundException e) {
          log.error(e);
        }
        catch (IOException e) {
          log.error(e);
        } finally {
            if (fileR != null) {
                fileR.close();
                /*
                ** Cancellazione file CSV
                */
                NASHandler.delete(fileName);
            }
        }
        reason = "Totale righe: "+rowCounter+" aggiornate: " + updatedRows+ "<br/> "+ reason;
        result = "{\"success\":true,\"valid\":true,\"reason\":\""+reason+"\"}";
    }
    out.write( result );
    %>
