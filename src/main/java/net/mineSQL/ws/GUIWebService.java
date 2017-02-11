/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ws;

/**
 *
 * @author alessio.finamore
 */
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import net.mineSQL.util.Utilita;
import org.apache.log4j.Logger;

// Plain old Java Object it does not extend as class or implements 
// an interface
// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 
// The browser requests per default the HTML MIME type.
//Sets the path to base URL + /hello
@Path("/gui")
public class GUIWebService {

    private static final Logger log = Logger.getLogger(GUIWebService.class);

    // This method is called if TEXT_PLAIN is request
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello MineSQL GUI Handler";
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultJSON getLogin(@FormParam("USERNAME") String username,
                           @FormParam("PASSWORD") String password,
                           @Context HttpServletRequest request) {

		String splitUserName = "";
        String result = "";
        
        log.info(" --------------->   MineSQL  HTTP Request <-----------------");
        if (request.getSession().getAttribute("USERNAME") == null) {
            if (username != null) {
                if (username.indexOf("\\") != -1) {
                    String[] usernameToSplit = username.split("\\\\");
                    splitUserName = usernameToSplit[1];
                    username = splitUserName;
                }
            }
        }

        if (!username.isEmpty() && password.equals("12345")) {
            request.getSession().setAttribute(Utilita.USERNAME, username);
            request.getSession().setAttribute(username, Utilita.ISLOGGEDIN);
            return new ResultJSON("true","true","Utente corretto");
        }
        return new ResultJSON("false","true","Errore Utente");
    }

    @GET
    @Path("/menu/database")
    public String getMenuDatabase(@PathParam("model") String model, @Context HttpServletRequest httpRequest) {
        log.debug("getMenuDatabase: " + model);

        return "TODO";
    }
}
