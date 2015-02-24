/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.rockscissorpaper.controller;

/**
 *
 * @author ale
 */
public class Choice {

    String type;
    
    public Choice(String type) {
        this.type = type;
    }
    public String getType(){
        return this.type;
    } 
            
    public boolean win(String aType ){
        if (this.type.equals("rock") && aType.equals("scissor") )
            return true;
        else if (this.type.equals("scissor") && aType.equals("paper") )
            return true;
        else if (this.type.equals("paper") && aType.equals("rock") )
            return true;
        return true;
    }
    
}
