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

    public Choice whoWin(Choice c1, Choice c2) {

        if (c1.win(c2.getType())) {
            return c1;
        } else {
            return c2;
        }
    }
}
