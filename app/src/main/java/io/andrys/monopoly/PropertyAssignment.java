package io.andrys.monopoly;

/**
 * PropertyAssignment.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Models the ownership -- or lack of ownership -- of a specific Property in a game.
 */
public class PropertyAssignment {
    private final String TAG = this.getClass().getSimpleName();

    // constant that signifies no token (i.e. no Player) owns this property.
    public static final int NO_OWNER = -1;

    private Property property;
    private int ownerToken;

    /**
     * Creates an empty assignment of the Property p.
     * This signifies that the Property p has not been purchased yet.
     * @param p An unowned Property
     */
    public PropertyAssignment(Property p) {
        this.property = p;
        this.ownerToken = NO_OWNER;
    }

    /**
     * Creates a new assignment of a Property to a Player.
     * @param p A Property owned by a player in the game
     * @param tokenID tokenID of the Player who purchased this Property
     */
    public PropertyAssignment(Property p, int tokenID) {
        this.property = p;
        this.ownerToken = tokenID;
    }

    /**
     * @return Property associated with this assignment relationship
     */
    public Property getProperty() {
        return this.property;
    }

    /**
     * @return true if this Property is owned by another player, false if it is owned by the bank
     */
    public boolean hasOwner() {
        if (this.ownerToken != NO_OWNER) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return tokenID of the owner of this property
     */
    public int getOwnerToken() {
        return ownerToken;
    }

    /**
     * Updates the owner of this Property, overwriting the existing owner if there is one.
     * @param newOwnerTokenID tokenID of the Player who should be assigned ownership of this Property
     */
    public void updateOwner(int newOwnerTokenID) {
        this.ownerToken = newOwnerTokenID;
    }

}
