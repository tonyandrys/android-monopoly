package io.andrys.monopoly;

/**
 * RailroadProperty.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * 4 of the 28 purchasable spaces on a Monopoly board are railroad properties.
 *
 * The rent paid to owners of railroads is determined by the total number of *other* railroad
 * properties that the owner also owns. They cannot be improved with houses or hotels.
 */
public class RailroadProperty extends Property {

    public RailroadProperty(String name, int price, int position) {
        super(name, price, position);
    }

    public int calculateRentPayment(int numRailroadsOwned) {
        return (int)(25*(Math.pow(2, numRailroadsOwned-1)));
    }
}
