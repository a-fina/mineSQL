package net.mineSQL.util;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import org.apache.log4j.Logger;

public  class FirstFilter implements Filter {
    private static final Logger log = Logger.getLogger(FirstFilter.class);
    private FilterConfig filterConfig = null;   
    private PrintWriter out = null;

   private boolean skipThisURL(String URL){
     boolean skip = true;
     // Filtro tutto tranne le .jsp
     if ( URL.endsWith(".jsp") ){
	log.debug(" Manage filter on URL: " + URL);
        skip = false;
     }

     return skip;
   }

    public void init (FilterConfig filterConfig) throws ServletException {
        log.info(" init ");
        this.filterConfig = filterConfig;
    }
    public void destroy () {
        log.info(" destroy ");
        this.filterConfig = null;
    }
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException 
    {
        HttpServletRequest  req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        HttpSession         ses = req.getSession();
       
        out = res.getWriter();
 
        try
        {
            //Elaborazione REQUEST
            //ServletContext context = config.getServletContext();
            String URL = req.getRequestURL().toString(); 

            log.info("");
            log.info(" /***************** MineSQL Access Filter Entry *********************/");
            log.info("      URL      : " + URL);

            // Get the current session ID by searching the received cookies.
            String sessionid = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
              for (int i = 0; i < cookies.length; i++) {
                  log.info("Cookie: " + cookies[i].getName() + "="+ cookies[i].getValue());
            //    if (cookies[i].getName().equals("sessionid")) {
            //      sessionid = cookies[i].getValue();
            //      break;
            //    }
              }
            }

            // Stampa l'errore in JSON o HTML
            log.debug("  MARK   requested filter output type: " + filterConfig.getInitParameter("type"));

	    if ( skipThisURL(URL) ){
                    chain.doFilter(req, res);
            	    log.info(" /***************** MineSQL Skip Access Filter URL *********************/");
	    }else{
		    // User request Info
		    log.info("      Remote IP: " + req.getRemoteAddr());
		    // Session Info
		    log.info("      session ID: " + ses.getId() + "   Max Inactive Time (seconds): " + ses.getMaxInactiveInterval() );
		    long dateTimes = ses.getCreationTime();
		    log.info("      session Creation Time  : " + new Date( dateTimes ) );
		    dateTimes = ses.getLastAccessedTime();
		    if( dateTimes < 0 ) {
		      log.info("      session Last Access Time: Never Been Access Befored" );
		    }
		    else {
		      log.info("      session Last Access Time: " + new Date( dateTimes ) );
		    }


		    String error = "Sessione scaduta";

		    if (filterConfig.getInitParameter("type").equals("json"))
            {
                // Controlla se l'autenticazione dell'utente ancora valida nelle richieste AJAX
                if ( ses.getAttribute("IDUTENTE") != null && ses.getAttribute("IDGRUPPO") != null ){
                    log.info("       Utente ID: " + ses.getAttribute("IDUTENTE").toString() + 
                         "       name: " + ses.getAttribute("username").toString() );
                    log.info("       Gruppo ID: " + ses.getAttribute("IDGRUPPO").toString() + 
                         "       groupname: " + ses.getAttribute("groupname").toString() );
                    chain.doFilter(req, res);
                    //res.addHeader("Content-Type","text/x-json; charset=ISO-8859-1");
                }else{
                    // Retunr JSON response
                    res.addHeader("Content-Type","text/x-json; charset=ISO-8859-1");
                        out.write("{\"totalCount\":\"0\",\"topics\":[{\"session\":\"" +error + "\"}]}");		
                    log.error(error);
                }
            }else if (filterConfig.getInitParameter("type").equals("html"))
            {
                // Controlla se l'autenticazione ancora valida o siamo dopo domainlogon per UtenteServlet
                if(ses.getAttribute("username")!=null || req.getParameter("username") != null ) {
                    log.info( "    request username: " + req.getParameter("username") );
                    log.info( "    session username: " + ses.getAttribute("username") );
                    chain.doFilter(req, res);
                }else{
                    // Return HTML response 
                    res.addHeader("Content-Type","text/html; charset=ISO-8859-1");
                    out.write("<body><center>");
                    out.write("<script type=\"text/javascript\">");
                    String address = "http://"+ req.getServerName()+ ":" + req.getServerPort();
                    out.write("function viewMain(){");
                    out.write("window.location.href=\""+address+"\";");
                    out.write("}</script>");
                    out.write("<p><img width='477' hspace='0' height='62' border='0' align='top' src='img/Top_sx.gif'/></p>");
                    out.write("<h2>"+error+"</h2>");
                    out.write("<input type='button' onclick='javascript:viewMain();' value='Ricarica la pagina'></input>");
                    out.write("</center></body>");
                }
            }else{
			//response.setContentType("text/html");
                out.write("Filter Error, invalid type parameter");
            }
            
	    }
            //TEST TODO setCookies(res);

            log.info(" /***************** MineSQL Access Filter Exit *********************/");
            log.info("");

            //Elaborazione RESPONSE
            //res.sendRedirect("/tecnologia.jsp");
        } 
        catch( Exception e )
        {
            log.error(" Ricevuta Eccezione " , e);
            throw new ServletException( e );
        }

    }

    private void setCookies(HttpServletResponse res){
        res.addCookie( new Cookie("defect_filter", new Date().toString() ) );
    }
}
