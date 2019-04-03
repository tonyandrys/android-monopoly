package io.andrys.monopoly;

import android.util.SparseIntArray;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Board.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 *
 * The Board contains all of the "physical pieces" of a game of Monopoly.
 */

public class Board {

    Die d1;
    Die d2;
    // maps integers -> integers faster than hashmap does; neat.
    SparseIntArray tokenPositionMap;

    public Board() {
        this.d1 = new Die();
        this.d2 = new Die();
        this.tokenPositionMap = new SparseIntArray();
    }

    /**
     * Adds a token to this board.
     * @param tokenID
     */
    public void addPlayerToken(int tokenID) {
        if (tokenPositionMap.get(tokenID, -1) != -1) {
            throw new IllegalArgumentException(String.format("TokenID '%d' already exists in this game; each player must use a different token!", tokenID));
        }
        tokenPositionMap.append(tokenID, 0);
    }
    /**
     * Moves a player token forward a number of spaces on the board.
     * @param tokenID id of token to move
     * @param p Non-negative integer number of spaces to advance.
     */
    public void incrementTokenPosition(int tokenID, int p) {
        if (p >= 0) {
            int currentPos = tokenPositionMap.get(tokenID, -1);
            if (currentPos != -1) {
                int newPos = (currentPos + p) % 40;
                // replace the existing tokenID -> pos mapping w/ this one, which has the updated position as its value.
                tokenPositionMap.put(tokenID, newPos);
            } else {
                // token does not exist on the board; complain and do nothing
                throw new IllegalArgumentException(String.format("TokenID '%d' does not exist on this board!", tokenID));
            }
        } else {
            throw new IllegalArgumentException("Cannot add a negative value to a token's position!");
        }
    }

    /**
     * Returns the current position of a token on this board.
     * @param tokenID id of token to inspect
     * @return
     */
    public int getTokenPosition(int tokenID) {
        int pos = tokenPositionMap.get(tokenID, -1);
        if (pos == -1) {
            throw new IllegalArgumentException(String.format("TokenID '%d' does not exist on this board!", tokenID));
        }
        return pos;
    }

    public void rollDice() {
        d1.roll();
        d2.roll();
    }

    public int[] getDiceValues() {
        return new int[]{d1.getValue(), d2.getValue()};
    }



}
