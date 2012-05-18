<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@page import="java.util.Date" %>
<%@page import="net.mineSQL.controller.Grid" %>
<%@include file="auto-lib.jsp"%>

<%
/**
 * Questa pagina restituisce l'estrazione di un solo campo da una tabella
 */

try {
    con = ConnectionManager.getConnection(jdbcDefect);
    // Prendo i parametri
    doGet(request);

    int rowCounter = 0;

    String where = "id="+idQuery;
    getQueryStatement(request, tableName, where);

    // JSON output
    out.print(query);
	
} catch (SQLException sqle) {
        log.error(sqle);
        JSONObject error = new JSONObject();
        error.put("error",sqle+ "<br/> Controllare la query: " + query);
    	out.print(error);
} finally {
	try {
		con.close();
	}
	catch (Exception ex) {
        JSONObject error = new JSONObject();
        error.put("error",ex.toString());
        out.print(error);
	}
}
%>
