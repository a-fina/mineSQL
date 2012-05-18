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
List  menuItems = new ArrayList();

try {
        // Prendo i parametri
        doGet(request);
        ps = con.prepareStatement("SHOW DATABASES");
			//ResultSet.TYPE_SCROLL_SENSITIVE,
			//ResultSet.CONCUR_READ_ONLY);
	rs = ps.executeQuery();
	// Mappo tutti i menuItems
	String title = "";
    String tip = "";
    int idRow = 0;
    List  savedTable = new ArrayList();

	while (rs.next()) {

		HashMap anagraf_map = new HashMap();
		anagraf_map.put("id","idRow"+idRow);
		log.debug(" row: " + rs.toString());
        //Gli spazi &nbsp; servono per fissare un Bug di CSS di firefox
        title = rs.getString("Database")+"&nbsp;&nbsp;&nbsp;&nbsp;";
        tip = "descrizione riga" + idRow; 
        title = "<span ext:qtip=\""+ tip  +"\">"+ title + "</span>";

		anagraf_map.put("nome", title); 
		menuItems.add(anagraf_map);
		savedTable.add(rs.getString("Database"));
        idRow++;
	}
	rs.close();
	ps.close();

	// Scandisco i menuItems e prendo le queries
	Iterator iter = menuItems.iterator();
    Iterator tablesIter = savedTable.iterator();
	JSONArray mainMenu = new JSONArray();

    // Ciclo di costruzione menu principale, cicla sull'elenco DB
    while ( iter.hasNext (  )  )  {  
    	HashMap anagraf_map = new HashMap();
    	anagraf_map = (HashMap)iter.next();  	
    
        String currentDB = (String)tablesIter.next();

        String sql = "show tables from " +  currentDB;
        log.debug(" currentDB: " + currentDB);

   		ps = con.prepareStatement( sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );
   		rs = ps.executeQuery();
        
    	JSONArray gruppo_menu = new JSONArray();
        String text = "";
        // Ciclo di costruzione sottoMenu, per ogni tabella fa elenco dei campi
		int idUniq = 0;
   		while (rs.next()) {
    	    JSONArray date_grafico = new JSONArray();
			String tableName = rs.getString("Tables_in_" + currentDB);

			// Informazioni sui campi della tabella
			sql = "show columns from "+ currentDB+"."+tableName;
			ps = con.prepareStatement( sql , ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY );
			rs2 = ps.executeQuery();

            while(rs2.next()){
                // Costruzione singolo elemento del sottoMenu
                String id = idUniq + "##" + currentDB + "##" + tableName;
                title = "NAME: "+ rs2.getString("Field")+"&nbsp;&nbsp;&nbsp;TYPE: "+ rs2.getString("Type");
                //String id = idUniq+"-"+currentDB+;
                text = "<span ext:qtip=\"Null: " + rs2.getString("Null") 
					+ " Key: " + rs2.getString("Key")
					+ " Default: " + rs2.getString("Default")
					+ " Extra: " + rs2.getString("Extra")
					+ "\">"+ title + "</span>";
                date_grafico.add( getSubMenuItem(id,title,text) );

        /**********************************
                try{
                    rs.next();
     
                    if ( ! rs.getString("GRUPPO_MENU").equals( nuovoGruppo )  ){
                        stessoGruppo = false;
                        nuovoGruppo = rs.getString("GRUPPO_MENU");
                        rs.previous();
                    }
                }catch(SQLException sqle){
                    break;
                }
        **********************************/
            }
			rs2.close();

            // Root reparto
            if (date_grafico.size() > 0) {
                String text2 = rs.getString("Tables_in_" + currentDB);
                gruppo_menu.add( getMenuItem(text2, date_grafico, "nulllllo") );
				log.debug(" gruppo_menu=" + getMenuItem(text2, date_grafico, null) );
            }
			idUniq++;

   		}
   		rs.close();
   		ps.close();

    	// Root reparto
    	if (gruppo_menu.size() > 0) {
            text = anagraf_map.get("nome").toString();
		    mainMenu.add( getMenuItem(text, gruppo_menu, null) );
    	}
    }  

    // Stampo l'errore di accesso negato
    if ( mainMenu.isEmpty() ){
        mainMenu.add( 
            getMenuItem("Non sono presenti grafici", null, "disableMe") 
        );
    }


    // E stampiamo sto benedetto JSON
    out.print(mainMenu);
	
} catch (SQLException sqle) {
	switch (sqle.getErrorCode()) {
	default:
        log.debug("",sqle);
		out.println(sqle);
		break;
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
