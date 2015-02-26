/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.rockscissorpaper.controller;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ale
 */
@XmlRootElement
public class Choice {

    String type;
    
    public void setType(String type) {
        this.type = type;
    }

    public Choice(String type) {
        this.type = type;
    }
    public String getType(){
        return this.type;
    } 
            
}
