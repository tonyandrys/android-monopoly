package io.andrys.monopoly;

/**
 * Player.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.graphics.Color;
import android.util.Log;

/**
 * A Player is a participant in a game controlled by either a human or AI.
 */

public class Player {
    private final String TAG = this.getClass().getSimpleName();

    private String name;        // Name to be displayed in the score panel
    private int balance;        // Cash on hand in dollars
    private int token;          // uid of the token used to represent this player's position on the board.
    private int color;          // color int used next to player on scoreboard, tint owned properties, etc.
    private boolean isInJail;

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
        this.isInJail = false;
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
        int balanceBefore = this.balance;
        this.balance += addVal;
        Log.v(TAG, String.format("incremented %s's balance from %d -> %d", this.name, balanceBefore, this.balance));

    }

    /**
     * Deducts money from a player's cash balance.
     * @param deductVal a positive value to subtract from the player's current balance.
     */
    public void deductFromBalance(int deductVal) {
        int balanceBefore = this.balance;
        this.balance -= deductVal;
        Log.v(TAG, String.format("decremented %s's balance from %d -> %d", this.name, balanceBefore, this.balance));
    }

    /**
     * Returns the ID of the token assigned to this player.
     * @return
     */
    public int getToken() {
        return token;
    }

    public void setIsInJail(boolean isInJail) {
        this.isInJail = isInJail;
        if (this.isInJail) {
            Log.v(TAG, String.format("%s has been thrown in jail.", this.name));
        } else {
            Log.v(TAG, String.format("%s has been released from jail!", this.name));
        }
    }

    public boolean isInJail() {
        return this.isInJail;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", token=" + token +
                ", color=" + color +
                ", isInJail=" + isInJail +
                '}';
    }
}
