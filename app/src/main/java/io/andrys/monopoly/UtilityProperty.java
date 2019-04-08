package io.andrys.monopoly;

/**
 * UtilityProperty.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * 2 of the 28 purchasable spaces on a Monopoly board are utilities.
 *
 * The rent paid to owners of railroads is determined by *both* the total number of utilities
 * owned by the owner and the sum of both die values.
 */
public class UtilityProperty extends Property {

    public UtilityProperty(String name, int price, int position) {
        super(name, price, position);
    }

    /**
     *
     * @param numUtilitiesOwned 2 if owner owns both utilities, 1 otherwise.
     * @param diceTotal sum of the values on each die. Ex: 3,5 -> diceTotal == 8
     * @return amount of rent owed to the owner of this property.
     */
    public int calculateRentPayment(int numUtilitiesOwned, int diceTotal) {
        if ((diceTotal <= 0) || (diceTotal > 12)) {
            throw new IllegalArgumentException(String.format("dice total of '%d' is out of range!", diceTotal));
        }
        if (numUtilitiesOwned == 1) {
            return (diceTotal*4);
        } else if (numUtilitiesOwned == 2) {
            return (diceTotal*10);
        } else {
            throw new IllegalArgumentException(String.format("Invalid number of utilities owned ('%d'); must be either 1 or 2!", numUtilitiesOwned));
        }
    }

}
