package net.mineSQL.util;
import net.mineSQL.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import net.sf.json.JSONSerializer;

//import oracle.sql.CLOB;

import org.apache.log4j.Logger;



/**
 * @author acroci
 * 
 * Classe di utilit� in grado di creare un tabella dato un resultset e un titolo
 */
public  class MakeTableForQuery{

    private static final Logger log = Logger.getLogger(MakeTableForQuery.class);

	private static String jdbcDefect="";
	// MARK: VALHALLA_14 | la formattazione sottostante permette di ottenere il formato
	// di data corretto per il riempimento nel campo corrispondente.
	private static SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
	//private static SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	
	/*public static HashMap coerenza(Connection con, String idStatoPadre, String idStatoFiglio, String idFlusso){
     //   Connection con = null;
		try {
                 log.debug(" call coerenza");
		//con = getConn(jdbcDefect);
        return coerenza(con, idStatoPadre, idStatoFiglio, idFlusso);
		} finally {
	    	try {
	    		if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("Chiusura connessione", e);
			}
		}
	}*/
	
	public static HashMap coerenza(Connection con, String idStatoPadre, String idStatoFiglio, String idFlusso){
		HashMap hm = new HashMap();
		String sqlCoerenza="select c.nome CAMPO, FLAG " +
				"from   dm_controllodicoerenza_t d, dm_campidefect_t c where  d.idflusso = c.idflusso " +
				"and    d.idcampo = c.idcampo AND IDSTATO_PADRE="+idStatoPadre+" " +
				"AND IDSTATO_FIGLIO="+idStatoFiglio+" AND d.idflusso="+idFlusso;
		log.info(" coerenza: " + sqlCoerenza);
		try {
			Statement st = con.createStatement();
			ResultSet rsCoerenza= st.executeQuery(sqlCoerenza);
			while(rsCoerenza!=null && rsCoerenza.next()){
				hm.put(rsCoerenza.getString("CAMPO"), rsCoerenza.getString("FLAG"));
			}
			rsCoerenza.close();
			st.close();
		} catch (SQLException e) {
			log.error("", e);
		}
		return hm;
	}
	
	/*public static HashMap abilitazione(Connection con, String idStato, String idFlusso, boolean disableAll){
        //Connection con = null;
        try {
        //    con = getConn(jdbcDefect);
            return abilitazione(con, idStato, idFlusso, disableAll);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("Chiusura connessione", e);
			}
		}
	}*/
	
	public static HashMap abilitazione(Connection con, String idStato, String idFlusso, boolean disableAll){
		HashMap hm = new HashMap();
		String sqlCoerenza="select 'FLAG_'||c.nome CAMPO, decode(flag, 'M','false', 'N','true', 'O','mandatory') FLAG " +
				"from dm_abilitazionemodifica_t d, dm_campidefect_t c where  d.idflusso = c.idflusso and " +
				"d.idcampo = c.idcampo and idstato="+idStato+" and d.idflusso="+idFlusso;
		try {
			Statement st = con.createStatement();
			ResultSet rsAbilitazione= st.executeQuery(sqlCoerenza);
			while(rsAbilitazione.next()){
                if (disableAll){
				    hm.put(rsAbilitazione.getString("CAMPO"), "true");
                }else{
				    hm.put(rsAbilitazione.getString("CAMPO"), rsAbilitazione.getString("FLAG"));
                }
			}
			if (disableAll) {
				hm.put("DISABLEALL", "true");
			}
			rsAbilitazione.close();
		    log.info(" abilitazione disableAll: " + disableAll + " select: " + sqlCoerenza);
		    log.info(" abilitazione return hm: " + hm);
			st.close();
		} catch (SQLException e) {
			log.error("", e);
		}
		return hm;
	}
	
