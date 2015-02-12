package net.mineSQL.controller;

import net.mineSQL.model.NASFile;
import net.mineSQL.util.ApplicationWatcher;
import net.mineSQL.util.FileComparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


public class NASHandler {
    private static final Logger log = Logger.getLogger(NASHandler.class);

    /**
     * Ritorna il JSON paginato, relativo alla lista di file su NAS
     * associati a un Compending, WA, Defect o Task
     * @param idflusso Compending, WA, ...
     * @param id identificativo del Compending, WA, ...
     * @param folderFilter filtro per estrarre solo la sottodirectory indicata
     *        (caso Task sottodirectory del Defect)
     * @param sortField campo in base al quale ordinare la lista
     * @param sortDir ordinamento ASC o DESC
     * @param start riga di partenza della lista
     * @param limit max numero di righe che pu� contenere una pagina della lista 
     * @return lista di file in formato JSON
     */
    public static String list(String idflusso, String id, String folderFilter, String sortField, String sortDir, String start, String limit, String subFolder) {
	String json = "";
	JSONObject list = new JSONObject();
	JSONArray jsonFiles = new JSONArray();
	String path = getDestPath(idflusso, id, subFolder);
	
	boolean hasParentFolder = (subFolder != null && subFolder.length() > 0);
	// Eventuale dir '..' di livello gerarchico superiore
	if (hasParentFolder && start.equals("0")) {
	   // path += File.separator + subFolder;
	    JSONObject jsonFile = new JSONObject();
	    jsonFile.put("name", "..");
	    jsonFile.put("bytes", "");
	    jsonFile.put("lastmodified", "");
	    jsonFile.put("dir", "true");
	    jsonFiles.add(jsonFile);
	}

	SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	// Parametri per l'ordinamento della lista di file
        String field = (sortField == null)?"lastmodified":sortField;
        String dir = (sortDir == null)?"DESC":sortDir;
        ArrayList fileArray = sortedFileList(new File(path), folderFilter, field, dir);

	// Paginazione lista di file
	int first = new Integer(start).intValue() - 
	               ((!start.equals("0") && hasParentFolder)?1:0);
	int last = first + new Integer(limit).intValue() - ((hasParentFolder)?1:0);

	for (int i = first; (i < last && i < fileArray.size()) ; i++) {
	    boolean isDir = false;
	    boolean selectable = true;

	    NASFile file = (NASFile)fileArray.get(i);

	    JSONObject jsonFile = new JSONObject();
	    jsonFile.put("name", file.getFile().getName());
	    jsonFile.put("pathname", file.getFile().getAbsolutePath().replace('\\', '/'));
	    jsonFile.put("bytes", new Long(file.getFile().length()).toString());
	    jsonFile.put("lastmodified", fmt.format(new Date(file.getFile().lastModified())));
	    jsonFile.put("owner", file.getOwner());
	    // Nel caso di directory, il file non puo' essere selezionato
	    if (file.getFile().isDirectory()) {
		isDir = true;
		selectable = false;
	    }
	    jsonFile.put("dir", new Boolean(isDir).toString());
	    jsonFile.put("selectable", new Boolean(selectable).toString());
	    jsonFiles.add(jsonFile);
	}

	// Conta anche l'eventuale directory '..'
	int totalCount = fileArray.size() + ((hasParentFolder)?1:0);

	// Costruzione JSON completo
	list.put("totalCount", "" + totalCount);
	list.put("topics", jsonFiles);
	json = list.toString();

	return json;
    }

    public static String getFileName(String filename){
        // split per IE che riempe con il path assoluto il nome del file
	    String[] filenameToSplit = filename.split(File.separator+File.separator );
	    if (filenameToSplit.length <= 1)
	        filenameToSplit = filename.split("\\\\"); //problema separatore IE7 su server unix
	    return filenameToSplit[filenameToSplit.length-1];
    }
    /**
     * Effettua l'upload di file su NAS
     * 
     * @param filename nome file da uploadare
     * @param file stream contenente il file
     * @param idflusso Compending, Wa, Defect, Task
     * @param id identificativo del Compending, Wa, ...
     * @param overwrite <code>true</code> o <code>false</code>
     * @return messaggio con esito operazione o errore verificatosi
     */
    public static String upload(String filename, InputStream file,
	    String idflusso, String id, String subFolder, String departement, boolean overwrite) {
	String returnMsg = "";
	FileOutputStream nasFile = null;
	try {
	    String destPath = getDestPath(idflusso, id, subFolder) + File.separator + departement;
	    // Se il path non esiste, viene creato su file system
	    if (!checkPath(destPath))
		mkDir(destPath);

	    // split per IE che riempe con il path assoluto il nome del file
            filename = getFileName(filename);
	    log.info(" NASHandler upload path: " + destPath + " fileName: " + filename);
	//    log.info(" NASHandler new upload path: " + destPath + " fileName: "
	//		+ str = java.net.URLEncoder.encode( filenameToSplit[filenameToSplit.length-1] ,
	//						     "UTF-8"));
            File outputFile = new File(destPath + File.separator + filename);
	    // Creo il file se non esiste gia' o se e' da sovrascrivere
	    if (!outputFile.exists() || overwrite) {
		if (overwrite)
		    outputFile.delete();
		// Crea il file
		outputFile.createNewFile();
		nasFile = new FileOutputStream(outputFile);
		int c;
		// Ciclo di scrittura
		while ((c = file.read()) != -1)
		    nasFile.write(c);

		returnMsg = "uploaded";
	    } else {
		returnMsg = "exist";
	    }
	} catch (IOException e) {
	    returnMsg = e.toString();
	    log.error("", e);
	} finally {
	    try {
		// Chiusura file e rilascio risorse
		if (file != null) file.close();
		if (nasFile != null) nasFile.close();
	    }
	    catch (IOException e) {
		returnMsg = e.toString();
		log.error("", e);
	    }
	}
	return returnMsg;
    }
    
