/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minesql.excel;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alessio.finamore
 */
public class CreateExcel {
    
    public CreateExcel() {

        Excel tsExcel = new Excel();
        String excel = tsExcel.getFile(con, query, hiddenColumns);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
