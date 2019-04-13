package io.andrys.monopoly;

/**
 * StreetProperty.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * 22 of the 40 spaces on a Monopoly board represents "street" real estate that a player can
 * purchase. A player who lands on one of these spaces can purchase the property, charge rent
 * to other players who land on it, and develop it with houses and hotels to increase rent values.
 *
 * All streets belong to one of eight color groups (StreetProperty.ColorGroup). A player that owns
 * every property in a color group is said to have a monopoly on that group, and can begin developing
 * the spaces in this group.
 *
 * The price of a property, the cost to develop it, and the values of rent the owner may charge
 * varies from property to property.
 */
public class StreetProperty extends Property {
    final private String TAG = this.getClass().getSimpleName();

    /** Values that we can charge for rent dependent on number of houses owned */
    int[] rentValues;

    /** Cost to add a new house or hotel on this property */
    int developmentCost;
    ColorGroup color;

    /**
     * Creates a new StreetProperty
     * @param name Full name of this property as a string
     * @param price Purchase price of this property from the bank
     * @param position Physical position on the board as an integer
     * @param rentValues Array of six rent values, one for each successive level of development
     * @param developmentCost Cost to advance to the next level of development
     * @param colorGroup ColorGroup this property belongs to
     */
    public StreetProperty(String name, int price, int position, int[] rentValues, int developmentCost, ColorGroup colorGroup) {
        super(name, price, position);
        this.rentValues = rentValues;
        this.developmentCost = developmentCost;
        this.color = colorGroup;
    }

    /**
     * Each street-type property is assigned a color; a player that owns all of the streets in a
     * ColorGroup has a "monopoly" on them and can begin to develop them to increase rent values.
     */
    enum ColorGroup {
        PURPLE, LIGHT_BLUE, PINK, ORANGE, RED, YELLOW, GREEN, DARK_BLUE
    }

    /**
     * @return The ColorGroup this property belongs to
     */
    public ColorGroup getColorGroup() {
        return this.color;
    }

    /**
     * @return The cost of adding an additional house or hotel on this property
     */
    public int getDevelopmentCost() {
        return this.developmentCost;
    }

    /**
     * Calculates the amount of rent a non-owner must pay to the owner for landing on
     * this property space at any level of development.
     *
     * A StreetProperty's level of development is equal to the successive number of improvements
     * the owner has made to it. That is:
     *
     * + 0 houses on the property -> development level == 0
     * + 1 houses on the property -> development level == 1
     * + 2 houses on the property -> development level == 2
     * + 3 houses on the property -> development level == 3
     * + 4 houses on the property -> development level == 4
     * + 1 hotel on the property  -> development level == 5
     *
     * @param developmentLevel development level as an integer in [0,5].
     * @return rent value at the passed level of development.
     */
    public int calculateRentPayment(int developmentLevel) {
        int payment = 0;
        if ((developmentLevel >= 0) && (developmentLevel <= 5)) {
            switch (developmentLevel) {
                case 0:
                    payment = this.rentValues[0];
                    break;
                case 1:
                    payment = this.rentValues[1];
                    break;
                case 2:
                    payment = this.rentValues[2];
                    break;
                case 3:
                    payment = this.rentValues[3];
                    break;
                case 4:
                    payment = this.rentValues[4];
                    break;
                case 5:
                    payment = this.rentValues[5];
                    break;
            }
        } else {
            throw new IllegalArgumentException(String.format("StreetProperties have no developmentLevel '%d'!", developmentLevel));
        }
        return payment;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US, "<<'%s' => StreetProperty[%s] / pos: '%d', price: '%d', max rent: '%d'>>", super.getName(), this.color.toString(), super.getPosition(), super.getPrice(), calculateRentPayment(5)
        );
    }
}
