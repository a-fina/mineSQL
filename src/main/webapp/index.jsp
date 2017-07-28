<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//IT">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@page import="net.mineSQL.controller.UtenteServlet"%>
<%@page import="net.mineSQL.util.Utilita"%>
<%@page import="net.mineSQL.util.SessionWatcher" %>
<%@page import="net.mineSQL.util.ApplicationWatcher" %>
<%@page import="java.util.GregorianCalendar" %>
<%@page import="org.apache.log4j.Logger"%>

<%
String listaUtenti = null;
String listaAree = null;
String listaStati = null;
String user = "";
String group = "";
String idruolo = "";
String groups = "";
String idreparto = "";
String nomeReparto = "";
String nomeRuolo = "";

Logger log = Logger.getLogger("index.jsp");

boolean CONTROLLO_ACCESSO = true;

boolean loggedin = false;

user = "user";
group = "0";
idruolo = "0";
groups = "[]";

if (CONTROLLO_ACCESSO){
    /*
	if (request.getSession().getAttribute("IDUTENTE") == null ||
		request.getSession().getAttribute("IDUTENTE").toString().length() == 0 ||
		! request.isRequestedSessionIdValid())
    {
        //response.sendRedirect("/login.jsp");
        return;
    }
    **/

    try {
        user = session.getAttribute("username").toString();
        group = session.getAttribute("groupname").toString();
        groups = UtenteServlet.getUserGroupJson(user, group);
        // Il ruolo serve per gestire nella MainView la visualizzazione del menu di Admin
        idruolo = session.getAttribute("IDRUOLO").toString();
        // Il reparto condiziona l'abilitazione del bottone "Crea nuovo problem"
        idreparto = session.getAttribute("IDREPARTO").toString();
        nomeReparto = session.getAttribute("NOME_REPARTO").toString();
        nomeRuolo = session.getAttribute("NOME_RUOLO").toString();

    }
    catch (NullPointerException ex) {
        log.error(" Error on main page");
    }

}

log.debug(" session.getId(): " + session.getId() +" request.getRemoteAddr(): " + request.getRemoteAddr());
SessionWatcher.addSession(session.getId(),"remoteAddress",request.getRemoteAddr());

String urlToGo = "/UtenteServlet";


/*
String valore(String nomeCampo) {
	if (nomeCampo.equals("id_area")) return listaAree;
	if (nomeCampo.equals("id_utente")) return listaUtenti;
	if (nomeCampo.equals("stato")) return listaStati;
	return "";
}
	*/
%>
<html>
<head>
    <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
    <META name="language" content="it">
    <META Http-Equiv="Cache-Control" Content="no-cache">
	<META Http-Equiv="Pragma" Content="no-cache">
	<META Http-Equiv="Expires" Content="0">

    <title>MineSQL - Application Service Accellerator</title>
    <!-- Ext requirement -->
    <link href="/favicon.png" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="ext-3.4.0/resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="ext-3.4.0/resources/css/xtheme-gray.css" />

    <script type="text/javascript" src="ext-3.4.0/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="ext-3.4.0/ext-all-debug.js"></script>

    <!-- script type="text/javascript" src="http://openlayers.org/api/2.10/OpenLayers.js"></script>
    <script type="text/javascript" src="GeoExt/lib/GeoExt.js"></script -->

    <script type="text/javascript" src="js/windows/WindowFormTable.js"></script>
    <!-- Ext Additional Plugin -->
    <% if ( Utilita.isLoggedIn(request) ){ %>
        <script type="text/javascript" src="js/ext-plugin/RemoteValidator.js"></script>
        <script type="text/javascript" src="js/ext-plugin/RowExpander.js"></script>
        <script type="text/javascript" src="js/ext-plugin/GroupSummary.js"></script>
        <script type="text/javascript" src="js/ext-plugin/SearchField.js"></script>
        <script type="text/javascript" src="js/ext-plugin/TableGrid.js"></script>
        <script type="text/javascript" src="js/ext-plugin/FieldEnh.js"></script>
        <script type="text/javascript" src="js/ext-plugin/DateFieldEnh.js"></script>
        <script type="text/javascript" src="js/ext-plugin/HtmlEditorEnh.js"></script>
        <script type="text/javascript" src="js/ext-plugin/FileUploadField.js"></script>
        <script type="text/javascript" src="js/al.js"></script>

        <!-- Griglie e componenti correlati -->
        <script type="text/javascript" src="js/grid/GridFilter.js"></script>
        <script type="text/javascript" src="js/grid/GridSearch.js"></script>
        <script type="text/javascript" src="js/grid/GridToolbar.js"></script>
        <script type="text/javascript" src="js/grid/EditableItem.js"></script>
        <script type="text/javascript" src="js/grid/GridFilters.js"></script>
        <script type="text/javascript" src="js/grid/filter/Filter.js"></script>
        <script type="text/javascript" src="js/grid/filter/ListFilter.js"></script>
        <script type="text/javascript" src="js/grid/filter/StringFilter.js"></script>
        <script type="text/javascript" src="js/grid/GridAdvancedFilters.js"></script>
        <script type="text/javascript" src="js/grid/AutoGrid.js"></script>
        <script type="text/javascript" src="js/windows/ScriptSQL.js"></script>
        <script type="text/javascript" src="js/windows/UploadWindow.js"></script>
        <script type="text/javascript" src="js/MainView.js"></script>
        <script type="text/javascript" src="js/GenericMenu.js"></script>

        <script type="text/javascript" src="js/model-form/FilterMngr.js"></script>
        <script type="text/javascript" src="js/model-form/PostMngr.js"></script>
        <script type="text/javascript" src="js/model-form/CSVMngr.js"></script>
        <script type="text/javascript" src="js/model-form/DatabaseMngr.js"></script>
        <script type="text/javascript" src="js/model-form/TimesheetMngr.js"></script>
        <script type="text/javascript" src="js/model-form/AnagraficaClientiMngr.js"></script>
        <script type="text/javascript" src="js/model-form/OrdiniMngr.js"></script>

    <% } else { %>
        <script type="text/javascript" src="js/LoginView.js"></script>
        <script type="text/javascript" src="js/model-form/LoginMngr.js"></script>
    <% } %>

    <!-- i css servono per correggere dei bug di extjs -->


    <link type="text/css" rel="stylesheet" href="css/mineSQL.css" />

    <style type="text/css">
		.x-grid3-hd-row td.ux-filtered-column {
        font-style: italic;
        font-weight: bold;
    }
	</style>

