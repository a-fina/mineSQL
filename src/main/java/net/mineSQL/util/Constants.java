package net.mineSQL.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Constants {

//tabella test
	public static int MIN_ANNO=1997;
	
	//utenza
	public static String POWERUSER="2";
	public static String USER="1";
	
	//flussi
	public static String FLUSSO_COMPENDING = "1";
	
	// stati
	
	// gruppi
	public static String GRUPPO_ADMIN = "0";
	
	// ruoli
	public static String RUOLO_READONLY = "3";
	public static String RUOLO_READWRITEONLY = "4";
	
	// reparti
	public static String REPARTO_QA = "4";
	public static String REPARTO_DEV = "5";
	
	// urgenza
	public static String URGENZA_MOLTOALTA = "4";
	
	// popup
	public static int POPUP_1_NOT_LAST_CLOSED = 1;
	public static int POPUP_2_LAST_CLOSED_NOT_SAME_SYSTEM = 2;
	public static int POPUP_3_LAST_CLOSED_SAME_SYSTEM = 3;
	
	// identificativo di cambiamenti di valori particolarmente significativi
	public static int TASK_CLOSED_BUT_NOT_SOLVED = 1;
	public static int TASK_BACK_ACCEPTED_DEV = 2;
	public static int TASK_CLOSED_BUT_NOT_DEFECT = 3;
	public static int TASK_CLOSED = 4;

	public static String getAnnoCalendar(String anno, String mese, String giorno){
		GregorianCalendar gc = new GregorianCalendar();
		StringBuffer options= new StringBuffer();
		options.append("<select name=\"giorno\">");
		if(giorno !=null && giorno.equalsIgnoreCase("15")){
			options.append("<option value=\"01\" >01</option>");
			options.append("<option selected value=\"15\" >15</option>");
		}else{
			options.append("<option selected value=\"01\" >01</option>");
			options.append("<option value=\"15\" >15</option>");
		}
		options.append("</select>");
		options.append("<select name=\"mese\">");
		for(int i =1; i<=(12);i++){
			String value=""+i;
			if(value.length()==1){
				value="0"+i;
			}
			if(value.equalsIgnoreCase(mese)){
				options.append("<option selected value=\""+value+"\">"+value+"</option>");
			}else{
				options.append("<option value=\""+value+"\">"+value+"</option>");
			}
		}
		options.append("</select>");
		options.append("<select name=\"anno\">");
		int start=gc.get(Calendar.YEAR);
		for(int i =0; i<=(gc.get(Calendar.YEAR) - MIN_ANNO);i++){
			if((""+start).equalsIgnoreCase(anno)){
				options.append("<option selected value=\""+(start)+"\">"+(start)+"</option>");
			}else{
				options.append("<option value=\""+(start)+"\">"+(start)+"</option>");
			}
			start--;
		}
		options.append("</select>");
		return options.toString();
	}
	public static void main(String[] args){
		GregorianCalendar gc = new GregorianCalendar();
		System.out.println(gc.toString());
		System.out.println(getAnnoCalendar(""+gc.get(GregorianCalendar.DAY_OF_MONTH),""+(gc.get(GregorianCalendar.MONTH)+1),""+gc.get(GregorianCalendar.YEAR)));
	}

}


