<%@page import="net.mineSQL.controller.NASHandler"%>

<%
    response.setContentType("text/html; charset=iso-8859-1");

    // List request parameters
    String pathname = (String)request.getParameter("pathname");

    out.write("{\"success\":" + NASHandler.delete(pathname)
            + ",\"valid\":true,\"reason\":\"\"}");
%>
