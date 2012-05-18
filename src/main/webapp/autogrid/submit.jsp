<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="it.fastweb.util.Utilita" %>
<%@ page import="it.fastweb.controller.CommonOperation"%>
<%@ page import="com.Ostermiller.util.ExcelCSVParser"%>

<%!
    Logger log = Logger.getLogger("submit.jsp");

    public boolean passagioDiStato(HttpServletRequest req){
        String padre = "";
        String figlio = "";

        if ( req.getParameter("IDSTATO")!=null )
            figlio = req.getParameter("IDSTATO");
        // Caso COMPENDING
        else if ( req.getParameter("IDSTATO_COMPENDING")!=null )
            figlio = req.getParameter("IDSTATO_COMPENDING");

        if ( req.getParameter("FLAG_IDSTATO")!=null )
            padre = req.getParameter("FLAG_IDSTATO");
        // Caso COMPENDING
        else if ( req.getParameter("FLAG_IDSTATO_COMPENDING")!=null )
            padre = req.getParameter("FLAG_IDSTATO_COMPENDING");

        if (! padre.equals(figlio) )
            return true;
        else
            return false;

    }

    public String getFormattedDate(){
        Date sysDate = new Date();
        String fullDate = sysDate.toString();
        Calendar cal = new GregorianCalendar();
        String[] monthName = {"Jan", "Feb",
                "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov",
                "Dec"};
        
        String time = fullDate.substring(11, 19);
        return ""+cal.get(Calendar.DAY_OF_MONTH)+" "+
               ""+monthName[cal.get(Calendar.MONTH)]+" "+
               ""+cal.get(Calendar.YEAR)+" "+
               ""+time;
    }
   
    /**
    * Cicla sulle variabili POST elencate
    * @param List elenco variabili
    * @param HttpServletRequest _req
    * @return HashMap compatibile al controllo di coerenza
    */
    public class PostMap { 
        private HashMap _hm = new HashMap(); 
        private HttpServletRequest _req = null;

        public PostMap(HttpServletRequest req){
          	_req  = req;
		// pm.getNoteHistory();
		List ep = new ArrayList();
		ep.add("DATACREAZIONE");
		getErasableParams( ep );
		
		getCombo(); 
		
		List fp = new ArrayList();
		fp.add("IDDEFECT");
		fp.add("IDTECNICO");
		fp.add("IDCOMPONENTE");
		getForwardParams( fp );
		
		List gp = new ArrayList();
		gp.add("IDUTENTE");
		getGlobalParams( gp );
        } 

        public HashMap toHashMap(){
            return _hm;
        }

	public boolean onlyCheck(){
		boolean onlyCheck = false;	
		if(_req.getParameter("check")!=null &&  
		   _req.getParameter("check").length()>0)
		{
		    onlyCheck = true;	
		}
		return onlyCheck;
	}

        // FLAG_ serve per il controllo di Coerenza, il campo puo essere sbianchettato
        public boolean getGlobalParams(List parameters){
            for(Iterator it = parameters.iterator(); it.hasNext();) 
            {
                String name = it.next().toString();
                if(_req.getSession().getAttribute(name)!=null)
                     _hm.put(name, _req.getSession().getAttribute(name).toString());
            } 
            return true;
        }
        // FLAG_ serve per il controllo di Coerenza, il campo puo essere sbianchettato
        public boolean getForwardParams(List parameters){
            for(Iterator it = parameters.iterator(); it.hasNext();) 
            {
                String name = it.next().toString();
                if(_req.getParameter(name)!=null)
                	   _hm.put(name, _req.getParameter(name));
            } 
            return true;
        }
        // FLAG_ serve per il controllo di Coerenza, il campo puo essere sbianchettato
        public boolean getErasableParams(List parameters){
            for(Iterator it = parameters.iterator(); it.hasNext();) 
            {
                String name = it.next().toString();
                if(_req.getParameter(name)!=null) {
                    if (name.equals("DESCRIZIONE") || name.equals("NOTE"))
                	   // per i campi che sono htmleditor sulla form
                	   _hm.put(name, Utilita.htmlTagConv(_req.getParameter(name)));
                    else
                	   _hm.put(name, _req.getParameter(name));
                }
                else
                    _hm.put(name, _req.getParameter("FLAG_" + name)); 
            } 
            return true;
        }

	/**
	 * Passo sempre il valore attuale di IDGRUPPO per confrontarlo
	 * con quello nuovo che verra' calcolato in CommonOperation
	  TODO: trovare una convenzione per automatizzare questo ciclo
	 */
        public void getCombo(){
		 if(_req.getParameter("IDGRUPPO_COMPENDING")!=null)
		     _hm.put("IDGRUPPO", _req.getParameter("IDGRUPPO_COMPENDING"));
		 else 
		     _hm.put("IDGRUPPO", _req.getParameter("FLAG_IDGRUPPO"));
	}

        
        public boolean getNoteHistory(){
            //MARK: VALHALLA_32 | Come nuova funzionalità, il campo NOTE diventa lo storico delle note.
            //Viene eseguita una "append" della nuova nota con il contenuto del campo NOTE corrente.
            if (_req.getParameter("NEWNOTE")!=null && _req.getParameter("NEWNOTE").length()>0) {
                
                if (!_req.getParameter("NEWNOTE").toString().trim().equals("")) {
                    String noteHistory;
                    String formattedDate = getFormattedDate();
                    
                    if(_req.getParameter("NOTE")!=null && _req.getParameter("NOTE").length()>0)
                        noteHistory = Utilita.htmlTagConv(_req.getParameter("NOTE"));
                    else 
                        noteHistory = _req.getParameter("FLAG_NOTE");
                     
                    String user = _req.getSession().getAttribute("username").toString();
                    noteHistory = "[" + formattedDate + " - " + user + "] \n" + 
                                  _req.getParameter("NEWNOTE") +
                                  "\n\n" +
                                  noteHistory;
                    
                    log.debug(" Storico Note: " + noteHistory );
                    _hm.put("NOTE", noteHistory);
                }
            }
            return true;
        }

    }

  public HashMap CSVtoMap(HttpServletRequest req, String row) throws IOException {
	String userName=req.getSession().getAttribute("username").toString();
	String userId=req.getSession().getAttribute("IDUTENTE").toString();
	String formattedDate=getFormattedDate();
    HashMap newRow = new HashMap();
	newRow.put("IDUTENTE",userId);

	// Parse the data
		String[][] values = ExcelCSVParser.parse(
		    new StringReader(row),';'
		);

		// TODO: trovare una convenzione per fissare le posizioni
		//	 delle colonne. Usare una vista anche per l'export 
 		//	 CSV, e poi per rileggere da import ciclare sulle colonne
     		//	 della vista. Cosi la posizione delle colonne nel CSVexport e CSVimport
		//	 sarà automatica.
		for (int i=0; i<values.length; i++){//righe
		    for (int j=0; j<values[i].length; j++){//colonne
			switch(j){
				case 20:
				    newRow.put("IDDEFECT",(values[i][j].equals(" "))?"":values[i][j]);break;
		        case 21:
		            newRow.put("IDTECNICO",(values[i][j].equals(" "))?"":values[i][j]);break;
				case 22: newRow.put("IDGRUPPO",values[i][j]);break;
				case 23: newRow.put("IDCOMPONENTE",values[i][j]);break;
				case 24:
				    if (!values[i][j].equals(""))
					     newRow.put("NOTE", "[" + formattedDate + " - " + userName + "] \n" + values[i][j]+ "\n\n");
					break;
			}
			//log.info(values[i][j]);
		    }
		    //log.info("-----");
		}
	
	return newRow;
    }
%>
