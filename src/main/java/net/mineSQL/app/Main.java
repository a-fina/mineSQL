/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.app;

import net.mineSQL.tomcat.MineBrowser;
import net.mineSQL.tomcat.MineTomcat;

/**
 *
 * @author alessio.finamore
 */


public class Main{

    public static void main(String[] args) throws Exception {

        if ( args != null ){
            
        }
        //TODO spostare in ApplicationWatcher fare oggetto statico context
        // shutdown alla chiusura del bbrowser
        MineTomcat mTm = new MineTomcat();
        MineBrowser mBr = new MineBrowser();
        
        // Start Tomcat
        mTm.start();
        // Start Browser
        mBr.start();
        // Wait tomcat
        mTm.await();
    }
}