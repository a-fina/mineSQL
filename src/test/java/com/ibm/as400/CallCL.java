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





/******************** USO DEI PARAMETRI
 * try{
//    Set up variables
       String vendorSeq = null;
       String rtnParm = null;
       String rtnValues = null;
       ProgramCall program = new ProgramCall(iSeries);

//    Get program library       
       QSYSObjectPathName pgmName = new QSYSObjectPathName("%LIBL%","RD0010L","PGM");

//     Set up the 3 parameters.
       ProgramParameter[] parameterList = new ProgramParameter[3];

//     First parameter is input 
       AS400Text keys = new AS400Text(40,iSeries);
       parameterList[0] = new ProgramParameter(keys.toBytes(tpi.getFileName().substring(0,4) + tpi.getFileName().substring(12,20) + tpi.getFileName().substring(21,29) + tpi.getTransactionType() + tpi.getFileLength() + appFlag));
       
//     Second parameter is input.
       AS400Text vendorKey = new AS400Text(256,iSeries);
       vendorSeq = tpi.getVendorSeq();
       parameterList[1] = new ProgramParameter(vendorKey.toBytes(vendorSeq));
       
//     Third Parameter is return values (Valid App/Acct(Y/N) and other data in the other 11 positions)
       parameterList[2] = new ProgramParameter(12);
       
//     Set the program name and parameter list.
       program.setProgram(pgmName.getPath(), parameterList);

//    Run the program.
       if (program.run() != true){
           // Report failure.
           System.out.println("Program failed!");
           // Show the messages.
           AS400Message[] messagelist = program.getMessageList();
           for (int i = 0; i < messagelist.length; ++i)
           {
               // Show each message.
               System.out.println(messagelist<i>);
           }
           return false;
        }else{
           // Retrieve and inspect output parameter
           AS400Text rtntext = new AS400Text(12,iSeries);
           rtnValues = (String)rtntext.toObject(parameterList[2].getOutputData());
           if (rtnValues.substring(0,1).equalsIgnoreCase("Y")){
               // do stuff 
               return true;
           }else{
               return false;
           }
        }
 ***********************/

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