	public static String getInitStatus(Connection con,String idFlusso) {
		String initStatus = "";
		String sqlSelect = "SELECT IDFASE " +
				           "FROM   CP_FASE_T " +
				           "WHERE  ORDINE = 1 " +
				           "AND    IDFLUSSO = " + idFlusso + " " +
				           "ORDER BY IDFASE";
        log.info(" getInitStatus(idFlusso="+idFlusso+"): select: " + sqlSelect);

	//	Connection con = getConn(jdbcDefect);
		try {
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(sqlSelect);
			if (rs.next()) {
				/**
				 * Non e' uno stato finale
				 */
				initStatus = rs.getString("IDFASE");
			}
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
		return initStatus;
		
	}
	
	/**
	 * Stato iniziale, viene richiamato solo per DEFECT e TASK per gestire anche il caso QA/DEV
	 * @param idFlusso
	 * @param idGruppo
	 * @return
	 */
	public static HashMap getInitStatus(Connection con, String idFlusso, String idGruppo) {
		HashMap hm = new HashMap();

		String sqlSelect = "SELECT S.IDSTATO, S.NOME " + 
		   			       "FROM   CP_FASE_T S, CP_FASE_GRUPPO_T R, CP_GRUPPO_T G " +
		   			       "WHERE  S.IDFLUSSO = R.IDFLUSSO " +
        				   "AND    S.IDFASE  = R.IDFASE " +
        				   "AND    G.IDGRUPPO = " + idGruppo + " " +
        				   "AND    S.IDFLUSSO = " + idFlusso + " " +
        				   "ORDER BY S.IDFASE";

        log.info(" getInitStatus select: " + sqlSelect);

	//	Connection con = getConn(jdbcDefect);
		try {
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(sqlSelect);
			if (rs.next()) {
				/**
				 * Non e' uno stato finale
				 */
				hm.put("IDSTATO", rs.getString("IDSTATO"));
				hm.put("IDSTATOCOMBO", rs.getString("NOME"));
                log.info(" return stato: " + hm.get("IDSTATO")+" nome: "+hm.get("IDSTATOCOMBO"));
			}
			else {
				hm.put("IDSTATO", "");
				hm.put("IDSTATOCOMBO", "");
                log.info(" return stato: " + hm.get("IDSTATO")+" nome: "+hm.get("IDSTATOCOMBO"));
			}
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
		return hm;
		
	}
	
	public static HashMap checkFinalStatus(Connection con, String idStato, String idFlusso){
		HashMap hm = new HashMap();
		String sqlSelect = "select * " +
					       "from   dm_stato_stato_t " +
		                   "where  idflusso = " + idFlusso + " " +
		                   "and    idstato_padre = " + idStato;
        log.info(" sqlSelect: " + sqlSelect);
		//Connection con = getConn(jdbcDefect);
		try {
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(sqlSelect);
			if (rs.next()) {
				/**
				 * Non e' uno stato finale
				 */
				hm.put("STATO_FINALE", "false");
			}
			else {
				hm.put("STATO_FINALE", "true");
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			log.error("", e);
		} finally {
	  //  	try {
	  //  		if (con != null)
	//				con.close();
	//		} catch (SQLException e) {
	//			log.error("Chiusura connessione", e);
	//		}
		}
		return hm;
	}
	
	public static HashMap getCheckbox(String table, String condition, Connection con){
		HashMap hm = new HashMap();
		String sqlQuery = "select * " +
		                  "from   CP_" + table + "_T " +
		                  "where " + condition;

        log.info(" getCheckbox sqlQuery: " + sqlQuery);
		                  
		//Connection con = getConn(jdbcDefect);
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while(rs.next()){
				hm.put("" + rsMetaData.getColumnName(2) + rs.getInt(2), "1");
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			log.error("", e);
		} finally {
/**	    	try {
	    		if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("Chiusura connessione", e);
			}*********/
		}
        log.info(" return hm: " + hm);
		return hm;
	}
	
	public static Connection getConn(String jdbc){
        log.debug(" Connection getConn jdbc: " +jdbc);
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection("host","db","user","password");
		} catch (Exception e) {
			log.error("", e);
		}
		return conn;
	}
	
	/*public static String inputStreamAsString(CLOB clob){
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
	
	public static String dateToOracleString(Timestamp date) {
        String oracle_to_date = "";
		if (date != null){
            oracle_to_date = "to_date('" + dateToString(date) + "','DD-Mon-YYYY HH24:MI:SS')";
        }
        return oracle_to_date;
	}

	public static String dateToString(Timestamp date) {
        String to_date = "";
		if (date != null){
            to_date = fmt.format(date);
        }
        return to_date;
	}

	public static String getFilterCondition(Connection con, String filter, String idUtente, String idGruppo, String idFlusso) {
		String sqlCondition = "";
		String[] conditions;
		conditions = filter.split(",", 0);
		for (int i = 0; i < conditions.length; i++ ) {
			if (conditions[i].equalsIgnoreCase("Utente")) {
				sqlCondition += " AND IDUTENTE = " + idUtente + " ";
			}
			else if (conditions[i].equalsIgnoreCase("Gruppo")) {
				sqlCondition += " AND IDGRUPPO = " + idGruppo + " ";
			}
			else if (conditions[i].equalsIgnoreCase("In lavorazione")) {
				/**
				 * Ricavo tutti le fasi non finali per un certo flusso
				 */
				String sqlSelect = "SELECT S.IDFASE " +
				                   "FROM   CP_FASE_T S " +
				                   "WHERE  S.INLAVORAZIONE <> 0 ";
				
				//Connection con = getConn(jdbcDefect);
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
							sqlCondition += rs.getString("NOME");
							isFirst = false;
						}
						else
							sqlCondition += "', '" + rs.getString("NOME");
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
		return sqlCondition;
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
	
	/**
	 * Verifica se un gruppo appartiene a QA/DEV o e' Administrator
	 */
	public static String checkQaDevUser(Connection con, String idGruppo){
		HashMap hm = new HashMap();
		String sqlSelect = "select * " +
                                   "from   CP_GRUPPO_T " +
                                   "where  IDGRUPPO = " + idGruppo + " " +
                                   "and    IDREPARTO in (" + Constants.GRUPPO_ADMIN + ", " +
                                                         Constants.REPARTO_QA + ", " + Constants.REPARTO_DEV +")";
		//Connection con = getConn(jdbcDefect);
		try {
			Statement st = con.createStatement();
			ResultSet rs= st.executeQuery(sqlSelect);
			if (rs.next()) {
				/**
				 * Non e' uno stato finale
				 */
				hm.put("IS_QA_DEV", "true");
			}
			else {
				hm.put("IS_QA_DEV", "false");
			}
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
		return JSONSerializer.toJSON(hm).toString();
	}

	public static String getPagingQuery(String tableName, String searchCondition, String filterCondition, String sorting, String direction, int start, int limit){ 
		String queryString = "select * " +
						       "from (select a.*, rownum rnum " +
						             "from (select * " +
						                   "from " + tableName + " " +
						                   "where 1 = 1 " +
                                           searchCondition +
                                           filterCondition +
                                           " order by " + sorting +" "+direction+ ") a " +
						             "where  rownum<=" + (start + limit) + ") " +
						       "where rnum > " + start;
		log.info("SQL per elenco di " + tableName + ": " + queryString);

        return queryString;
    }

}
