<%@page import="java.io.File"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="net.mineSQL.controller.NASHandler"%>

<%
    response.setContentType("text/html; charset=iso-8859-1");
    Logger log = Logger.getLogger("upload.jsp");

    // Upload request parameters
    String idflusso = "ImportCSV";
    String id = "0_0";/*session.getAttribute("username").toString()+"_"+             
        	session.getAttribute("groupname").toString();*/

    String subfolder = "";
    String departement = "";
    boolean overwrite = false;
    
    // Return variables
    boolean success = true;
    String reason = "";

    try {
        log.debug("MARK_upload_1");
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
        log.debug("MARK_upload_2");
		// Parse the request
        if (ServletFileUpload.isMultipartContent(request))
            log.debug("MARK_upload_2.1 -> isMultipartContent true");
        else
            log.debug("MARK_upload_2.1 -> isMultipartContent false");
        
		List items = upload.parseRequest(request);
        log.debug("MARK_upload_3.1");

		FileItem file = null;
		Iterator params = items.iterator();
		// Process each request parameter
        log.debug("MARK_upload_4");
		while (params.hasNext()) {		    
		    FileItem param = (FileItem) params.next();
		    if (!param.isFormField()) {
			    // This is the file to upload
			    file = param;
		    } else {
			    log.debug("- Param: " + param.getFieldName()
				    + " Value: " + param.getString());
/**
			    if (param.getFieldName().equals("idflusso"))
				    idflusso = param.getString();
			    else if (param.getFieldName().equals("id"))
				    id = param.getString();
			    else 
**/
			    if (param.getFieldName().equals("subfolder"))
		            subfolder = param.getString();
			    else if (param.getFieldName().equals("departement"))
		            departement = param.getString();
			    else if (param.getFieldName().equals("overwrite"))
		            overwrite = (new Boolean(param.getString())).booleanValue();
		    }
		}

        log.debug("MARK_upload_5");
		if (file != null) {
		    reason = "uploaded";
		    /* Lancia aggiornamento da file */
		    reason = NASHandler.upload(file.getName(), file
			    .getInputStream(), idflusso, id, ""/*subfolder*/, departement, overwrite);
		    if (!reason.equals("uploaded") && !reason.equals("exist")) {
			    success = false;
			    log.info(" NAS upload failed for file " + file.getName());
		    }else{
			    log.info(" NAS upload successfully file " + file.getName() +
				     " into " + subfolder);
		    }
		}
    } catch (Exception e) {
        e.printStackTrace();
		success = false;
		reason = e.toString();
		log.error("", e);
    }

    out.write("{\"success\":" + success
		    + ",\"valid\":true,\"reason\":\"" + reason + "\"}");
%>
