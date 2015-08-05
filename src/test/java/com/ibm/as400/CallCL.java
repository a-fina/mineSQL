/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.as400;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;


/**
 *
 * @author alessio.finamore
 */
public class CallCL {

    public CallCL() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
        String server = "yourserver.company.com";
        String user = "AS400USER";
        String pass = "AS400PWRD";

        String commandStr = "MYLIB/RUNMYJOB SOME(PARAM)";

        AS400 as400 = null;
        try {
            // Create an AS400 object  
            as400 = new AS400(server, user, pass);

            // Create a Command object
            CommandCall command = new CommandCall(as400);

            // Run the command.
            System.out.println("Executing: " + commandStr);
            boolean success = command.run(commandStr);

            if (success) {
                System.out.println("Command Executed Successfully.");
            } else {
                System.out.println("Command Failed!");
            }

            // Get the command results
            AS400Message[] messageList = command.getMessageList();
            for (AS400Message message : messageList) {
                System.out.println(message.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Make sure to disconnect   
                as400.disconnectAllServices();
            } catch (Exception e) {
            }
        }
        System.exit(0);
    }
}
