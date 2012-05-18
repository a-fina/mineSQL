<%@page import="net.mineSQL.controller.NASHandler"%>

<%
    response.setContentType("text/html; charset=iso-8859-1");

    // List request parameters
    String idflusso = (String)request.getParameter("idflusso");
    String id = (String)request.getParameter("id");
    String folderFilter = (String)request.getParameter("folderFilter");
    String sortField = (String)request.getParameter("sort");
    String sortDir = (String)request.getParameter("dir");
    String start = (String)request.getParameter("start");
    String limit = (String)request.getParameter("limit");
    String subFolder = (String)request.getParameter("subfolder");
    
    // Return variables
    boolean success = true;
    String json = "";

    json = NASHandler.list(idflusso, id, folderFilter, sortField, sortDir, start, limit, subFolder);

    out.write(json);
%>
