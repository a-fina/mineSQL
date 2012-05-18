<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@include file="menu-lib.jsp"%>

<%
Logger log = Logger.getLogger("scripts.jsp");

    // Prendo i parametri
    doGet(request);

    log.info("Connected to: " + hostName +":"+databaseName);

// Database init
PreparedStatement ps = null;
ResultSet rs = null;

// Prendo elenco menuItems
List  menuItems = new ArrayList();

try {
    /*************************
	ps = con.prepareStatement("select distinct data_rilascio from dm_listacr_t order by data_rilascio desc");	
			//ResultSet.TYPE_SCROLL_SENSITIVE,
			//ResultSet.CONCUR_READ_ONLY);
	rs = ps.executeQuery();
	// Mappo tutti i menuItems
	while (rs.next()) {
		HashMap date_map = new HashMap();
		date_map.put("id",rs.getString("data_rilascio"));
        //Gli spazi &nbsp; servono per fissare un Bug di CSS di firefox
		date_map.put("nome",rs.getString("data_rilascio")+"&nbsp;&nbsp;&nbsp;&nbsp;"); 
		menuItems.add(date_map);
	}
	rs.close();
	ps.close();
	
	// Scandisco i menuItems e prendo le queries
	Iterator iter = menuItems.iterator();
    */
	JSONArray treeMenu = new JSONArray();
    /*

    // Ciclo di costruzione menu principale, elenco dei menuItems 
    while ( iter.hasNext (  )  )  {  
    	HashMap date_map = new HashMap();
    	date_map = (HashMap)iter.next();  	
        ************************/
		// TODO verificare la presenza della tabella sul DB corrente
        String sql = "SELECT ID, nome FROM msq_SCRIPT_T";
   		ps = con.prepareStatement( sql );

   		rs = ps.executeQuery();
        
    	JSONArray reparto_queries = new JSONArray();
        // Ciclo di costruzione sottoMenu, elenco delle queries associate ad ogni reparto
   		while (rs.next()) {
            // Costruzione singolo elemento del sottoMenu
            String title = "";
            String id = "";

            id = ""+rs.getInt("ID");
            title = id + "-" + rs.getString("nome");
            String tooltip = "Staus:&nbsp;" + "TODO TOOLTIP";

            if (rs.wasNull())
                tooltip = "";
            String text = "<span ext:qtip=\"" + tooltip + "\">"+ title + "</span>";

            reparto_queries.add( getSubMenuItem(id,title,text) );
   		}
   		rs.close();
   		ps.close();
    	// Root reparto
    	if (reparto_queries.size() > 0) {
            //String text = date_map.get("nome").toString();
		    treeMenu.add( getMenuItem("Elenco degli script", reparto_queries, null) );
    	}
    /********
    }  
    ***********/
    // Stampo l'errore di accesso negato
    if ( treeMenu.isEmpty() ){
        treeMenu.add( 
            getMenuItem("Elenco script vuoto", null, "disableMe") 
        );
    }


    // E stampiamo sto benedetto JSON
    out.print(treeMenu);
	
} catch (SQLException sqle) {
	switch (sqle.getErrorCode()) {
	default:
		log.error("", sqle);
		break;
	}
} finally {
	try {
	    if (con != null)
		    con.close();
	}
	catch (Exception ex) {
		log.error("", ex);
	}
}	
%>
