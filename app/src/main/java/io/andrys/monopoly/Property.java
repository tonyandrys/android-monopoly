package io.andrys.monopoly;

/**
 * Property.java // Monopoly
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
public class Property {
    final private String TAG = this.getClass().getSimpleName();

    String name;
    /** The price to purchase this property from the bank */
    int price;
    int[] rentValues;
    /** Cost to add a new house or hotel on this property */
    int developmentCost;
    int position;
    PropertyType type;
    ColorGroup color;

    public Property() {
        this.name = "";
        this.price = 0;
        this.rentValues = new int[]{};
        this.developmentCost = 0;
        this.type = null;
        this.color = null;
        this.position = 0;
    }

    public Property(String name, int price, int[] rentValues, int developmentCost, int position, PropertyType type, ColorGroup colorGroup) {
        this.name = name;
        this.price = price;
        this.rentValues = rentValues;
        this.developmentCost = developmentCost;
        this.type = type;
        this.color = colorGroup;
        this.position = position;
    }

    /**
     * Constructs a Property from its JSON representation.
     * Examples of a JSON representation of a Property can be found in res/raw/property_data.json.
     * @param jsonString Stringified JSON object to transform into a Property instance.
     */
    public Property(String jsonString) {
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

            // color group & property type values need to be converted to their appropriate enum values
            this.color = ColorGroup.valueOf(jRoot.getString("color"));
            this.type = PropertyType.valueOf(jRoot.getString("type"));
        } catch (JSONException e) {
            Log.e(TAG, "cannot parse Property object from provided JSON string!", e);
        }
    }

    /**
     * All properties are one of three types:
     *  1) Streets, which belong to one of the eight {@link ColorGroup}s.
     *  2) Railroads, whose rent values increase proportionally to the number of railroads owned by a single player
     *  3) Utilities, whose rent values are calculated by the value of a dice roll.
     */
    enum PropertyType {
        STREET, RAILROAD, UTILITY
    }

    /**
     * Each street-type property is assigned a color; a player that owns all of the streets in a
     * ColorGroup has a "monopoly" on them and can begin to develop them to increase rent values.
     */
    enum ColorGroup {
        PURPLE, LIGHT_BLUE, PINK, ORANGE, RED, YELLOW, GREEN, DARK_BLUE
    }

    /**
     * The amount of cash the owner of this property would receive if they mortgage this property.
     * @return
     */
    public int getMortgagedValue() {
        return Math.round((price/2));
    }

    /**
     * Un-mortgaging a mortgaged property requires the loan be paid back plus 10% interest.
     * @return price in dollars to un-mortgage this property
     */
    public int getCostToUnmortgage() {
        return Math.round((int)(price*1.10));
    }



}
