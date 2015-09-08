<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@page import="net.mineSQL.ormlite.controller.Menu"%>
<%@page import="net.sf.json.JSONArray;"%>

<%
        Menu menu = new Menu();
        JSONArray elencoDatabases = menu.getDatabaseList();

        out.print(elencoDatabases);

%>
