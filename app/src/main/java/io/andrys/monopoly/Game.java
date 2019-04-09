package io.andrys.monopoly;

/**
 * Game.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * An instance of a game of Monopoly.
 */
public class Game {
    private final String TAG = this.getClass().getSimpleName();

    private Board board;
    private ArrayDeque<Player> players;

    public Game(Player[] players) {
        this.players = new ArrayDeque<>(Arrays.asList(players));
        this.board = new Board();
    }


}
