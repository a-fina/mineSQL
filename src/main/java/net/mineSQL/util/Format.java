/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.util;

import java.text.DecimalFormat;

/**
 *
 * @author alessio.finamore
 */
public class Format {

    static public String custom(String pattern, String number) 
    {

        double value = new Double(number);
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }
}
