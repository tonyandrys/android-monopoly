package io.andrys.monopoly;

/**
 * PropertyAssignment.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.util.Log;

/**
 * Models the ownership -- or lack of ownership -- of a specific Property in a game.
 * An purchased property has both an owner and a level of development. In the case of street
 * properties, their level of development improves as houses/hotels are built on them. Other
 * properties cannot be developed, so their level cannot be updated (i.e. must be zero).
 */
public class PropertyAssignment {
    private final String TAG = this.getClass().getSimpleName();

    // constant that signifies no token (i.e. no Player) owns this property.
    public static final int NO_OWNER = -1;

    private Property property;
    private int ownerToken;
    private int developmentLevel;

    /**
     * Creates an empty assignment of the Property p.
     * This signifies that the Property p has not been purchased yet.
     * @param p An unowned Property
     */
    public PropertyAssignment(Property p) {
        this.property = p;
        this.ownerToken = NO_OWNER;
        this.developmentLevel = 0;
    }

    /**
     * Creates a new assignment of a Property to a Player.
     * @param p A Property owned by a player in the game
     * @param tokenID tokenID of the Player who purchased this Property
     */
    public PropertyAssignment(Property p, int tokenID) {
        this.property = p;
        this.ownerToken = tokenID;
        this.developmentLevel = 0;
    }

    /**
     * @return Property associated with this assignment relationship
     */
    public Property getProperty() {
        return this.property;
    }

    /**
     * Returns the level of development of the Property associated w/ this assignment relationship
     * if it can be developed (i.e. it is a street property).
     * @return level of development from 0-5.
     */
    public int getDevelopmentLevel() {
        if (isPropertyDevelopable()) {
            return developmentLevel;
        } else {
            return 0;
        }
    }

    /**
     * Checks if the Property in this assignment relationship can be developed.
     * @return true if houses or hotels can be built on this property, false if not.
     */
    public boolean isPropertyDevelopable() {
        if (property instanceof StreetProperty) {
            return true;
        } else {
            return false;
        }
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

    /**
     * Updates the development level of the property associated with this assignment relationship.
     * @param newDevLevel new development level from [0,5].
     */
    public void updateDevelopmentLevel(int newDevLevel) throws IllegalArgumentException {
        if ((newDevLevel < 0) || (newDevLevel > 5)) {
            if (isPropertyDevelopable()) {
                this.developmentLevel = newDevLevel;
                Log.v(TAG, String.format("Property '%s' updated to development level %d", property.getName(), developmentLevel));
            } else {
                throw new IllegalArgumentException(String.format("Property '%s' cannot be developed; cannot update its development level to '%d'!", property.getName(), newDevLevel));
            }
        } else {
            throw new IllegalArgumentException(String.format("Development level parameter '%d' is out of bounds!", newDevLevel));
        }
    }


}
