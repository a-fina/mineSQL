/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.app.restlet.demo.application;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

import org.restlet.Restlet;
import org.restlet.resource.Directory;

/**
 *
 * @author alessio.finamore
 */
public class Main {

    // URI of the root directory.  
    public static final String ROOT_URI = "file:///C://Users//alessio.finamore//Documents";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Create a new Component.  
        Component component = new Component();

        // Add a new HTTP server listening on port 8182.  
        component.getServers().add(Protocol.HTTP, 8182);
        // Attach the sample application.  
        component.getDefaultHost().attach("/firstSteps",
                new FirstStepsApplication());

        // Create Static-File application  
        component.getClients().add(Protocol.FILE);
        Application application = new Application() {
            @Override
            public Restlet createInboundRoot() {
                return new Directory(getContext(), ROOT_URI);
            }
        };
        component.getDefaultHost().attach("/web", application);

        // Start the component.  
        component.start();
    }

}
