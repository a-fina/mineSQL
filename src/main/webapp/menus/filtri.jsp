<%@page import="net.mineSQL.ormlite.controller.Menu"%>
<%@page import="net.sf.json.JSONArray;"%>

<%
	JSONArray treeMenu = new JSONArray();

    
//    try
//    {
    //con = ConnectionManager.getConnection("localhost","mineSQL");
    String idUtente = "0";//session.getAttribute("IDUTENTE").toString(); 

    Menu menu = new Menu();
    JSONArray filtri = menu.getFiltersList(null, null);


    //JSONArray filtri = getFiltersList("1", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( menu.getMenuItem("Livello 1", filtri , null) );

    /**********
    filtri = getFiltersList("2", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( getMenuItem("Workaround", filtri , null) );
    
    filtri = getFiltersList("3", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( getMenuItem("Defect", filtri , null) );

    filtri = getFiltersList("4", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( getMenuItem("Task", filtri , null) );

    filtri = getFiltersList("5", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( getMenuItem("Report", filtri , null) );
    
    filtri = getFiltersList("6", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( getMenuItem("Multi-Task", filtri , null) );
    ******************/

    if ( treeMenu.isEmpty() )
        treeMenu.add( menu.getMenuItem("Nessun filtro salvato", null, "disableMe") );

/*    } catch (SQLException sqle) {
        switch (sqle.getErrorCode()) {
        default:
            log.error("", sqle);
            break;
        }
    }
*/	

    // E stampiamo sto benedetto JSON
    out.print(treeMenu);
%>
