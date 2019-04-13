package io.andrys.monopoly;

/**
 * GameContext.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.content.Context;

import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * An instance of a game of Monopoly.
 */
public class GameContext {
    private final String TAG = this.getClass().getSimpleName();

    public Board board;
    public ArrayDeque<Player> players;
    public Player activePlayer;
    public PropertyManager pm;
    int[] diceValues;

    public GameContext(int[] diceVals, Player activePlayer, ArrayDeque<Player> players, Board board, PropertyManager pm) {
        this.players = players;
        this.activePlayer = activePlayer;
        this.board = board;
        this.pm = pm;
        this.diceValues = diceVals;
    }

}
