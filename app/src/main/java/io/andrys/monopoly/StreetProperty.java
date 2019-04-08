package io.andrys.monopoly;

/**
 * StreetProperty.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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



    // TODO: MOVE THIS OUT AND INTO A "PROPERTYBUILDER" TYPE STATIC METHOD vvvvv
    /**
     * Constructs a Property from its JSON representation.
     * Examples of a JSON representation of a Property can be found in res/raw/property_data.json.
     * @param jsonString Stringified JSON object to transform into a Property instance.
     */
    public StreetProperty(String jsonString) {
        try {
            // extract json structures from the input string
            JSONObject jRoot = new JSONObject(jsonString);
            JSONArray jRents = jRoot.getJSONArray("rent");
            this.name = jRoot.getString("name");
            this.price = jRoot.getInt("cost");
            this.position = jRoot.getInt("position");
            this.developmentCost = jRoot.getInt("house");
            this.rentValues = new int[] {
                    jRents.getInt(0),
                    jRents.getInt(1),
                    jRents.getInt(2),
                    jRents.getInt(3),
                    jRents.getInt(4),
                    jRents.getInt(5)
            };
            // color group should be converted to its appropriate enum value
            this.color = ColorGroup.valueOf(jRoot.getString("color"));
        } catch (JSONException e) {
            Log.e(TAG, "cannot parse Property object from provided JSON string!", e);
        }
    }
    // TODO: MOVE THIS OUT AND INTO A "PROPERTYBUILDER" TYPE STATIC METHOD ^^^^^^







}
