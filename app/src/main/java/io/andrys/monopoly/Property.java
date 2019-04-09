package io.andrys.monopoly;

/**
 * Property.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.support.annotation.NonNull;

/**
 * 28 of the 40 spaces on a Monopoly board represent real estate (or "property") that a player can
 * purchase. A player who lands on one of these spaces can purchase the property, charge rent
 * payments to other players who land on it in subsequent rounds, and develop it to increase
 * rent values.
 *
 * The price of a property, the cost to develop on it, and its chargeable amount of rent differ from
 * property to property.
 *
 * We model each property space using a Property object; it encapsulates the different costs/values
 * associated with purchasing/landing on the space in the game.
 */
public class Property implements Comparable<Property>{
    String name;

    /** The price to purchase this property from the bank */
    int price;

    /** This property's position on the board */
    int position;

    public Property() {
        this.name = "";
        this.price = 0;
        this.position = 0;
    }

    public Property(String name, int price, int position) {
        this.name = name;
        this.price = price;
        this.position = position;
    }

    /**
     * @return Display name of this property
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Price to purchase this property from the bank
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * @return Board position that this property corresponds to (0=Go, 39=Boardwalk)
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * @return The amount of cash the owner of this property would receive if they mortgage this property.
     */
    public int getMortgagedValue() {
        return Math.round((int)(price*0.5));
    }

    /**
     * Un-mortgaging a mortgaged property requires the loan be paid back plus 10% interest.
     * @return price in dollars to un-mortgage this property
     */
    public int getCostToUnmortgage() {
        return Math.round((int)(price*1.10));
    }

    /**
     * Define a total ordering over the set of Property objects wrt their position on the board.
     *
     * Property A precedes Property B iff A's position is smaller than B's position
     * Property A equals Property B iff A and B have the same position
     * Property A succeeds Property B iff A's position is larger than B's position
     *
     * @param o Another Property instance to compare against this instance
     * @return negative integer, 0 if equal, or positive integer
     */
    @Override
    public int compareTo(@NonNull Property o) {
        return (this.position - o.getPosition());
    }
}
