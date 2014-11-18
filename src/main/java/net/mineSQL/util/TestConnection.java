package net.mineSQL.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestConnection extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
        String driver = "com.ibm.as400.access.AS400JDBCDriver";
        String ip = "192.168.10.243";
        String db = "BCD_BGBLUE";
        String username = "TNFIAL";
        String password = "ale20141";
        String jdbc = "as400";
        
		PrintWriter out = response.getWriter();
		try {
			out.println(" Loading Driver");
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName(driver);

			Statement stmt = null;
			ResultSet rset = null;
			// @machineName:port:SID,   userid,  password
			out.println("");
			out.println(" Loading JNDI dataSource ");
			out.println("");
			Connection con = DriverManager.getConnection(
				//"jdbc:oracle:thin:@ws401act.intranet.fw:1523:actsvil", 
				//"jdbc:filemaker://IP/db",
				"jdbc:"+jdbc+"://"+ip+"/"+db,
				username, 
				password);
			/**
			Context env = (Context)new InitialContext().lookup("java:comp/env");
			DataSource ds = (DataSource)env.lookup("jdbc/mineSQL");
			out.println("");
			out.println(" Testing Connection ");
			out.println("");
 			Connection con = ds.getConnection();
			**/


			out.println(" Connection Established Successfully ");
			out.println("");
			      stmt = con.createStatement();
			      rset = stmt
				  //.executeQuery("select 'User name: '||USER||'!' result from dual");
				  .executeQuery("select * from eve_eventi ");
			      out.println(" Testing executeQuery");
			      while (rset.next())
				        out.println(rset.getString(1));
			      out.println(" executeQuery Successfully");
			      rset.close();
			      rset = null;
			      stmt.close();
			      stmt = null;
			      con.close();
			      con = null;

		} catch (ClassNotFoundException e1) {
			out.println("1 Connection Failed " + e1.toString());
			e1.printStackTrace();
		} catch (SQLException e2) {
			out.println("2 Connection Failed  " + e2.toString());
			e2.printStackTrace();
		} catch (Exception e) {
			out.println("5 Connection Failed  " + e.toString());
			e.printStackTrace();
	    }
	}
}
