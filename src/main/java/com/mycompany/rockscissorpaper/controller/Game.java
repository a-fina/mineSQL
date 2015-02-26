/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rockscissorpaper.controller;

import com.mycompany.rockscissorpaper.model.Player;
import java.util.List;

/**
 *
 * @author ale
 */
public class Game {

    String rules;

    public Game(String rules) {
        this.rules = rules;
    }

    /*
     * Choose winner
     *
     * @return Player winner
     */
    public Player play(List<Player> players) {

        return players.get(1);
    }
    /*
    * Morra cinese engage rules
    *
    */
    public boolean win(String type1, String type2 ){
        if (type1.equals("rock") && type2.equals("scissor") )
            return true;
        else if (type1.equals("scissor") && type2.equals("paper") )
            return true;
        else if (type1.equals("paper") && type2.equals("rock") )
            return true;
        return true;
    }

    public Choice whoWin(Choice c1, Choice c2) {
        if (win(c1.getType(), c2.getType())) {
            return c1;
        } else {
            return c2;
        }
    }
}
