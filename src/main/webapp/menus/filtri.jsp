<%@include file="menu-lib.jsp"%>

<%@page import="net.mineSQL.ormlite.controller.Menu"%>

<%
	JSONArray treeMenu = new JSONArray();

    try
    {
    //con = ConnectionManager.getConnection("localhost","mineSQL");
    String idUtente = "0";//session.getAttribute("IDUTENTE").toString(); 

    Menu menu = new Menu();
    JSONArray filtri = menu.getFiltersList(null, null);

    log.debug("MARK_filtri :" + filtri);

    //JSONArray filtri = getFiltersList("1", idUtente);
    if ( ! filtri.isEmpty() ) 
        treeMenu.add( getMenuItem("Livello 1", filtri , null) );

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
        treeMenu.add( getMenuItem("Nessun filtro salvato", null, "disableMe") );

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

    // E stampiamo sto benedetto JSON
    out.print(treeMenu);
%>