</head>
<body>
    <!-- Configurazione ambiente e variabile globali -->
    <script type="text/javascript">
        var ACTIVATE_DEBUG_LOG = false;
        var RUOLO_SUPERUSER = 'RUOLO_2';

        var ID_FLUSSO_PROBLEM   = 1;
        var ID_FLUSSO_DEFECT    = 3;
        var ID_FLUSSO_WORKAROUND= 2;
        var ID_FLUSSO_TASK      = 4;
        var ID_FLUSSO_GENERIC   = 5;
        var RUOLO_READONLY      = "3";
        var RUOLO_READWRITEONLY = "4";

        var HOST = "<%=request.getServerName()+ ":" + request.getServerPort()%>";
        var USER_NAME = "<%=user%>";
        var USER_GROUP= "<%=group%>";
        var USER_NOME_RUOLO = "<%=nomeRuolo%>";
        var USER_IDREPARTO = "<%=idreparto%>";
        var USER_NOME_REPARTO= "<%=nomeReparto%>";
        var HOST_NAME = "<%=ApplicationWatcher.hostName%>";
        var DM_VERSION = "<%=ApplicationWatcher.version%>";
        var USER_IDRUOLO = "<%=idruolo%>";
        gruppiDisponibili = <%=groups%>;
        /*
         * Tutti i filtri in sessione
         */
        SessionFilters = {};
        SessionFilters[ID_FLUSSO_PROBLEM] = {};
        SessionFilters[ID_FLUSSO_DEFECT] = {};
        SessionFilters[ID_FLUSSO_WORKAROUND] = {};
        SessionFilters[ID_FLUSSO_TASK] = {};

        /**
         * Stampa messaggi d'errore nella console javascript
         **/
        function debug(string){
            if (ACTIVATE_DEBUG_LOG){
                try     { console.log(this +": " +string); }
                catch(e){ }
            }
        }
        var maxInactiveInterval = <%=session.getMaxInactiveInterval()%>;
        var tplCombo =
                new Ext.XTemplate('<tpl for="."><div ext:qtip="{ID}. {NOME}" class="x-combo-list-item">{NOME}</div></tpl>');

        function openWindows(){
                ScriptSQL.openWindow();
        }
        /**
         * Definisce la versione corrette dell'applicazione
         */
        function setVersion(ver){
            Ext.get('versione').update(ver);
        }
        /**
        * Short hand
        */
        $ = Ext.get;
        $$ = Ext.getCmp;
        /**
         * Init quick tips
         */
        Ext.QuickTips.init();

        /*
        * Go to main page
        */
        function viewMain(){
            window.location.href="http://<%out.write(request.getServerName()+ ":" + request.getServerPort());%>/UtenteServlet";
        }

        /**
        * Check if current user is limited to disable various buttons
        */
        function isLimitedUser(){
            if ( USER_IDRUOLO == RUOLO_READONLY ||
                 USER_IDRUOLO == RUOLO_READWRITEONLY)
                return true;
            else
                return false;
        }

    </script>

<div id="contenitore">

    <!-- Header -->
    <div id="intestazione">
	    <table cellspacing="0" cellpadding="0" width="100%" >
			<tr>
			  <td colspan="3">
				<table cellspacing="0" cellpadding="0" width="100%" border="0">
					<tr>
					  <td width="477">
						<table cellspacing="0" cellpadding="0" width="100%" border="0" bordercolor="red">
						  <tr>
							<td height="62" width="100%">
							  <!-- img hspace="0" src="http://recensioni-videogiochi.dvd.it/images/world_in_conflict/world-in-conflict-3-m.jpg" width="477"  height="62" border="0" align="top" -->
							</td>
						  </tr>
						  <tr>
							<td height="25" width="100%">
                                <!-- Form fittizia usata per effettuare il cambio gruppo, ricarica la pagina -->
								<form name="groupForm" action="<%=urlToGo%>" method="post">
									<input type="hidden" name="username" value="<%=user%>">
									<input type="hidden" id="idgruppo" name="idgruppo" value="">
									<input type="hidden" id="RUOLO_<%=idruolo%>">
									<input type="hidden" name="operazione" value="CHOICE">
									<input type="hidden" name="onchange" value="null">
								</form>
							</td>
						  </tr>
						</table>
					  </td>
					  <td width="100%"></td>
					  <td width="93"><!--img hspace="0" src="img/Bottom_Sx.gif" width="199" height="87" border="0" align="top"--></td>
					</tr>
				</table>
			  </td>
			</tr>
		</table>
    </div>

    <!-- Corpo centrale -->
        <div id="sommario">

        </div>
        <!-- Main Body, filled with ExtJS -->
        <div id="corpo">
            <div id="principale"></div>
            <div id="secondario"></div>
            <div id="fi-form"></div>
        </div>
        <div id="esclusiva" class="execute"></div>

</div>

</body>
</html>
