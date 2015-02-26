/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.rockscissorpaper.model;

import com.mycompany.rockscissorpaper.controller.Choice;
import java.util.Random;

/**
 *
 * @author ale
 */
public class Player {
    /*
    * 
    */
    String type;
    Choice choise;

    
    public Player(String type) {
        this.type = type;
    }

    public Player() {
   }

    /*
    * Read player choice from many sources
    */
    public Choice readChoice(/*String channel*/) {
        /*TODO 
            aggiungere parametro ingresso String channel
        */
        
        this.choise = new Choice("scissor");

        return this.choise;
    }

    public Choice randomChoice() {
       String[] choiceList = {"scissor","paper","rock"};

       Random randomGenerator = new Random();
       int r = randomGenerator.nextInt(3);
       
        this.choise = new Choice(choiceList[r]);
        
        return this.choise;
    }

    public Choice getChoise() {
        return choise;
    }

        
}
