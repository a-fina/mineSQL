<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="menu-lib.jsp"%>

<%


    try {

        Logger log = Logger.getLogger("databases.jsp");
        //String idUtente = session.getAttribute("IDUTENTE").toString();
        // Prendo elenco menuItems
        // Prendo i parametri
        //doGet(request);

    //    request.setAttribute("connections",  confConnection);
        JSONArray mainMenu = new JSONArray();

        Iterator conn = connections.iterator();

        while (conn.hasNext()){
            HashMap<String, String> con = (HashMap) conn.next();
            log.info("Creating menu databases loop HOST:  " + con.get(HOST)+" DATABASE: " + con.get(DATABASE) + " TYPE: " + con.get(TYPE));
            try{
                mainMenu = getConnectionDBMenu(mainMenu,
                        con.get(HOST),
                        con.get(DATABASE),
                        con.get(USER),
                        con.get(PASSWORD),
                        con.get(SHOW_ALL_DATABASE),
                        con.get(TYPE)
                );
                log.debug(" menu databases loop end");
            }catch(Exception ex){
                mainMenu.add(getMenuItem("Impossibile connettersi a " + con.get(HOST) + " "  + con.get(DATABASE) , null, "disableMe"));
            }
            // Stampo l'errore di accesso negato
            if (mainMenu.isEmpty()) {
                mainMenu.add(getMenuItem("Non sono presenti grafici", null, "disableMe"));
            }
        }
        // E stampiamo sto benedetto JSON
        out.print(mainMenu);
    } catch (Exception ex) {
        log.error(ex);
    }
%>
