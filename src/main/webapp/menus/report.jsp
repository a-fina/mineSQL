<%@include file="menu-lib.jsp"%>

<%
Logger log = Logger.getLogger("report.jsp");

// Database init
String jdbcDefect = "mineSQL";
PreparedStatement ps = null;
ResultSet rs = null;

// Prendo elenco reparti
List  reparti = new ArrayList();

try {
    
        // Prendo i parametri
        doGet(request);
	ps = con.prepareStatement("select * from dm_reparto_t");	
			//ResultSet.TYPE_SCROLL_SENSITIVE,
			//ResultSet.CONCUR_READ_ONLY);
	rs = ps.executeQuery();
	// Mappo tutti i reparti
	while (rs.next()) {
		HashMap reparto_map = new HashMap();
		reparto_map.put("id",rs.getString("idreparto"));
		reparto_map.put("nome",rs.getString("nome"));
		reparti.add(reparto_map);
	}
	rs.close();
	ps.close();
	
	// Scandisco i reparti e prendo le queries
	Iterator iter = reparti.iterator();
	JSONArray treeMenu = new JSONArray();
   
    // Ciclo di costruzione menu principale, elenco dei reparti 
    while ( iter.hasNext (  )  )  {  
    	HashMap reparto_map = new HashMap();
    	reparto_map = (HashMap)iter.next();  	
    	
   		ps = con.prepareStatement("select * from dm_query_t where idreparto=" + reparto_map.get("id"));	  
   		rs = ps.executeQuery();
        
    	JSONArray reparto_queries = new JSONArray();
        // Ciclo di costruzione sottoMenu, elenco delle queries associate ad ogni reparto
   		while (rs.next()) {
            if ( isAutorized( rs.getString("IDRUOLO"),
                              session.getAttribute("IDRUOLO").toString()  ) )
            {
                // Costruzione singolo elemento del sottoMenu
                String id = rs.getString("ID");
                String title = reparto_map.get("nome") + " - " + rs.getString("NOME");
                String tooltip = rs.getString("NOTE");
                if (rs.wasNull())
                    tooltip = "";
                String text = "<span ext:qtip=\"" + tooltip + "\">"+ rs.getString("NOME")+ "</span>";

                reparto_queries.add( getSubMenuItem(id,title,text) );
            }
   		}
   		rs.close();
   		ps.close();

    	// Root reparto
    	if (reparto_queries.size() > 0) {
            String text = reparto_map.get("nome").toString();
		    treeMenu.add( getMenuItem(text, reparto_queries, null) );
    	}
    }  
    // Stampo l'errore di accesso negato
    if ( treeMenu.isEmpty() ){
        treeMenu.add( 
            getMenuItem("Utenza non abilitata ad alcun report", null, "disableMe") 
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
