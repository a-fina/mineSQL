/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.controller;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
/**
 *
 * @author alessio.finamore
 */
public class Excel {

    private static final Logger log = Logger.getLogger(Excel.class);
    
    /*
    * IBM DB2 Read column description
    */
    public String getColumnTextDB2(Connection con, String lib, String table, String column) throws SQLException {
        
            PreparedStatement ps = null;
            ResultSet rs = null;

            ps = con.prepareStatement("select COLUMN_TEXT  from SYSIBM.SQLCOLUMNS where TABLE_SCHEM = ? and TABLE_NAME = ? and COLUMN_NAME = ?");

            ps.setString(1, lib); 
            ps.setString(2, table); 
            ps.setString(3, column);

            rs = ps.executeQuery();

            rs.next();

           return rs.getString(1);
        
    }

    public String getFile(Connection con, String query, String[] hiddenColumns) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        log.info("Get file Excel con: "  + con + " query: " + query);

        ps = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        rs = ps.executeQuery();
        ResultSetMetaData rsMd = rs.getMetaData();
        int numberOfColumns = rsMd.getColumnCount();


        rs.last();
        int countRow = rs.getRow();
        rs.beforeFirst();

        log.info("Get file Excel con: "  + con + " query: " + query + " numberOfColumns: " + numberOfColumns + " countRow: " + countRow);
        // TODO: StringBuilder optimization
        StringBuilder html = new StringBuilder()
                .append("<html>\n<head>\n<style>\n.type-GEN {\nmso-number-format:General;\n}\n.type-CHAR{\nmso-number-format:\"\\@\";/*force text*/\n}\n.type-NUMERIC{\nmso-number-format:\"\\#0\\.00\";\n}\n.intestazione {\nbackground-color: #5bc0de;\ncolor: #000;\nfont-size: 10px;\n}\n</style>\n</head>\n<body>");

        if (countRow == 0) {
            html.append("<table id=\"tabella-report\" border=\"1\">\n<thead>	\n<tr>\n<th>Messaggio</th>\n</tr>\n</thead>\n<tr>\n<td width=\"150\"><p>Nessuna corrispondenza</p></td>\n</tr>\n</table>");

        } else {
            html.append("<table id=\"tabella-report\" border=\"1\">\n<thead>	\n<tr>");

            for (int i = 1; i <= numberOfColumns; i++) {
                String th = rsMd.getColumnLabel(i);
                boolean skip = false;
                for (String hiddenColumn : hiddenColumns) {
                    if (th.equalsIgnoreCase(hiddenColumn)) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    html.append("<th class=\"intestazione\">").append(th).append("</th>");
                }
            }

            html.append("</tr>\n</thead>");

            /*
             * Ciclo sulle righe estratte 
             */
            while (rs.next()) {
                html.append("<tr>");

                for (int i = 1; i <= numberOfColumns; i++) {
                    String strValue = "-";
                    String format = "";

                    // Leggo tutto come Stringa
        
                        if (rsMd.getColumnType(i) == Types.BLOB) {
                            Blob ablob = rs.getBlob(rsMd.getColumnLabel(i));
                            strValue = new String(ablob.getBytes(1l, (int) ablob.length()));
                        }else {
                            strValue = rs.getString(rsMd.getColumnLabel(i));

                            if (strValue == null) {
                                strValue = "-";
                            } else {
                                if (rsMd.getColumnType(i) == Types.NUMERIC) {
                                    strValue = strValue.replaceAll("\\.", ",");//Format.custom("0,00", strValue);
                                    if (strValue.indexOf(",") > 0) {
                                        format = rsMd.getColumnTypeName(i);
                                    }
                                } else {
                                    format = rsMd.getColumnTypeName(i);
                                }
                            }
                        }
                    // UtilitaNew.escapeHTML(strValue)
                    boolean skip = false;
                    /*
                     for(int l = 0; l<hiddenColumns.length; l++){
                     if ( rsMd.getColumnLabel(i).equalsIgnoreCase(hiddenColumns[l])  ){
                     skip = true;
                     break;
                     }
                     }
                     */
                    if (!skip) {
                        html.append("<td class=\"type-").append(format).append("\">").append(strValue.replaceAll("<", "&lt;")).append("</td>");
                    }
                }
                html.append("</tr>");
            }
            html.append("</table></body></html>");

            rs.close();
            ps.close();
        }
        log.info("File Excel READY");
        return html.toString();
    }

}
