/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.util;

import com.ibm.as400.access.AS400;
import com.ibm.as400.vaccess.AS400ExplorerPane;
import com.ibm.as400.vaccess.VIFSDirectory;
import javax.swing.JFrame;

/**
 *
 * @author alessio.finamore
 */
public class AS400Explorer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set up the explorer pane.
        AS400 system = new AS400("MySystem", "Userid", "Password");
        VIFSDirectory directory = new VIFSDirectory(system, "/myDirectory");
        AS400ExplorerPane explorerPane = new AS400ExplorerPane(directory);
        explorerPane.load();

// Add the explorer pane to a frame.
        JFrame frame = new JFrame("My Window");
        frame.getContentPane().add(explorerPane);
    }

}