    public static boolean delete(String pathname) {
	File file = new File(pathname);
	return file.delete();
    }

    /**
     * Ritorna la sottodirectory di pertinenza del flusso considerato
     * 
     * @param idflusso
     * @return
     */
    public static String getSubFolder(String idflusso) {
	String folder = "";

	//if (idflusso.equals(Constants.FLUSSO_COMPENDING))
	    folder = ApplicationWatcher.NASDir;


	return folder;
    }

    public static String getDestPath(String idflusso, String id, String subfolder) {
	log.debug("getDestPath:  " + 
		ApplicationWatcher.NASDir + File.separator +
		getSubFolder(idflusso) + File.separator + 
		id + ((subfolder!=null)?File.separator+subfolder:"") );

	return ApplicationWatcher.NASDir + File.separator +
		getSubFolder(idflusso) + File.separator + 
		id + ((subfolder!=null)?File.separator+subfolder:"");
    }

    /**
     * Controlla se il path specificato esiste gia' o meno
     * 
     * @param path path da controllare
     * @return <code>true</code> o <code>false</code>
     */
    private static boolean checkPath(String path) {
	return (new File(path)).exists();
    }

    /**
     * Crea il path desiderato
     * 
     * @param path path da creare su NAS
     * @return <code>true</code> o <code>false</code>
     * @throws IOException
     */
    private static boolean mkDir(String path) throws IOException {
	return (new File(path)).mkdirs();
    }

    /**
     * Ritorna l'array di file contenuti in una directory ordinata in base
     * ad un determinato attributo e in ordine ASC o DESC.
     * Tiene conto dell'alberatura organizzata per reparto (owner).
     * @param folder directory su NAS contenente i file
     * @param sortAttribute attributo dei file sui quali ordinare l'array
     * @param sortDir ASC o DESC
     * @return lista ordinata di file contenuti nella directory
     */
    private static ArrayList sortedFileList(File folder, String folderFilter, String sortAttribute, String sortDir) {
	if (!folder.isDirectory()) {
	    return new ArrayList();
	}
	FileComparator fileCmp = new FileComparator(sortAttribute, sortDir);
	ArrayList fileList = new ArrayList();
	File files[] = folder.listFiles();
	for (int i = 0; i < files.length; i++) {
	    if (files[i].isDirectory()) { 
		// Se e' una sottodirectory numerica, sara' l'idtask, quindi aggiungo la sottodirectory
		if (files[i].getName().matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
		    // Potenzialmente e' una dir Task, vediamo se supera il filtro
		    if (folderFilter != null && folderFilter.length() > 0 &&
		           files[i].getName().equals(folderFilter))
		    {
			File taskDir = new File(folder.getAbsolutePath() + File.separator + files[i].getName());
			File[] taskFiles = taskDir.listFiles();
			for (int j = 0; j < taskFiles.length; j++) {
			    if (taskFiles[j].isDirectory()) {
				// E' una directory che identifica un reparto,
				// quindi devo aggiungere i file che contiene
				File depDir = new File(folder.getAbsolutePath() + File.separator + files[i].getName() + File.separator + taskFiles[j].getName());
				File[] depFiles = depDir.listFiles();
				for (int k = 0; k < depFiles.length; k++) {
				    fileList.add(new NASFile(depFiles[k], depDir.getName()));
    			    	}
    			    }
			    else {
				// E' un file non una directory
				fileList.add(new NASFile(taskFiles[j], ""));
			    }
    		    	}
		    }
		}
	        // E' una directory che identifica un reparto,
	        // quindi devo aggiungere i file che contiene
		else {
		    File depDir = new File(folder.getAbsolutePath() + File.separator + files[i].getName());
		    File[] depFiles = depDir.listFiles();
		    for (int j = 0; j < depFiles.length; j++) {
			fileList.add(new NASFile(depFiles[j], depDir.getName()));
		    }
		}
	    }
	    else {
		// E' un file non una directory
		fileList.add(new NASFile(files[i], ""));
	    }
	}
	// Ordino i file
	Collections.sort(fileList, fileCmp);

	return fileList;
    }
}
