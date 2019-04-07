package io.andrys.monopoly;

/**
 * Player.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * A Player is a participant in a game controlled by either a human or AI.
 */

public class Player {
    private String name;    // Name to be displayed in the score panel
    private int balance;    // Cash on hand in dollars
    private int token;      // uid of the token used to represent this player's position on the board.
    // To implement: list of owned properties & collectibles like get out of jail free cards

    /**
     * Creates a new player that starts w/ $1500 (rulebook standard amount for a new player).
     * @param name Display name for this new player
     */
    public Player(String name, int token) {
        this.name = name;
        this.balance = 1500;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    /**
     * Adds money to a player's cash balance
     * @param addVal a positive value to add to the player's current balance.
     */
    public void addToBalance(int addVal) {
        this.balance += addVal;
    }

    /**
     * Deducts money from a player's cash balance.
     * @param deductVal a positive value to subtract from the player's current balance.
     */
    public void deductFromBalance(int deductVal) {
        this.balance -= deductVal;
    }

    /**
     * Returns the ID of the token assigned to this player.
     * @return
     */
    public int getToken() {
        return token;
    }


}
