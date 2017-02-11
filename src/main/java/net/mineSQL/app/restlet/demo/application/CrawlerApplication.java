/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.app.restlet.demo.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class CrawlerApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());

        // Defines only one route
        router.attach("/crawl", CrawlerResource.class);

        return router;
    }

}