<%@ page import="java.util.*" %>
  

<%


    String HOST = "HOST";
    String USER = "USER";
    String PASSWORD = "PASSWORD";
    String DATABASE = "DATABASE";
    String SHOW_ALL_DATABASE = "SHOW_ALL_DATABASE";
    String NAME = "NAME";

    HashMap<String, String> confConnection = new HashMap<String, String>();
    List connections = new ArrayList();



    confConnection = new HashMap<String, String>();
    confConnection.put(HOST, "localhost");
    confConnection.put(USER,"root");
    confConnection.put(PASSWORD,"");
    confConnection.put(SHOW_ALL_DATABASE,"true");
    confConnection.put(DATABASE,"mineSQL");
    confConnection.put(NAME,"local");
    connections.add(confConnection);


    /* Add new connection
    confConnection = new HashMap<String, String>();
    confConnection.put(HOST, "1.1.1.1");
    confConnection.put(USER,"");
    confConnection.put(PASSWORD,"");
    confConnection.put(SHOW_ALL_DATABASE,"false");
    confConnection.put(DATABASE,"");
    confConnection.put(NAME,"local");
    connections.add(confConnection);
    */

%>

