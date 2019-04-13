package io.andrys.monopoly;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.widget.Space;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Board.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 *
 * The Board contains all of the "physical pieces" of a game of Monopoly.
 */

// TODO: When you get there, you'll probably need to add RAILROAD and UTILITY to this list of enums.
enum SpaceType {
    PROPERTY, CHANCE, COMMUNITY_CHEST, INCOME_TAX, LUXURY_TAX, GO, JAIL, FREE_PARKING, GO_TO_JAIL;
}

public class Board {

    private final String TAG = this.getClass().getSimpleName();

    /** There are 40 different spaces on a Monopoly board that a player can land on. */
    private final int NUM_BOARD_POSITIONS = 40;
    private final int[] POSITIONS_CHANCE = {7,22,36};
    private final int[] POSITIONS_CCHEST = {2,17,33,36};
    private final int[] POSITIONS_TAX = {4,38};
    private final int[] POSITIONS_CORNER = {0,10,20,30};

    /** Keeps track of each token's position on the board by its tokenID. */
    private SparseIntArray tokenPositionMap;
    /** Mapping of each position on the board to its SpaceType. */
    private SparseArray<SpaceType> positionTypeMap;

    private Die d1;
    private Die d2;

    public Board() {
        this.d1 = new Die();
        this.d2 = new Die();
        this.tokenPositionMap = new SparseIntArray();
        this.positionTypeMap = buildSpaceTypeMap();
    }

    /**
     * Constructs the (board position -> SpaceType) mapping which contains one element each of the 40 spaces on the board.
     * Use this to determine what kind of space a token has landed on in O(1).
     * @return SparseArray<SpaceType> positionTypeMap
     */
    private SparseArray<SpaceType> buildSpaceTypeMap() {
        SparseArray<SpaceType> positionTypeMap = new SparseArray<>(NUM_BOARD_POSITIONS);

        // iterate over each non-property category position to determine which type to assign each position.
        for (int i=0; i<NUM_BOARD_POSITIONS; i++) {
            if (Arrays.binarySearch(POSITIONS_CHANCE, i) >= 0) {
                positionTypeMap.append(i, SpaceType.CHANCE);
            } else if (Arrays.binarySearch(POSITIONS_CCHEST, i) >= 0) {
                positionTypeMap.append(i, SpaceType.COMMUNITY_CHEST);
            } else if (Arrays.binarySearch(POSITIONS_TAX, i) >= 0) {
                if (i == 4) {
                    positionTypeMap.append(i, SpaceType.INCOME_TAX);
                } else {
                    positionTypeMap.append(i, SpaceType.LUXURY_TAX);
                }
            } else if (Arrays.binarySearch(POSITIONS_CORNER, i) >= 0) {
                // I tried as hard as I could to eliminate magic numbers here
                //  w/out adding unnecessary bulk to this class but I just couldn't swing it
                if (i == 0) {
                    positionTypeMap.append(i, SpaceType.GO);
                } else if (i == 10) {
                    positionTypeMap.append(i, SpaceType.JAIL);
                } else if (i == 20) {
                    positionTypeMap.append(i, SpaceType.FREE_PARKING);
                } else if (i == 30) {
                    positionTypeMap.append(i, SpaceType.GO_TO_JAIL);
                }
            }
            // values not caught already are definitely properties.
            else {
                positionTypeMap.append(i, SpaceType.PROPERTY);
            }
        }
        return positionTypeMap;
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

    /**
     * Returns the type of space that exists at board position 'p'.
     * @param p the board position whose type should be interrogated
     * @return the SpaceType of position 'p'.
     */
    public SpaceType getSpaceTypeForPosition(int p) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("'%d' is an invalid board position! A valid board position is in [0,39].", p));
        } else {
            return positionTypeMap.get(p);
        }
    }

    public void rollDice() {
        d1.roll();
        d2.roll();
    }

    public int[] getDiceValues() {
        return new int[]{d1.getValue(), d2.getValue()};
    }





}
