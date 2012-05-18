<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="menu-lib.jsp"%>

<%
            Logger log = Logger.getLogger("report.jsp");

// Database init
            String jdbcDefect = "mineSQL";
            PreparedStatement ps = null;
            ResultSet rs = null;
            ResultSet rs2 = null;


//String idUtente = session.getAttribute("IDUTENTE").toString(); 

// Prendo elenco menuItems
            List menuItems = new ArrayList();
            Connection dbCon = null;
            try {
                // Prendo i parametri
                //doGet(request);
                dbCon = ConnectionManager.getConnection(HOST, databaseName, USER, PASSWORD);

                // Elenco dei database
                String where = "";
                if (!SHOW_ALL_DATABASE) {
                    where = " like '" + DATABASE + "'";
                }

                ps = dbCon.prepareStatement("SHOW DATABASES " + where);
                //ResultSet.TYPE_SCROLL_SENSITIVE,
                //ResultSet.CONCUR_READ_ONLY);
                rs = ps.executeQuery();

                // Mappo tutti i menuItems
                String title = "";
                String tip = "";
                int idRow = 0;
                List savedTable = new ArrayList();

                while (rs.next()) {

                    HashMap anagraf_map = new HashMap();
                    anagraf_map.put("id", "idRow" + idRow);
                    log.debug(" row: " + rs.toString());
                    //Gli spazi &nbsp; servono per fissare un Bug di CSS di firefox
                    title = rs.getNString(1) + "&nbsp;&nbsp;&nbsp;&nbsp;";
                    tip = "descrizione riga" + idRow;
                    title = "<span ext:qtip=\"" + tip + "\">" + title + "</span>";

                    anagraf_map.put("nome", title);
                    menuItems.add(anagraf_map);
                    savedTable.add(rs.getNString(1));
                    idRow++;
                }
                rs.close();
                ps.close();

                // Scandisco i menuItems e prendo le queries
                Iterator iter = menuItems.iterator();
                Iterator tablesIter = savedTable.iterator();
                JSONArray mainMenu = new JSONArray();

                // Ciclo di costruzione menu principale, cicla sull'elenco DB
                while (iter.hasNext()) {
                    HashMap anagraf_map = new HashMap();
                    anagraf_map = (HashMap) iter.next();

                    String currentDB = (String) tablesIter.next();

                    String sql = "show tables from " + currentDB;
                    log.debug(" currentDB: " + currentDB);

                    ps = dbCon.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = ps.executeQuery();

                    JSONArray gruppo_menu = new JSONArray();
                    String text = "";
                    // Ciclo di costruzione sottoMenu, per ogni tabella fa elenco dei campi
                    int idUniq = 0;
                    String id = "";
                    String text2 = "";
                    String textTip = "";

                    while (rs.next()) {
                        String tableName = rs.getString("Tables_in_" + currentDB);



                        // Attacco elenco dei campi del database

                        id = idUniq + "##" + currentDB + "##" + tableName;
                        text2 = rs.getString("Tables_in_" + currentDB);
                        textTip = "<span ext:qtip=\"database_name_info\">" + text2 + "</span>";
                        gruppo_menu.add(getSubMenuItem(id, text2, textTip));
                        //log.debug(" gruppo_menu=" + getMenuItem(text2, date_grafico, null) );

                        idUniq++;

                    }
                    rs.close();
                    ps.close();

                    // Attacco elenco dei databases
                    if (gruppo_menu.size() > 0) {
                        text = anagraf_map.get("nome").toString();
                        mainMenu.add(getMenuItem(text, gruppo_menu, null));
                    }
                }

                // Stampo l'errore di accesso negato
                if (mainMenu.isEmpty()) {
                    mainMenu.add(
                            getMenuItem("Non sono presenti grafici", null, "disableMe"));
                }


                // E stampiamo sto benedetto JSON
                out.print(mainMenu);

            } catch (SQLException sqle) {
                switch (sqle.getErrorCode()) {
                    default:
                        log.debug("", sqle);
                        out.println(sqle);
                        break;
                }
            } finally {
                try {
                    dbCon.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
%>
