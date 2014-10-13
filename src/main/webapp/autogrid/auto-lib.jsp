<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>

<%-- @ page import="oracle.sql.CLOB" --%>
<%@ page import="net.sf.json.JSONArray"%>
<%@ page import="net.sf.json.JSONObject"%>
<%@ page import="net.sf.json.JSONSerializer"%>
<%@ page import="org.apache.log4j.Logger" %>

<%@ page import="net.mineSQL.util.*" %>
<%@ page import="net.mineSQL.connection.*" %>
<%@ page import="net.mineSQL.util.Constants" %>
<%@ page import="net.mineSQL.util.Utilita" %>
<%@ page import="net.mineSQL.controller.MineScript" %>
<%@ page import="net.mineSQL.controller.MineTable" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.regex.*" %>

<%@ include file="../config.jsp" %>
<%!
    /* DB settings */
    Connection con = null;

    /* GET/POST parameters */
    String idQuery=null;
    String bodyQuery=null;
    int start = 0;
    int limit = 10;
    String dir = "ASC";
    String sort = "";
    String tableName = "";
    String databaseName = "";
    String hostName = "";
    String pageContext = "";
    String action = "";

    /* Letti dal DB */
    String query=null;
    String querySel=null;
    String nomeQuery=null;

    String DEFAULT_TESTO = "$testo";

    Logger log = Logger.getLogger("auto-json.jsp");
    /*
    * Cattura i parametri postati
    * @param req HttpServletRequest
    */ 
    public void doGet(HttpServletRequest req) throws ConnectionException, SQLException {
        if (req.getParameter("idQuery") != null) idQuery = req.getParameter("idQuery");
        else idQuery = null;
        if (req.getParameter("databaseName") != null) databaseName = req.getParameter("databaseName");
        else databaseName = "mineSQL";
        if (req.getParameter("tableName") != null) tableName = req.getParameter("tableName");
        else tableName = null;
        if (req.getParameter("hostName") != null) hostName = req.getParameter("hostName");
        else hostName = "localhost";
        if (req.getParameter("bodyQuery") != null && ! req.getParameter("bodyQuery").equals("")) 
            bodyQuery = req.getParameter("bodyQuery");
        else bodyQuery = null;
        if (req.getParameter("start") != null) start = (new Integer(req.getParameter("start"))).intValue();
        if (req.getParameter("limit") != null) limit = (new Integer(req.getParameter("limit"))).intValue();
        if (req.getParameter("sort") != null) sort = req.getParameter("sort");
        if (req.getParameter("dir") != null) dir = req.getParameter("dir");
        if (req.getParameter("context") != null) pageContext = req.getParameter("context");
        if (req.getParameter("action") != null) 
            action = req.getParameter("action");
        else action = "dummy";

        log.debug("Try to connecting to: " + hostName +":"+databaseName);
        con = ConnectionManager.getConnection(hostName,databaseName);
    }
    /*
    * Va sul DB e si carica il testo della query da visualizzare nella griglia
    * @param  request
    * @return 
    */ 
    public void getQueryStatement(HttpServletRequest req, String table, String where) throws SQLException{
        if (bodyQuery != null){
            log.info("- - - - > MARK_FILTER bodyQuery: " + bodyQuery );
            query = bodyQuery;
        }else{
            if (where != null)
                where = " WHERE " + where;
			log.debug(" getQueryStatement sqL: " + "select * from "+ table +" "+  where);
            ResultSet rs = doQuery("select * from "+ table +" "+  where);
            while (rs.next()) {
                nomeQuery=rs.getString("nome");
                log.debug("-> MARK_FILTER nome script: CLOB di MERDA XXXX YYY className: " +
                    //rs.getClob("testo").getClass().getName()
                    rs.getString("testo").getClass().getName()
                );
                /*oracle.sql.CLOB value = (oracle.sql.CLOB) rs.getClob("testo");
                query = inputStreamAsString(value);*/
                query = rs.getString("testo");
                querySel = rs.getString("script_out");

                log.debug("-> MARK_FILTER nomeQuery: " + nomeQuery +" testo script:\n " + query); 
            }
            log.info("->  MARK_FILTER query: " + query ); 
        }
    }
    /**
    * Restituisce il numero di righe della query
    * @param query SQL statement
    * @return integer
    */ 
    public int getRowsNumber(String _query) throws SQLException{
        log.debug(" getRowsNumber before executed query: " + _query+ " Con: " + con);
        ResultSet rs = doQuery("select count(*) tot from (" + _query + ") as counteggio");
        rs.next();
        return rs.getInt("TOT");
    }
	/*public String inputStreamAsString(CLOB clob){
		String strValue="";
		
		if (clob != null) {
		    	BufferedReader re = null;
			try {
			    String str;
			    re = new BufferedReader(clob.getCharacterStream());
			    while ((str = re.readLine()) != null) {
			    	strValue += str + '\n';
			    }

			    //strValue = sbuf.toString();
			    if (strValue.length() == 0) strValue = "";
			} catch (SQLException e) {
				log.error("", e);
			} catch (IOException e) {
			    	log.error("", e);
			}
			finally {
			    try {
				re.close();
			    }
			    catch (IOException e) {
				log.error("Chiusura BufferedReader", e);
			    }
			}
		}
		log.debug(">>>>>> inputStreamAsString | strValue: " + strValue);
		return strValue;
	}*/
    /**
    * Prepara lo staement ed esegue la query
    * @param query
    * @return ResultSet
    * @see ResultSet
    */ 
    private ResultSet doQuery(String query) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs  = null;
        log.debug(" before executed query: " + query+ " Con: " + con);
        ps = con.prepareStatement(query,
                                  ResultSet.TYPE_SCROLL_SENSITIVE,
                                  ResultSet.CONCUR_READ_ONLY);
        log.info(" after executed query: " + query);
        rs = ps.executeQuery();
        return rs;
    }
    /**
    * Scandisce la hashMap e costruisce la stringa della clausula WHERE
    * @param hm hashMap[nome, valore]
    * @return   stringa con la clausula where 
    * @see String
    */      
    public String addWhere(String query, HashMap hm){
        String whereClause = " WHERE ";
	    Iterator it = hm.keySet().iterator();
	    Object key = null;
        while ( it.hasNext()  )  {  
            key = it.next();	
            whereClause += " AAA." +key.toString()+ " LIKE  '" +hm.get(key)+ "' ";
            if ( it.hasNext() )
                whereClause += "AND";
        }
        if (! whereClause.equals(" WHERE "))
            query = "select AAA.* from (" + query + ") as AAA " + whereClause;
        return query;
    }


    /**
    * Aggiunge la paginazione alla query
    * @param query
    * @param start
    * @param linit
    */ 
    public String getPaginationOracle(String query, int start, int limit){
		String sql = "select * " +
					"from (select rownum \"#\", a.* " +
						  "from (" +  query + " ) a " +
						  "where  rownum<=" + (start + limit) + ") " +
					"where \"#\" > " + start + " ";
		log.debug(" getPagination sql: " + sql);
		return sql;
    }
    public String getPaginationDB2(String query, int start, int limit){
        String sql = "select * from ( " +
            "select ROW_NUMBER() OVER() as NUM, a.* from " + 
            "( " + query +" )" + 
            " as a) as b where  b.NUM <=" + (start + limit) + " and NUM >" + start + " ";

		log.debug(" getPaginationDB2 sql: " + sql);
		return sql;
    }
    public String getPaginationMySQL(String query, int start, int limit){
		String sql =  query + " LIMIT "+start+", "+limit;
		log.debug(" getPagination sql: " + sql);
		return sql;
    }
    public String getPagination(String query, int start, int limit){
        return getPaginationDB2(query, start, limit);
    }


    // C'era una volta un Export Excel che poteva leggere solo dalla tabella
    // DM_QUERY_T. Laddove questo  metodo venga chiamato senza specificare la tabella
    // verra presa la buon vecchia DM_QUERY_T.
	public void getFinalQuery(HttpServletRequest request) throws SQLException, ConnectionException {
        getFinalQuery(request, "DM_QUERY_T");
    }

    // Assembla i parametri di POST, e restituisce il testo completo della query
    // paginata e con tutti le condizioni impostate	
	public void getFinalQuery(HttpServletRequest request, String table ) throws SQLException, ConnectionException {
        // Prendo i parametri
        //log.info(" auto-excel.jsp | con" + con);
        doGet(request);
        
        int filterCount = 0;
        
        // Estraggo il testo della query
        getQueryStatement(request, table ,"id="+idQuery);
       
        // Se il ho già il corpo della query esco
        // altrimenti aggiungo le clausole WHERE 
        if (bodyQuery != null)
            return;
        
        // Se è un export di un report allora la variabile query contiene già i valori 
        // dei filtri impostati, mentre se sto esportando delle entità devo prelevare i
        // valori dei filtri direttamente dalle variabili in sessione "filter" e "searchCondition"
        if (pageContext.equals("Problem") || pageContext.equals("Defect") ||
            pageContext.equals("Task") || pageContext.equals("Workaround")) {
            
            if ((request.getSession().getAttribute("filter") != null) &&
                (request.getSession().getAttribute("searchCondition") != null)) {
                String addAnd = " AND ";
                if (request.getSession().getAttribute("searchCondition")=="") addAnd = ""; 
                query = query + " WHERE 1 = 1 " +
                        addAnd + 
                        request.getSession().getAttribute("searchCondition")+ 
                        request.getSession().getAttribute("filter");	
                // log.info("3.1 - logan - - ->getFinalQuery MARK_FILTER query: " +  query );
            }
        }// Aggiungo i filtri contestuali dei Report.
        else if (pageContext.equals("Report")) {
            if ((request.getSession().getAttribute("filter") != null) &&
                (request.getSession().getAttribute("searchCondition") != null)) {
                query = "select * from (" + query + ") WHERE 1=1 " + 
                    request.getSession().getAttribute("filter");
                // log.info("3.2 - logan - - ->getFinalQuery MARK_FILTER query: " +  query );
            }
        }
    }

    // Restituisce una mappa dei valori postati
    // Prende tutte la varibili postate che cominciano
    // con MineTable.prefix;
    private HashMap Post2HashMap(HttpServletRequest request){
        HashMap hm = null;
        return hm;
    }

public static void execScript(Connection conn, String stmt) throws SQLException
{
    PreparedStatement st = null;
    try
    {
       st = conn.prepareStatement(stmt.replaceAll("\r", "\n"));
       //st.executeUpdate();
       st.execute();
    }

    finally
    {
       if (st != null) st.close();
    }
}    
    
public static void execSQL(Connection conn, String stmt) throws SQLException
{
InputStream in = null;

//try {
        // InputStream is = new ByteArrayInputStream(text.stmt("UTF-8"));
//} catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//}

Scanner s = new Scanner(in);
s.useDelimiter("(;(\r)?\n)|(--\n)");
Statement st = null;
try
{
st = conn.createStatement();
while (s.hasNext())
{
String line = s.next();
if (line.startsWith("/*!") && line.endsWith("*/"))
{
int i = line.indexOf(' ');
line = line.substring(i + 1, line.length() - " */".length());
}

if (line.trim().length() > 0)
{
st.execute(line);
}
}
}
finally
{
if (st != null) st.close();
}
}


%>
