package io.andrys.monopoly;

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

    public Board() {
        this.d1 = new Die();
        this.d2 = new Die();
    }

    public int[] rollDice() {
        d1.roll();
        d2.roll();
        return new int[]{d1.getValue(), d2.getValue()};
    }

}
