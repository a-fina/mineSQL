/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alessio.finamore
 */
@XmlRootElement
public class Data {
    private int roll;
    private String name;


    public int getRoll() {
        return roll;
    }
    public void setRoll(int roll) {
        this.roll = roll;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
