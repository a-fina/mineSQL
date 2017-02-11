package net.mineSQL.util;

import net.mineSQL.connection.ConnectionException;
import net.mineSQL.connection.ConnectionManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class Utilita {

    public static String USERNAME = "USERNAME";
    public static String PASSWORD = "PASSWORD";
    public static String ISLOGGEDIN = "ISLOGGED";

    private static final Logger log = Logger.getLogger(Utilita.class);
		
	public static String timeStampToString(Timestamp t, String format) {
//		String format = "dd/MM/yyyy";
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.S");
		String property = t.toString();
		String out=null;
		try
		{
			java.util.Date dataOut = (java.util.Date) formatter.parse(property, new java.text.ParsePosition(0));
			SimpleDateFormat formatter1 = new SimpleDateFormat (format);
			out = formatter1.format(dataOut);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		return out;
	}

	public static String eliminaDuplicati(String stringa, String separator){
		StringTokenizer str = new StringTokenizer(stringa,separator);
		String array[] = new String[str.countTokens()];
		int i=0;
		while (str.hasMoreTokens()){
			array[i] = str.nextToken();
	 		i++;
			
		}
		
		String risultato="";
		HashMap map = new HashMap();
	 
		for (int j=0;j<array.length;j++){
			map.put(array[j].trim(),array[j].trim());
			
			}
		Iterator it = map.values().iterator();
		while(it.hasNext()){
			risultato+=it.next()+" - ";
		}
		risultato = risultato.substring(0,risultato.length()-3);
			return risultato;
	}

	public static int nextValue(Connection con, String table, String field, String condition) {
		//Connection con = null;
		try {
			String jdbcDefect="mineSQL";
		//	con = ConnectionManager.getConnection(jdbcDefect);
			Statement st = con.createStatement();
            log.info("FW-UR-280509 query: " + "SELECT NVL("+field+",0) +1 AS NEXTVALUE FROM "+table+" WHERE "+condition);
			ResultSet rsId= st.executeQuery("SELECT NVL("+field+",0) + 1 AS NEXTVALUE FROM "+table+" WHERE "+condition);
			rsId.next();
			int id=rsId.getInt("NEXTVALUE");
            log.info("FW-UR-280509 "+field+": "+ id);
            		rsId.close();
			st.close();
			return id;
		//} catch (ConnectionException e) {
		//	e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		/**    try {
	    		if (con != null)
	    		    con.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }***/
		}
		return 0;
	}
    public static boolean isLoggedIn(HttpServletRequest request){
        if ( request.getSession().getAttribute(USERNAME) != null ){
            String username = request.getSession().getAttribute(USERNAME).toString();
            if ( request.getSession().getAttribute(username) != null)
                if ( request.getSession().getAttribute(username).equals(ISLOGGEDIN) )
                    return true;
        }
        return false;

    }

	public static int nextId(Connection con, String table) {
		//Connection con = null;
		try {
			String jdbcDefect="mineSQL";
			//con = ConnectionManager.getConnection(jdbcDefect);
			Statement st = con.createStatement();
			ResultSet rsId= st.executeQuery("SELECT DM_"+table+"_SEQ.nextval FROM DUAL");
			rsId.next();
			int id=rsId.getInt("NEXTVAL");
			st.close();
			return id;
		//} catch (ConnectionException e) {
		//	e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	    /*	try {
	    		if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}
		return 0;
	}
	
	/***public static int nextId(Connection con, String table) {
		try {
			Statement st = con.createStatement();
			ResultSet rsId= st.executeQuery("SELECT DM_"+table+"_SEQ.nextval FROM DUAL");
			rsId.next();
			int id=rsId.getInt("NEXTVAL");
			st.close();
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}***/

	public static String escapeHTML(String string) {
		
	    StringBuffer sb = new StringBuffer(string.length());
	    int len = string.length();
	    char c;

	    for (int i = 0; i < len; i++)
	    {
	        c = string.charAt(i);
	        if (c == ' ') { 
	            sb.append(' ');
	        }
	        else {
	            if (c == '&')
	                sb.append("&amp;");
	            else if (c == '<')
	                sb.append("&lt;");
	            else if (c == '>')
	                sb.append("&gt;");
	            else if (c == '\n')
	                // Newline
	                sb.append("&lt;br/&gt;");
	            else {
	                    sb.append(c);
	                int ci = 0xffff & c;
	                if (ci < 160 )
	                    // 7 Bit
	                    sb.append(c);
	                else {
	                	// sistema unicode
	                	if (ci == 224)
		                	sb.append("a'");
		                else if (ci == 232 || ci == 233)
		                	sb.append("e'");
		                else if (ci == 236)
		                	sb.append("i'");
		                else if (ci == 242)
		                	sb.append("o'");
		                else if (ci == 249)
		                	sb.append("u'");
		                else {
		                	sb.append("&#");
		                	sb.append(new Integer(ci).toString());
		                	sb.append(';');
	                    }
	                }
	            }
	        }
	    }
	    return sb.toString();
	}

	public static String getFilterCondition(Connection con, HttpServletRequest request, String idutente, String idgruppo, String flusso) {
		String filter = "";
		if (request.getParameter("filter") != null)
			filter = request.getParameter("filter");
	   
        log.debug(" idgruppo: " + idgruppo); 
		/**
		 * Recupera i parametri di filtro per stato e sistema
		 */
		int filterCount = 0;
		ArrayList filterNameList = new ArrayList();
		ArrayList filterValueList = new ArrayList();
		ArrayList searchNameList = new ArrayList();
		ArrayList searchValueList = new ArrayList();
		/**
		 * Scorre tutti i parametri di tipo filtro/ricerca
		 */
		while (request.getParameter("filter[" + filterCount + "][field]") != null) {
			/**
			 * Verifica se e' un filtro con selezione multivalore o una stringa di ricerca
			 */
			if (request.getParameter("filter[" + filterCount + "][data][type]").toString().equals("string")) {
				String searchValue = request.getParameter("filter[" + filterCount + "][data][value]");
				searchNameList.add(request.getParameter("filter[" + filterCount + "][field]").toString());
				searchValueList.add(searchValue);
			}
			else {
				filterNameList.add(request.getParameter("filter[" + filterCount + "][field]").toString());
				String filterValue = "";
				String[] values = request.getParameterValues("filter[" + filterCount + "][data][value]");
				if (values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						filterValue += values[i] + ",";
					}
					filterValueList.add(filterValue);
				}
			}
			filterCount++;
		}
		
		return getFilterCondition(con, filterNameList, filterValueList, filter, idutente, idgruppo, flusso) +
		       getSearchCondition(searchNameList, searchValueList);
	}
	
	public static String getFilterCondition(Connection con,
                ArrayList filterNameList, ArrayList filterValueList, String filter,
                String idUtente, String idGruppo, String idFlusso)
        {
		String sqlCondition = "";
		String[] conditions;
		
		conditions = filter.split(",", 0);
		for (int i = 0; i < conditions.length; i++ ) {
		    log.info(i+": "+conditions[i]);
			if (idUtente.length()!=0 && conditions[i].equalsIgnoreCase("Utente")) {
				sqlCondition += " AND IDUTENTE = " + idUtente + " ";
			}
			else if (idGruppo.length()!=0 && conditions[i].equalsIgnoreCase("Gruppo")) {
				sqlCondition += " AND IDGRUPPO = " + idGruppo + " ";
			}
			else if (conditions[i].equalsIgnoreCase("In lavorazione")) {
				/**
				 * Ricavo tutti gli stati non finali per un certo flusso
				 */
				String sqlSelect = 
				   "SELECT S.IDFASE " +
		                   "FROM   CP_FASE_T S " +
		                   "WHERE  S.INLAVORAZIONE <> 0 ";
				
			//	Connection con = getConn(jdbcDefect);
				try {
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sqlSelect);
					/**
					 * Costruisco la condizione SQL con la clausola IN
					 */
					sqlCondition += " AND IDFASE IN ('";
					boolean isFirst = true;
					while (rs.next()) {
						if (isFirst) {
							sqlCondition += rs.getString("IDFASE");
							isFirst = false;
						}
						else
							sqlCondition += "', '" + rs.getString("IDFASE");
					}
					sqlCondition += "') ";
					
					rs.close();
					st.close();
				} catch (SQLException e) {
					log.error("", e);
				} finally {
			    	try {
			    		if (con != null)
							con.close();
					} catch (SQLException e) {
						log.error("Chiusura connessione", e);
					}
				}
			}
		}
		
		for (int i = 0; i < filterNameList.size(); i++) {
			conditions = filterValueList.get(i).toString().split(",", 0);
			sqlCondition += " AND (";
			sqlCondition += filterNameList.get(i) + " = '" + conditions[0] + "' ";
			for (int j = 1; j < conditions.length; j++ ) {
				sqlCondition += "OR " + filterNameList.get(i) + " = '" + conditions[j] + "' ";
			}
			sqlCondition += ") ";
		}
		
		return sqlCondition;
	}
	public static String getSearchCondition(ArrayList searchNameList, ArrayList searchValueList) {
		String sqlCondition = "";
		String conditions;
        
		for (int i = 0; i < searchNameList.size(); i++) {
            //TODO intercettare qui le colonne 'particolari' per ora TIPO e BLC
            // if (searchNameList.geti).equals('BLC'){
            //   concatenare i vari valori OR LIKE 'Bug' OR LIKE 'bUg' OR LIKE 'Bag' ....
            // }else{
                        // TODO : check FILT_AUX in auto-lib.jsp
			conditions = (String)searchValueList.get(i);
			sqlCondition += " AND LOWER( FILT_AUX." + searchNameList.get(i) + ") LIKE '" + conditions.toLowerCase() + "' ";
            //}
		}
		
		return sqlCondition;
	}
	public static String BASE64encode(String input) {
	    String output = null;
	    if (input == null)
		return output;

	    try {
		output = (new sun.misc.BASE64Encoder()).encode(input.getBytes("UTF-8"));
	    }
	    catch (UnsupportedEncodingException e) {
		log.error(e.getMessage());
	    }
	    return output;
	}
	
	public static String BASE64decode(String input) {
	    String output = null;
	    if (input == null)
		return output;
	    
	    byte idByte[];
	    try {
		idByte = (new sun.misc.BASE64Decoder()).decodeBuffer(input);
		output = new String(idByte, "UTF-8");
	    }
	    catch (UnsupportedEncodingException e) {
		log.error(e.getMessage());
	    }
	    catch (IOException e) {
		log.error(e.getMessage());
	    }
	    return output;
	}

    public static String getJSONerror(String error, String details){
        error = error.replaceAll("\n","<br/>").replaceAll("\"","&#34;"); 
        details = details.replaceAll("\n","<br/>").replaceAll("\"","&#34;"); 

        String jsonString = null;
        jsonString = "{\"totalCount\":\"0\",\"topics\":["+
                        "{\"error\":\"" + error + "\"},"+
                        "{\"details\":\"" + details + "\"}"+
                    "]}";
        return jsonString;
    }
    
    public static String formatAge(float numOfDays) {
	String age = "";
	
	if (numOfDays < 1) {
	    // Sono ore
	    Integer hours = new Integer((int)(numOfDays * 24)); 
	    age = new String("h " + hours.toString());
	}
	else {
	    // Sono giorni
	    Integer days = new Integer((int)numOfDays);
	    age = new String("d " + days.toString());
	}
	
	return age;
    }

    public static String getJSONerror(String error, String details, String callback){
        error = error.replaceAll("\n","<br/>").replaceAll("\"","&#34;"); 
        details = details.replaceAll("\n","<br/>").replaceAll("\"","&#34;"); 

        String jsonString = null;
        jsonString = "{\"totalCount\":\"0\",\"topics\":["+
                        "{\"error\":\"" + error + "\"},"+
                        "{\"details\":\"" + details + "\"},"+
                        "{\"callback\":\"" + callback+ "\"}"+
                    "]}";
        return jsonString;
    }
    
    /**
     * Converte tag <STRONG> in <B> e <EM> in <I>
     * Internet explorer scrive <STRONG> e <EM> invece che <B> e <I>
     * Inoltre elimina il trailer finale che deriva dall'htmleditor
     * @param html
     * @return
     */
    public static String htmlTagConv(String html) {
	String newHtml = null;
	if (html != null) {
	    newHtml = html.replaceAll("<STRONG>","<B>").replaceAll("</STRONG>","</B>").
			   replaceAll("<EM>","<I>").replaceAll("</EM>", "</I>");
	    // Elimina inoltre il Trailer finale che deriva dall'htmleditor
	    // Caso Firefox
	    if (newHtml.toLowerCase().endsWith("<br>"))
		newHtml = newHtml.substring(0, newHtml.length() - 4);
	    // Caso Internet Explorer
	    else if (newHtml.toLowerCase().endsWith("<p>&nbsp;</p>"))
		newHtml = newHtml.substring(0, newHtml.length() - 13);		
	}
	return newHtml;
    }
    
    /**
     * Converte gli \n di una stringa in tag html
     */
    public static String blankToHtml(String value) {
	String html = null;
	if (value != null) {
	    html = value.replaceAll("\r\n\r\n", "\n").replaceAll("\n", "<br>");
	}
	return html;
    }
}







