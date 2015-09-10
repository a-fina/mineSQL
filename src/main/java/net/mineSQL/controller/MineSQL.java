/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.controller;
import java.sql.SQLException;
import net.mineSQL.connection.ConnectionManager;

/**
 *
 * @author alessio.finamore
 */
public class MineSQL {
 
    public static String getSchemaList(String dbType, boolean showAllDB, String database) throws SQLException{
         String sqlSCHEMA = "";
                 
        if ( dbType.equals(ConnectionManager.DB2) || dbType.equals("as400") ){
            
                sqlSCHEMA = "select TABLE_SCHEM as SCHEMA from SYSIBM.SQLSCHEMAS";
                //MySQL: SHOW DATABASES
                if (! showAllDB ) {
                    //MySQL where = " like '" + database+"'" ;
                    //DB2
                    sqlSCHEMA += " where TABLE_SCHEM like '" + database +"'";
                }
        } else if ( dbType.equals(ConnectionManager.H2) ){
            
                sqlSCHEMA = "SHOW SCHEMAS";
                //MySQL: SHOW DATABASES
               /* if (! showAllDB ) {
                    //MySQL where = " like '" + database+"'" ;
                    //DB2
                    sqlSCHEMA += " WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME LIKE '" + database +"'";
                } */
        }else {
            throw new SQLException("MineSQL dbType: " + dbType +" NOT supported yet");
        }

        return sqlSCHEMA; 
    }


    public static String getTableList(String dbType, String currentDB) throws SQLException{
        String sqlTABLE = "";
                
        if ( dbType.equals(ConnectionManager.DB2) || dbType.equals("as400") ){
                    //MySQL String sqlTABLE = "show tables from `" + currentDB +"`";
                    sqlTABLE = "select TABLE_NAME, TABLE_TEXT  from SYSIBM.SQLTABLES where TABLE_SCHEM = '"
                                      + currentDB + "'";
                    // MySQL String sql = "show tables from `" + currentDB +"`";
        } else if ( dbType.equals(ConnectionManager.H2) ){
                    sqlTABLE = "SHOW TABLES FROM " + currentDB;
        }else {
            throw new SQLException("MineSQL dbType: " + dbType +" NOT supported yet");
        }
      
        return sqlTABLE; 
    }
}
