package io.andrys.monopoly;

/**
 * Player.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.graphics.Color;

/**
 * A Player is a participant in a game controlled by either a human or AI.
 */

public class Player {
    private String name;    // Name to be displayed in the score panel
    private int balance;    // Cash on hand in dollars
    private int token;      // uid of the token used to represent this player's position on the board.
    private int color;      // color int used next to player on scoreboard, tint owned properties, etc.

    /**
     *
     * @param name
     *
     */

    /**
     * Creates a new player that starts w/ $1500 (rulebook standard amount for a new player).
     * @param name      Display name for this new player
     * @param token     ID of the token assigned to this user
     * @param colorStr  color selected by this player as a "#AARRBBGG" string
     */
    public Player(String name, int token, String colorStr) {
        this.name = name;
        this.balance = 1500;
        this.token = token;
        this.color = Color.parseColor(colorStr);
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public int getOpaqueColor() {
        return color;
    }

    /**
     * Transparent color is just the Player's selected color w/ alpha channel
     * at approx 30% (75 in decimal)
     * @return transparent color variation as a color int
     */
    public int getTransparentColor() {
        return Color.argb(75, Color.red(color), Color.green(color), Color.blue(color));
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
