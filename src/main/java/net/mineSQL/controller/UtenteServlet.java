package net.mineSQL.controller;

import com.mchange.v2.c3p0.impl.C3P0Defaults;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import net.mineSQL.connection.ConnectionManager;
import net.mineSQL.util.Utilita;
/**
 * Servlet che gestisce le richieste dalle pagine HTML/JSP.
 * Utilizza il pattern J2EE "Front Controller".
 * @author dluciano
 */
public class UtenteServlet extends HttpServlet {	

	String jdbcUser="utenze";
	String jdbcDefect="mineSQL";

    HttpServletResponse resp = null;
    HttpServletRequest req = null;    

	
    private static final Logger log = Logger.getLogger(UtenteServlet.class);

    private void forward(String url) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        String message = "ok";

        try{
            // Prendo il messaggio d'errore e lo loggo
            if (req.getSession().getAttribute("content")!=null)
                message = req.getSession().getAttribute("content").toString();
            log.info(" -------------------------------------------------------");
            log.info(" UtenteServlet FORWARD to page : " + url + " with error: " + message );
            log.info(" -------------------------------------------------------");

            dispatcher = getServletContext().getRequestDispatcher(url); 
            dispatcher.forward(req, resp);
        }
        catch(IllegalStateException ise){
            // lunica cosa che vedr sarla stack 
            // trace delleccezione
            log.error("",ise);
        } 
    }	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = "";
		String password = "";
		String splitUserName = "";

        resp = response;
        req = request;
        
        log.info(" --------------->   MineSQL  HTTP Request <-----------------");
		if(request.getSession().getAttribute(Utilita.USERNAME)==null){
			if (request.getParameter(Utilita.USERNAME) != null) {
			        username = request.getParameter(Utilita.USERNAME);
			        password = request.getParameter(Utilita.PASSWORD);
			        if (username.indexOf("\\") != -1) {
			                String[] usernameToSplit = username.split("\\\\");
			                splitUserName = usernameToSplit[1];
			                username = splitUserName;
			        }
			}
		}else{
			username = (String)request.getSession().getAttribute(Utilita.USERNAME);
		}
		//String sqlInsert="";
		//String sqlGrant="";
        String result = "";
		
		//log.info("Post UtenteServlet: apro connessioni.");
	
        if (! username.isEmpty() && password.equals("12345")){
		    request.getSession().setAttribute(Utilita.USERNAME, username);
            request.getSession().setAttribute(username, Utilita.ISLOGGEDIN);
            log.info("User: " + username + " is authenticated");
            result = "{\"success\":true,\"valid\":true,\"reason\":\"Aggiornamento effettuato.\"}";
        }else{
            log.info("User: " + username + " is NOT authenticated");
            result = "{\"success\":false,\"valid\":true,\"reason\":\"Errore aggiornamento.\"}";
        }
    
        
		PrintWriter out = response.getWriter();
        out.println(result);

    }
	/**
	 * Risponde a richieste HTTP di tipo POST.
	 * @throws  
	 */
	public void OLDdoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = "";
		String password = "";
		String splitUserName = "";

        resp = response;
        req = request;
        
        log.info(" --------------->   MineSQL  HTTP Request <-----------------");
		if(request.getSession().getAttribute("username")==null){
			if (request.getParameter("username") != null) {
			        username = request.getParameter("username");
			        password = request.getParameter("password");
			        if (username.indexOf("\\") != -1) {
			                String[] usernameToSplit = username.split("\\\\");
			                splitUserName = usernameToSplit[1];
			                username = splitUserName;
			        }
			}
		}else{
			username=(String)request.getSession().getAttribute("username");
		}
		//String sqlInsert="";
		//String sqlGrant="";
		
		//log.info("Post UtenteServlet: apro connessioni.");
	
        if (! username.isEmpty() && password.equals("12345")){
		    request.getSession().setAttribute(Utilita.USERNAME, username);
            request.getSession().setAttribute(username, Utilita.ISLOGGEDIN);
            forward("/index.jsp");
        }
        
		log.info("Login username: " + username); 
		Connection conUte = null;// = getConn(jdbcUser);
		Connection conDef = null;// = getConn(jdbcDefect);
		Statement stUte;
		Statement stDef;
	
		/*
		---da scommentare poi ----
		Connection conDef = MakeTableForQuery.getConn(jdbcDefect);
		Statement st;
		try {
			st = con.createStatement();
			ResultSet rsUtente= st.executeQuery("SELECT * FROM DM_UTENTE_T WHERE USERNAME='"+username+"'");
			if(rsUtente.next()){
				//log.info("crea utente sessione");
				Utente ut = new Utente(rsUtente.getInt("IDUtente"), rsUtente.getString("username"), rsUtente.getString("Descrizione"), rsUtente.getInt("IDGruppo"), rsUtente.getInt("IDRuolo")); 
				request.getSession().setAttribute("utente", ut);				
			} 
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		*/
		
		//end
		if(request.getParameter("onchange")==null || request.getParameter("onchange").equalsIgnoreCase("null")){
			String operazione=request.getParameter("operazione");
			String sqlInsert="";
			String sqlGrant="";
			int idUte=0;
			int idApp=0;
            //log.debug(" operazione: " + operazione);
			if(operazione!=null){
				try {
					stUte = conUte.createStatement();
					stDef = conDef.createStatement();
					ResultSet rsApp = stUte.executeQuery("SELECT ID_APP FROM UT_APPLICATIVI_T WHERE NOME ='DefectManagement'");
					if(rsApp.next()){
						idApp = rsApp.getInt("id_app");
					}
					//log.info("Post UtenteServlet: Operazione = "+operazione);
					/*
                    * Questo ramo viene richiamato quando si fa un cambio gruppo con la
                    * comboBox del main
                    */  
					if(operazione.equalsIgnoreCase("CHOICE")) {
						String redirect = "";
						String idGruppo = request.getParameter("idgruppo");						
						ResultSet rsDef;
						/**
						 * Vado a pescare IDUTENTE e IDGRUPPO sul DB di DEFECTMANAGEMENT
						 */
/*TODO*/
						String sql="SELECT IDUTENTE, G.IDGRUPPO IDGRUPPO, G.NOME NOME, " +
                                        "REP.IDREPARTO, REP.NOME NOME_REPARTO, " +
                                        "RU.IDRUOLO IDRUOLO, RU.NOME NOME_RUOLO " +
					    		   "FROM DM_UTENTE_T U, DM_GRUPPO_T G, DM_REPARTO_T REP, " +
                                        "DM_RUOLO_T RU " +
					    		   "WHERE  U.IDGRUPPO = G.IDGRUPPO " +
					    		   "AND    U.USERNAME = '" + username + "' " +
					    		   "AND    REP.IDREPARTO = G.IDREPARTO " +
					    		   "AND    RU.IDRUOLO = U.IDRUOLO " +
					    		   "AND    G.IDGRUPPO = " + idGruppo;
                        log.info(" select: " + sql);
						stDef = conDef.createStatement();
						rsDef = stDef.executeQuery(sql);
						if (rsDef.next()) {							
							request.getSession().setAttribute("IDUTENTE", ""+rsDef.getInt("IDUTENTE"));
							request.getSession().setAttribute("IDGRUPPO", ""+rsDef.getInt("IDGRUPPO"));
							request.getSession().setAttribute("IDRUOLO", ""+rsDef.getInt("IDRUOLO"));
							request.getSession().setAttribute("IDREPARTO", ""+rsDef.getInt("IDREPARTO"));
							request.getSession().setAttribute("NOME_REPARTO", ""+rsDef.getString("NOME_REPARTO"));
							request.getSession().setAttribute("NOME_RUOLO", ""+rsDef.getString("NOME_RUOLO"));
							request.getSession().setAttribute("groupname", ""+rsDef.getString("NOME"));

                            //log.debug("  MARK 1 update user session IDGRUPPO:  " + request.getSession().getAttribute("IDGRUPPO").toString() );
							//forward("/main.jsp?username='" + username + "'&groupname='" + rsDef.getString("NOME") + "'");
							redirect = "/main.jsp";
						}
						else {
							request.getSession().setAttribute("content", "Utenza non presente su DefectManagement, rivolgersi all'amministratore"); 
							redirect = "/error.jsp";
						}
						rsDef.close();
						stDef.close();
						conDef.close();
                        //log.debug(" forward(\"" + redirect +"\")" );
						//forward(redirect);
						forward(redirect);

					} else if(operazione.equalsIgnoreCase("TOABLE")) {
						////log.info("Entro in abilitazione");
						String sql="SELECT * FROM UT_UTENTE_T WHERE USERNAME='"+request.getSession().getAttribute("username")+"'";
						ResultSet rsUser=stUte.executeQuery(sql);
						////log.info("1 abil");
						if(rsUser.next()){
							idUte = rsUser.getInt("id");
							String mailUte = rsUser.getString("email");
							if (rsUser.wasNull()) mailUte = "";
							//ResultSet rsApp = stUte.executeQuery("SELECT ID_APP FROM UT_APPLICATIVI_T WHERE NOME ='DefectManagement'");
							if(idApp==2){
								////log.info("ok id app");
								sqlGrant="insert into UT_UTEAPP_T (ID_UTE, ID_APP, GRANTS) VALUES ("+idUte+","+idApp+", 0)";
								////log.info("sqlGrant: "+sqlGrant);
								stUte.execute(sqlGrant);
								rsApp.close();
								/**
								 * Inserisco l'utente nel DB di DefectManagement se non e' presente
								 */
								sql="SELECT * FROM DM_UTENTE_T WHERE USERNAME='"+request.getSession().getAttribute("username")+"'";
								////log.info("inserisco su altro db");
								//log.info(sql);
								rsUser = stDef.executeQuery(sql);
								if (!rsUser.next()) {
									ResultSet rsNext = stDef.executeQuery("SELECT DM_UTENTE_SEQ.nextval FROM DUAL");
									rsNext.next();
									sqlInsert="INSERT INTO DM_UTENTE_T (IDUTENTE, USERNAME, IDGRUPPO, IDRUOLO, ORDINE) VALUES(" +
										  rsNext.getInt("NEXTVAL")+", "+
							              "'"+request.getSession().getAttribute("username")+"', " +
							              request.getParameter("idgruppo")+", 1, 0)";
									//log.info(sqlInsert);
									rsNext.close();
									stDef.execute(sqlInsert);
									/**
									 * Inserisco anche la mailbox
									 */
									if (mailUte.length() > 0) {
										rsNext = stDef.executeQuery("SELECT DM_MAILBOX_SEQ.nextval FROM DUAL");
										rsNext.next();
										sqlInsert="INSERT INTO DM_MAILBOX_T (IDMAILBOX, NOME, DESCRIZIONE) VALUES (" +
											  rsNext.getInt("NEXTVAL")+", "+
								              "'" + mailUte + "', " +
								              "null) ";
										//log.info(sqlInsert);
										rsNext.close();
										stDef.execute(sqlInsert);
									}
								}
								stDef.close();
								conDef.close();
							}
							rsApp.close();

							request.getSession().setAttribute("mail", "ok");
							forward("/utente.jsp");
						}
						rsUser.close();

					} else if(operazione.equalsIgnoreCase("EDIT")) {
						////log.info("Entro in edit");
						String sql="SELECT * FROM UT_UTENTE_T WHERE USERNAME='"+request.getSession().getAttribute("username")+"'";
						////log.info("1");
						ResultSet rsUser=stUte.executeQuery(sql);
						////log.info(sql);
						if(rsUser.next()){
							String userMail = rsUser.getString("email");
							////log.info("3");
							////log.info("Entro in update");
							String sqlUpdate="UPDATE UT_UTENTE_T " +
							"set NOME='"+request.getParameter("nome")+"', " +
							"COGNOME = '"+request.getParameter("cognome")+"', " +
							"EMAIL = '"+request.getParameter("email")+"' " +
							"WHERE USERNAME='"+request.getSession().getAttribute("username")+"'";	
							//log.info("sqlUpdate: " +sqlUpdate);
							stUte.execute(sqlUpdate);
							//ResultSet rsApp = stUte.executeQuery("SELECT ID_APP FROM UT_APPLICATIVI_T WHERE NOME ='DefectManagement'");
							//log.info("grant: "+request.getParameter("grant"));
							
							sqlUpdate="UPDATE DM_UTENTE_T " +
									"set IDGRUPPO = " + request.getParameter("idgruppo") + " " +
									"WHERE USERNAME = '" + request.getSession().getAttribute("username") + "'";	
							//log.info("sqlUpdate: " +sqlUpdate);
							stDef.execute(sqlUpdate);
							
							if (!userMail.equals(request.getParameter("email"))) {
								sqlUpdate="UPDATE DM_MAILBOX_T " +
										"set NOME = '" + request.getParameter("mail") + "' " +
										"WHERE NOME = '" + userMail + "'";	
								//log.info("sqlUpdate: " +sqlUpdate);
								stDef.execute(sqlUpdate);
							}
							
							if(rsApp.next()){
								String grants=request.getParameter("grant")!=null?request.getParameter("grant"):"0";
								sqlGrant="UPDATE UT_UTEAPP_T SET GRANTS="+grants+" WHERE " +
										"ID_UTE=(SELECT ID FROM UT_UTENTE_T WHERE USERNAME='"+username+"') " +
										"and ID_APP='"+idApp+"')";
								//log.info("sqlGrant: "+sqlGrant);
								stUte.execute(sqlGrant);
								rsApp.close();
							}
							rsApp.close(); 
							forward("/index.jsp");
							
						}else{
							ResultSet rsNext=stUte.executeQuery("SELECT UT_UTENTE_SEQ.nextval FROM DUAL");
							rsNext.next();
							int next=rsNext.getInt("NEXTVAL");
							sqlInsert="INSERT INTO UT_UTENTE_T (ID,USERNAME,NOME,COGNOME,EMAIL) VALUES(" +
								      next+", "+
								      "'"+request.getSession().getAttribute("username")+"', " +
								      "'"+request.getParameter("nome")+"', " +
								      "'"+request.getParameter("cognome")+"', " +
								      "'"+request.getParameter("email")+"')";						
							stUte.execute(sqlInsert);
							//log.info(sqlInsert);
							//ResultSet rsApp = stUte.executeQuery("SELECT ID_APP FROM UT_APPLICATIVI_T WHERE NOME ='DefectManagement'");
							if(idApp==2){
								sqlGrant="insert into UT_UTEAPP_T (ID_UTE, ID_APP, GRANTS) VALUES ("+next+","+idApp+", 0)";
								stUte.execute(sqlGrant);
								rsApp.close();
							}
							ResultSet rsUserNew = stDef.executeQuery("SELECT * FROM DM_UTENTE_T WHERE USERNAME='"+request.getSession().getAttribute("username")+"'");
							if (!rsUserNew.next()) {
								
								rsNext = stDef.executeQuery("SELECT DM_UTENTE_SEQ.nextval FROM DUAL");
								rsNext.next();
								next = rsNext.getInt("NEXTVAL");
								sqlInsert="INSERT INTO DM_UTENTE_T (IDUTENTE, USERNAME, IDGRUPPO, IDRUOLO, ORDINE) VALUES(" +
								      	next+", "+
								      	"'"+request.getSession().getAttribute("username")+"', " +
								      	request.getParameter("idgruppo")+", 1, 0)";
								//log.info(sqlInsert);
								stDef.execute(sqlInsert);

								if (request.getParameter("email") != null && request.getParameter("email").length() > 0 ) {
									rsNext = stDef.executeQuery("SELECT DM_MAILBOX_SEQ.nextval FROM DUAL");
									rsNext.next();
									next = rsNext.getInt("NEXTVAL");
									sqlInsert="INSERT INTO DM_MAILBOX_T (IDMAILBOX, NOME, DESCRIZIONE) VALUES(" +
											next+", "+
											"'"+ request.getParameter("email") +"', " +
											"null) ";
									//log.info(sqlInsert);
									stDef.execute(sqlInsert);
								}
							}
							
							rsApp.close();
							rsNext.close();
							rsUserNew.close();
							stUte.close();
							conUte.close();
							stDef.close();
							conDef.close();
							request.getSession().setAttribute("mail", "ok");
							forward("/utente.jsp");
						}

					} else if(operazione.equalsIgnoreCase("GRANT")){
						stUte.execute("UPDATE UT_UTEAPP_T set GRANTS="+request.getParameter("grant")+
						" WHERE ID_UTE="+request.getParameter("utenti") +" AND ID_APP="+idApp);						
						forward("/index.jsp");
					} else if(operazione.equalsIgnoreCase("DEL")){
						ResultSet rsID_APP = stUte.executeQuery("SELECT ID_APP FROM UT_APPLICATIVI_T WHERE NOME='DefectManagement'");
						if(rsID_APP.next()){
							sqlInsert="DELETE UT_UTENTE_T WHERE ID="+request.getParameter("utenti");
							sqlGrant="DELETE UT_UTEAPP_T WHERE ID_UTE="+request.getParameter("utenti")+" and ID_APP="+rsID_APP.getInt("ID_APP");
							stUte.execute(sqlInsert);
							stUte.execute(sqlGrant);
						}
						rsID_APP.close();
						forward("/index.jsp");
					}
					stUte.close();
					conUte.close();
				} catch (SQLException e) {
					request.getSession().setAttribute("content", e.getMessage()+ " "+sqlInsert+" A "+sqlGrant+ " A");
					////log.info("Vado a error1");
					forward("/error.jsp");
					e.printStackTrace();
				}
			}else{
				try {
					// Ramo if di scelta gruppo
					ResultSet rsGrant;
					stUte = conUte.createStatement();
					String sql="SELECT ua.GRANTS as GRANTS FROM UT_UTEAPP_T ua, " +
					"UT_UTENTE_T u, UT_APPLICATIVI_T ap WHERE ua.ID_UTE=u.ID and ua.ID_APP=ap.ID_APP " +
					"and u.USERNAME='"+username+"' and ap.NOME='DefectManagement'";
					rsGrant=stUte.executeQuery(sql);
					if(rsGrant.next()){
						request.getSession().setAttribute("GRANTS", rsGrant.getString("GRANTS"));
						switch (rsGrant.getInt("GRANTS")) {
						case 0:
							request.getSession().setAttribute("content", "Utenza da convalidare"); 
							//log.info("Vado a error");
							forward("/error.jsp");
							break;
						default:						
							ResultSet rsDef;
							String redirect = "";
							// Vado a pescare IDUTENTE e IDGRUPPO sul DB di DEFECTMANAGEMENT
						    sql="SELECT IDUTENTE, G.IDGRUPPO IDGRUPPO, G.NOME NOME, RE.IDREPARTO, RE.NOME NOME_REPARTO," +
                                        " RU.IDRUOLO IDRUOLO, RU.NOME NOME_RUOLO, U.ORDINE  " +
                                "FROM   DM_UTENTE_T U, DM_GRUPPO_T G, DM_REPARTO_T RE, DM_RUOLO_T RU " +
						        "WHERE  U.IDGRUPPO = G.IDGRUPPO " +
                                "AND    U.USERNAME = '" + username + "' " + 
                                "AND    RE.IDREPARTO = G.IDREPARTO " +
                                "AND    RU.IDRUOLO = U.IDRUOLO " +
						    	"ORDER BY U.ORDINE ASC, RU.IDRUOLO ASC, G.IDGRUPPO ASC";

                            log.info(" main select: " + sql);
							stDef = conDef.createStatement();
							rsDef = stDef.executeQuery(sql);
							if (rsDef.next()) {
								request.getSession().setAttribute("IDUTENTE", ""+rsDef.getInt("IDUTENTE"));
								request.getSession().setAttribute("IDGRUPPO", ""+rsDef.getInt("IDGRUPPO"));
								request.getSession().setAttribute("IDRUOLO", ""+rsDef.getInt("IDRUOLO"));
							    request.getSession().setAttribute("IDREPARTO", ""+rsDef.getInt("IDREPARTO"));
							    request.getSession().setAttribute("NOME_REPARTO", ""+rsDef.getString("NOME_REPARTO"));
							    request.getSession().setAttribute("NOME_RUOLO", ""+rsDef.getString("NOME_RUOLO"));
								request.getSession().setAttribute("groupname", ""+rsDef.getString("NOME"));
                                //log.debug("  MARK 3 update user session IDGRUPPO:  " + request.getSession().getAttribute("IDGRUPPO").toString() );
								//forward("/main.jsp?username='" + username + "'&groupname='" + rsDef.getString("NOME") + "'");
								redirect = "/main.jsp";
								
								/** if (!rsDef.next()) {
									// L'utente e' assegnato ad un solo gruppo quindi vado diretto al main
									redirect = "/main.jsp";
								}else {
									//  L'utente e' assegnato a piu' gruppi quindi dovra' sceglierne uno
									redirect = "/gruppo.jsp";
								} **/
							}
							else {
								request.getSession().setAttribute("content", "Utenza non presente su DefectManagement, rivolgersi all'amministratore");								
								redirect = "/error.jsp";
							}
							rsDef.close();
							stDef.close();
							conDef.close();
							forward(redirect);
							break;
						}
					}else{
						forward("/utente.jsp");
					}
					rsGrant.close();
					stUte.close();
					conUte.close();
				} catch (SQLException e) {
					request.getSession().setAttribute("content",e.getMessage()); 
					forward("/error.jsp");
				}
			}
		}else{
            //log.debug(" MARK 4 ");
			request.getSession().setAttribute("onchange", request.getParameter("onchange"));
			forward("utente.jsp");
		}
		
        //log.debug(" MARK 7 ");
		//log.info("Post UtenteServlet: chiudo connessioni.");
		try {
			if (conUte != null)
				conUte.close();
			if (conDef != null)
				conDef.close();
		}
		catch (SQLException ex) {
            log.error("",ex);
			//log.info("Post UtenteServlet: fallita chiusura connessioni.");
		}
	}

	/**
	 * Le chiamate di tipo GET vengono reindirizzate al POST.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		doPost(request, response);
	}

	public static String getCampiUser(String user, int grant){
		StringBuffer sbCampi = new StringBuffer();
		Connection con = getConn("utenze");
		boolean Esiste=false;
		String nome="";
		String cognome="";
		String email="";
		Statement st1;
		Statement st;
		String sql1="SELECT * from UT_UTENTE_T where username='"+user+"'";
		String sql="SELECT u.*, ua.GRANTS as GRANTS FROM UT_UTEAPP_T ua, " +
		"UT_UTENTE_T u, UT_APPLICATIVI_T ap WHERE ua.ID_UTE=u.ID and ua.ID_APP=ap.ID_APP " +
		"and u.USERNAME='"+user+"' and ap.NOME='DefectManagement'";
		try {
			st1 = con.createStatement();
			ResultSet rsUtExist = st1.executeQuery(sql1);
			if(rsUtExist.next()) {
				Esiste = true;
				nome=rsUtExist.getString("nome")!=null?rsUtExist.getString("nome"):"";
				cognome=rsUtExist.getString("cognome")!=null?rsUtExist.getString("cognome"):"";
				email=rsUtExist.getString("email")!=null?rsUtExist.getString("email"):"";
			}
			rsUtExist.close();
			st1.close();
			if (Esiste){
				st = con.createStatement();
				ResultSet rsCampi=st.executeQuery(sql);
				if(rsCampi.next()){
					if(grant>1){
						String sel1="";
						String sel2="";
						if(rsCampi.getInt("GRANTS")>1){
							sel2="selected";
						}else{
							sel1="selected";
						}
						sbCampi.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
						sbCampi.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
						sbCampi.append("<tr>");
						sbCampi.append("<td>Tipo di utenza</td>");
						sbCampi.append("<td><select name=\"grant\">");
						sbCampi.append("<option "+sel1+" value=\"1\">User</option>");
						sbCampi.append("<option "+sel2+" value=\"2\">Power User</option>");
						sbCampi.append("</select></td>");
						sbCampi.append("<td><input type=\"button\" onclick=\"javascript:modifica('GRANT');\" value=\"Autorizza\"></td></tr>");
					}
					sbCampi.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
					sbCampi.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
					sbCampi.append("<tr><td>Nome</td>");
					sbCampi.append("<td><input type=\"text\" name=\"nome\" value=\""+nome+"\" ></td>");
					sbCampi.append("</tr><tr><td>Cognome</td>");
					sbCampi.append("<td><input type=\"text\" name=\"cognome\" value=\""+cognome+"\" ></td></td><td><input type=\"button\" onclick=\"javascript:modifica('EDIT');\" value=\"Modifica i dati anagrafici\"></td>");
					sbCampi.append("</tr><tr><td>Email</td>");
					sbCampi.append("<td><input type=\"text\" name=\"email\" value=\""+email+"\" ></tr>");
				}else{
					sbCampi.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
					sbCampi.append("<tr><td>Nome</td>");
					sbCampi.append("<td colspan=\"2\"><input type=\"text\" name=\"nome\"value=\""+nome+"\" ></td>");
					sbCampi.append("</tr><tr><td>Cognome</td>");
					sbCampi.append("<td colspan=\"2\"><input type=\"text\" name=\"cognome\" value=\""+cognome+"\" ></td></tr>");
					sbCampi.append("<tr><td>Email</td>");
					sbCampi.append("<td><input type=\"text\" name=\"email\" value=\""+email+"\" ></td><td><input type=\"button\" onclick=\"javascript:modifica('TOABLE');\" value=\"Richiedi Abilitazione\"></td></tr>");
				} 
				rsCampi.close();
				st.close();
			}else {
				sbCampi.append("<tr><td colspan=\"2\">&nbsp;</td></tr>");
				sbCampi.append("<tr><td>Nome</td>");
				sbCampi.append("<td colspan=\"2\"><input type=\"text\" name=\"nome\" value=\"\" ></td>");
				sbCampi.append("</tr><tr><td>Cognome</td>");
				sbCampi.append("<td colspan=\"2\"><input type=\"text\" name=\"cognome\" value=\"\" ></td></tr>");
				sbCampi.append("<tr><td>Email</td>");
				sbCampi.append("<td><input type=\"text\" name=\"email\" value=\"\" ></td><td><input type=\"button\" onclick=\"javascript:modifica('EDIT');\" value=\"Inserisci i tuoi dati anagrafici\"></td></tr>");
			}
		} catch (SQLException e) {
			sbCampi.append(e.getMessage()+" SQL "+ sql);
			e.printStackTrace();
		} finally {
	    	try {
	    		if (con != null)
	    			con.close();
			} catch (SQLException e) {
                log.error("",e);
			}
		}
		return sbCampi.toString();
	}
	
	public static String getComboUser(String user, int grant){
		Connection con = getConn("utenze");
		Statement st;
		String disabled="";
		if(grant<2){
			 disabled="disabled";
		}
		StringBuffer sbCombo = new StringBuffer();
		try {
			st = con.createStatement();
			sbCombo.append("<tr><td>Username</td>");
			sbCombo.append("<td><SELECT name=\"utenti\" onChange=\"javascript:ricarica();\" "+disabled+" >");
			if(disabled.equalsIgnoreCase("")){
				ResultSet rsUser= st.executeQuery("SELECT ID, COGNOME, NOME, USERNAME FROM UT_UTENTE_T");
				while(rsUser.next()){
					if(rsUser.getString("USERNAME").equalsIgnoreCase(user))
						sbCombo.append("<option selected value=\""+rsUser.getString("ID")+"\" >"+rsUser.getString("USERNAME")+"</option>");
					else
						sbCombo.append("<option value=\""+rsUser.getString("ID")+"\" >"+rsUser.getString("USERNAME")+"</option>");
				}
			}else{
				String sql="SELECT * FROM UT_UTENTE_T WHERE USERNAME='"+user+"'";
				
				ResultSet rsUser= st.executeQuery(sql);
				if(rsUser.next()){
					sbCombo.append("<option selected value=\""+rsUser.getString("ID")+"\" >"+rsUser.getString("USERNAME")+"</option>");
				}else{
					sbCombo.append("<option selected value=\"\" >"+user+"</option>");
				}
			}
			if(grant<2){
				sbCombo.append("</SELECT></td></tr>");
			}else{
				sbCombo.append("</SELECT></td><td><input type=\"button\" onclick=\"javascript:modifica('DEL');\" value=\"Cancella\"></td></tr>");				
			}
			st.close();
		} catch (SQLException e) {
			sbCombo.append(e.getMessage());
			e.printStackTrace();
		} finally {
	    	try {
	    		if (con != null)
	    			con.close();
			} catch (SQLException e) {
                log.error("",e);
			}
		}
		return sbCombo.toString();
	}
	
	public static String getComboGroup(String user, int grant){
		Connection con = getConn("mineSQL");
		Statement st;
		StringBuffer sbCombo = new StringBuffer();
		String idGruppo = "";
		try {
			st = con.createStatement();
			sbCombo.append("<tr><td>Gruppo</td>");
			sbCombo.append("<td><SELECT name=\"idgruppo\" >");
			
			ResultSet rsUser = st.executeQuery("SELECT G.IDGRUPPO IDGRUPPO FROM DM_UTENTE_T U, DM_GRUPPO_T G WHERE U.IDGRUPPO = G.IDGRUPPO AND U.USERNAME = '" + user + "'");
			if (rsUser.next()) {
				idGruppo = rsUser.getString("IDGRUPPO");
				rsUser.close();
			}

			ResultSet rsGroup= st.executeQuery("SELECT IDGRUPPO, NOME FROM DM_GRUPPO_T WHERE IDGRUPPO > 0");
			while(rsGroup.next()){
				if(rsGroup.getString("IDGRUPPO").equalsIgnoreCase(idGruppo))
					sbCombo.append("<option selected value=\""+rsGroup.getString("IDGRUPPO")+"\" >"+rsGroup.getString("NOME")+"</option>");
				else
					sbCombo.append("<option value=\""+rsGroup.getString("IDGRUPPO")+"\" >"+rsGroup.getString("NOME")+"</option>");
			}

			sbCombo.append("</SELECT></td></tr>");
			rsGroup.close();
			st.close();
		} catch (SQLException e) {
			sbCombo.append(e.getMessage());
			e.printStackTrace();
		} finally {
	    	try {
	    		if (con != null)
	    			con.close();
			} catch (SQLException e) {
                log.error("",e);
			}
		}
		return sbCombo.toString();
	}
	
	public static String getUserGroupCombo(String user, String currentGroup){
		Connection con = getConn("mineSQL");
		Statement st;
		StringBuffer sbCombo = new StringBuffer();
		try {
			st = con.createStatement();
			//sbCombo.append("<tr><td>Gruppo</td>");
			
			ResultSet rsGroup = st.executeQuery("SELECT IDUTENTE, G.IDGRUPPO IDGRUPPO, G.NOME NOME " +
			    					           "FROM   DM_UTENTE_T U, DM_GRUPPO_T G " +
			    					           "WHERE  U.IDGRUPPO = G.IDGRUPPO " +
			    					           "AND    U.USERNAME = '" + user + "' " +
			    					           "ORDER BY G.IDGRUPPO DESC");
			Map rows = new HashMap();
			String  mioGruppo = "";
			while(rsGroup.next()){
				rows.put(rsGroup.getString("IDGRUPPO"), rsGroup.getString("NOME"));
				mioGruppo = rsGroup.getString("NOME");
			}
			if (rows.size() == 1 )
			{
				return "<span id=\"change-group\">"+mioGruppo+"</span>";
			}
			// Altrimenti combobox
			sbCombo.append("<SELECT id=\"change-group\" name=\"idgruppo\" >");
			for (Iterator it = rows.entrySet().iterator(); it.hasNext();) {
				Map.Entry pairs = (Map.Entry)it.next();
		        
		        if(pairs.getValue().toString().equalsIgnoreCase(currentGroup))
					sbCombo.append("<option selected value=\""+pairs.getKey().toString()+"\" >"+pairs.getValue().toString()+"</option>");
				else
					sbCombo.append("<option value=\""+pairs.getKey().toString()+"\" >"+pairs.getValue().toString()+"</option>");
			
			}
			sbCombo.append("</SELECT>");
			
			rsGroup.close();
			st.close();
		} catch (SQLException e) {
			sbCombo.append(e.getMessage());
			e.printStackTrace();
		} finally {
	    	try {
	    		if (con != null)
	    			con.close();
			} catch (SQLException e) {
                log.error("",e);
			}
		}
		return sbCombo.toString();
	}	 

	public static Connection getConn(String jdbc){
        log.debug(" Connection getConn jdbc: " +jdbc);
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection("host","db","user","password","dbtype");
		} catch (Exception e) {
			log.error("", e);
		}
		return conn;
	}

	public static String getUserGroupJson(String user, String currentGroup){
		Connection con = getConn("mineSQL");
		Statement st;
        JSONArray values  = new JSONArray();

		try {
			st = con.createStatement();
			ResultSet rsGroup = st.executeQuery("SELECT IDUTENTE, G.IDGRUPPO IDGRUPPO, G.NOME NOME " +
			    					           "FROM   DM_UTENTE_T U, DM_GRUPPO_T G " +
			    					           "WHERE  U.IDGRUPPO = G.IDGRUPPO " +
			    					           "AND    U.USERNAME = '" + user + "' " +
			    					           "ORDER BY G.IDGRUPPO DESC");
			Map rows = new HashMap();
			String  mioGruppo = "";
			while(rsGroup.next()){
				rows.put(rsGroup.getString("IDGRUPPO"), rsGroup.getString("NOME"));
				mioGruppo = rsGroup.getString("NOME");
			}
            
			for (Iterator it = rows.entrySet().iterator(); it.hasNext();) {
				Map.Entry pairs = (Map.Entry)it.next();
                JSONArray coppia = new JSONArray();
                
                coppia.add(pairs.getKey().toString());
                coppia.add(pairs.getValue().toString());
    
                values.add(coppia);
			}
            
			rsGroup.close();
			st.close();
		} catch (SQLException e) {
            log.error("",e);
		} finally {
	    	try {
	    		if (con != null)
	    			con.close();
			} catch (SQLException e) {
                log.error("",e);
			}
		}
		//return sbCombo.toString();
		return values.toString();
	}	 
}
