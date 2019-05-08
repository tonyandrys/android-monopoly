package io.andrys.monopoly;

/**
 * Game.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

//                   //
// THIS IS DEAD CODE //
//                   //

/**
 * An instance of a game of Monopoly.
 */
public class Game {
    private final String TAG = this.getClass().getSimpleName();

    public Board board;
    public ArrayDeque<Player> players;
    public PropertyManager pm;

    public FullscreenActivity gameActivity;

    public Game(FullscreenActivity activity, Player[] players) {
        this.players = new ArrayDeque<>(Arrays.asList(players));
        this.board = new Board();
        this.pm = new PropertyManager((Context)activity);
        this.gameActivity = activity;
    }

    public void startNewGame() {
        // Add each player's token to the board
        Iterator<Player> itr = players.iterator();
        while (itr.hasNext()) {
            Player p = itr.next();
            board.addPlayerToken(p.getToken());
            Log.v(TAG, String.format("Token ID '%d' added to the board.", p.getToken()));
        }
    }

    /**
     * Rolls the dice and returns the values on each die as an integer array.
     */
    public int[] rollDice() {
        board.rollDice();
        return board.getDiceValues();
    }


}
