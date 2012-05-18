<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="menu-lib.jsp"%>

<%


    try {

        Logger log = Logger.getLogger("report.jsp");
        //String idUtente = session.getAttribute("IDUTENTE").toString();
        // Prendo elenco menuItems
        // Prendo i parametri
        //doGet(request);

    //    request.setAttribute("connections",  confConnection);
        JSONArray mainMenu = new JSONArray();

        Iterator conn = connections.iterator();

        while (conn.hasNext()){
            HashMap<String, String> con = (HashMap) conn.next();
            log.debug(" Creating menu databases loop host:  " + con.get(HOST));
            mainMenu = getConnectionDBMenu(mainMenu,
                    con.get(HOST),
                    con.get(DATABASE),
                    con.get(USER),
                    con.get(PASSWORD),
                    con.get(SHOW_ALL_DATABASE)
            );
            log.debug(" menu databases loop end");
            // Stampo l'errore di accesso negato
            if (mainMenu.isEmpty()) {
                mainMenu.add(getMenuItem("Non sono presenti grafici", null, "disableMe"));
            }
        }


        // E stampiamo sto benedetto JSON

        out.print(mainMenu);
        log.debug("output printed");
    } catch (ConnectionException ex) {
        log.error(ex);
    }
%>
