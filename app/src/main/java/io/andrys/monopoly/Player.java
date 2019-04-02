package io.andrys.monopoly;

/**
 * null.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * A Player is a participant in a game controlled by either a human or AI.
 */

public class Player {
    private String name;    // Name to be displayed in the score panel
    private int balance;    // Cash on hand in dollars
    private int position;   // current position on the board from [0,39], where 0 is Go and 39 is Boardwalk
    private int token;      // maps to a drawable managed by VisualAssetManager used to represent the player on the board.
    // To implement: list of owned properties & collectibles like get out of jail free cards

    /**
     * Creates a new player that starts w/ $1500 (rulebook standard amount for a new player) at Go.
     * @param name Display name for this new player
     */
    public Player(String name, int token) {
        this.name = name;
        this.balance = 1500;
        this.position = 0;
        this.token = token;
    }

    /**
     * Moves this player a non-negative integer number of spaces forward on the board.
     * @param p
     */
    public void incrementBoardPosition(int p) {
        if (!(p >= 0)) { throw new IllegalArgumentException("Cannot add a negative value to a player's position! Use the setter instead."); }
        this.position = (this.position + p) % 40;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public int getBoardPosition() {
        return position;
    }

    public int getToken() {
        return token;
    }


}
