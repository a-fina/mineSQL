<%@ page pageEncoding="ISO-8859-1" contentType="text/html; charset=iso-8859-1" %>
<%@ page import="net.mineSQL.util.SessionWatcher" %>
<%@ page import="net.mineSQL.util.ApplicationWatcher" %>
<%@ page import="net.mineSQL.connection.ConnectionManager" %>
<%@ page import="net.mineSQL.connection.ORMLite" %>

<%
if (request.getParameter("foo") != null) {
    return; // Stop request
}
%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<html>
<head>
	<title>System Status</title>
</head>
<body>
	<p align="center">
    <h3>Application</h3>
    Vesrion: <%= ApplicationWatcher.version %><br/>
	Hosted on: <%= ApplicationWatcher.hostName%><br/>
    Started on: <%= ApplicationWatcher.applicationStarDate%><br/>
    
    <h3>MineSQL DB</h3>
    path: <%= ORMLite.path %><br/>
    file: <%= ORMLite.dbName %>
	
    <h3>Sessions</h3>
    Actives : <%= SessionWatcher.getActiveSessions() %><br/>
	<%
    // Scorro tutte le sessioni
	Iterator it = SessionWatcher.getSessions().keySet().iterator();
	while (it.hasNext()){
		String key = (String)it.next();
        out.write("<b>ID:</b> "+key+" ");
        // Stampo dettagli della sessione 
		HashMap ses = SessionWatcher.getSession(key);
        Iterator ises = ses.keySet().iterator();
        while (ises.hasNext()){
            String k = (String)ises.next();
            out.write("<b>"+k+":</b> "+ses.get(k)+"  ");
        }
        out.write("<br/>");
	}
	%>
    <h3>Connections</h3>
    Appended : <%= ConnectionManager.getCurrentConnection() %><br/> 
	</p>


</body>
</html>
