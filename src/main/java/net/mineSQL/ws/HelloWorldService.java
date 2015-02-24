/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import net.mineSQL.model.Data;

/**
 *
 * @author alessio.finamore
 */

@Path("/helloworld")
public class HelloWorldService {

    /**
     *
     * @param artist_id
     * @param from
     * @param to
     * @return
     */
    @GET
    @Path("/query/{artist_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Data getMsg(@PathParam("artist_id")  int artist_id,
                       @QueryParam("from")      int from,
                       @QueryParam("to")        int to) {

        Data d=new Data();
        d.setName("Mateen");
        d.setRoll(77);
        return d;

    }
}
