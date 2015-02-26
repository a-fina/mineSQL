/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rockscissorpaper.services;

import com.mycompany.rockscissorpaper.controller.Choice;
import com.mycompany.rockscissorpaper.model.Player;
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

@Path("/morra")
public class MorraCinese {

    /**
     * Read player choice
     * 
     * @param playerType
     * @param name
     * @return
     */
    @GET
    @Path("/choice/{player_type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Choice getChoice(@PathParam("player_type")  String playerType,
                            @QueryParam("name")        String name) {

        Player player = new Player();
        Choice choice = null; 
        
        if ( playerType.equals("computer")   ){ 
            choice = player.randomChoice();
        }else{
            // TODO do human operation
        }

        if ( name != null && name.equals("name") ){
            // Custom action TODO
        }

        return choice;

    }
}
