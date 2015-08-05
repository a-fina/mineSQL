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

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.ProgramParameter;

/**
 *
 * @author alessio.finamore
 */
public class CallRPG {
    
    public CallRPG() {
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
    // @Test
    public void hello() {
   String server="yourserver.company.com";
  String user = "AS400USER";
  String pass = "AS400PWRD";
 
  String input = "RAZA";
  String fullProgramName = "/QSYS.LIB/RAZA.LIB/RAZPGM.PGM";
 
  AS400 as400 = null;
   
  try  {
    
   // Create an AS400 object  
   as400 = new AS400(server, user, pass);  
 
   // Create a parameter list
   // The list must have both input and output parameters
   ProgramParameter[] parmList = new ProgramParameter[2];  
 
   // Convert the Strings to IBM format
   byte[] inData = input.getBytes("IBM285");
 
   // Create the input parameter  
   parmList[0] = new ProgramParameter(inData);
 
   // Create the output parameter
   parmList[1] = new ProgramParameter(5);
 
   // Create a program object  
   // specifying the name of the  
   // program and the parameter list.  
   ProgramCall pgm = new ProgramCall(as400);  
   pgm.setProgram(fullProgramName, parmList);  
 
   // Run the program.  
   if (!pgm.run()) {  
    // If the AS/400 cannot run the  
    // program, look at the message list  
    // to find out why it didn't run.  
    AS400Message[] messageList = pgm.getMessageList();
    for (AS400Message message : messageList) {
     System.out.println(message.getID() + " - " + message.getText());
    }
 
   } else {  
    // Else the program ran. Process the  
    // second parameter, which contains  
    // the returned data.
    byte[] data = parmList[1].getOutputData();  
    String lastName = new String(data, "IBM285").trim();
 
    System.out.println("Output is " +  lastName);  
   }  
 
  } catch (Exception e) {  
   e.printStackTrace();  
  }finally{
   try{
    // Make sure to disconnect   
    as400.disconnectAllServices();  
   }catch(Exception e){}
  }  
   
  System.exit(0);   
    }
}
