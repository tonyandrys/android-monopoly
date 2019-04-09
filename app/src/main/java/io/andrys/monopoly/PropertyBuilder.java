package io.andrys.monopoly;

/**
 * PropertyBuilder.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Example on how to load Property objects from disk, cast them to their correct subclasses,
 * and use overloaded methods.
 *
 *     private void jsonExperiment() {
 *         ArrayList<Property> properties = PropertyBuilder.loadProperties(this);
 *         Log.v(TAG, String.format("Loaded '%d' properties from JSON", properties.size()));
 *         for (int i=0; i<properties.size(); i++) {
 *             if (properties.get(i) instanceof StreetProperty) {
 *                 StreetProperty p = (StreetProperty) properties.get(i);
 *                 @SuppressLint("DefaultLocale") String s = String.format("'%s' => StreetProperty[%s] / pos: '%d', price: '%d', max rent: '%d'", p.getName(), p.getColorGroup(), p.getPosition(), p.getPrice(), p.calculateRentPayment(5));
 *                 Log.v(TAG, s);
 *             } else if (properties.get(i) instanceof RailroadProperty) {
 *                 RailroadProperty p = (RailroadProperty) properties.get(i);
 *                 @SuppressLint("DefaultLocale") String s = String.format("'%s' => RailroadProperty / pos: '%d', price: '%d', max rent: '%d'", p.getName(), p.getPosition(), p.getPrice(), p.calculateRentPayment(4));
 *                 Log.v(TAG, s);
 *             } else if (properties.get(i) instanceof UtilityProperty) {
 *                 UtilityProperty p = (UtilityProperty) properties.get(i);
 *                 @SuppressLint("DefaultLocale") String s = String.format("'%s' => UtilityProperty / pos: '%d', price: '%d', max rent: '%d'", p.getName(), p.getPosition(), p.getPrice(), p.calculateRentPayment(2, 12));
 *                 Log.v(TAG, s);
 *             }
 *         }
 *         Log.v(TAG, "Done!");
 *     }
 */

/**
 * De-serializes property data from JSON & converts each object to its proper Property subclass.
 */
public class PropertyBuilder {
    final private static String TAG = "PropertyBuilder";
    final private static int NUM_PROPERTIES = 28;

    enum PropertyType {STREET, RAILROAD, UTILITY}

    /**
     * Reads property data from disk, converts data to Java representation, and returns
     * all purchasable properties on the board as a set of Property subclasses.
     * @param context A context that can access R.raw
     * @return ArrayList of Properties
     */
    public static ArrayList<Property> loadProperties(Context context) {
        ArrayList<Property> builtProperties = new ArrayList<>(NUM_PROPERTIES);
        ArrayList<JSONObject> propObjs = getPropertyResourcesAsJSON(context);
        Iterator<JSONObject> itr = propObjs.iterator();
        while (itr.hasNext()) {
            // construct this Property
            Property p = buildProperty(itr.next());
            builtProperties.add(p);
        }
        // sort the newly created Properties by their position
        Collections.sort(builtProperties);
        return builtProperties;
    }

    /**
     * Converts a JSON string-encoded property object into the Java-equivalent Property subclass.
     * @param o JSONObject to convert
     * @return a StreetProperty, RailroadProperty, or UtilityProperty containing the data in 'o'.
     */
    private static Property buildProperty(JSONObject o) {
        try {
            // Extract elements common to all Properties
            String name = o.getString("name");
            int price = o.getInt("cost");
            int position = o.getInt("position");

            // map type string to PropertyType enum in order to initialize proper subclass for this property
            PropertyType type = PropertyType.valueOf(o.getString("type"));

            // Street properties have additional parameters that must be extracted before building
            if (type == PropertyType.STREET) {
                JSONArray jRents = o.getJSONArray("rent");
                int developmentCost = o.getInt("house");
                int[] rentValues = new int[] {
                        jRents.getInt(0),
                        jRents.getInt(1),
                        jRents.getInt(2),
                        jRents.getInt(3),
                        jRents.getInt(4),
                        jRents.getInt(5)
                };
                // convert color group to the appropriate enum value
                StreetProperty.ColorGroup color = StreetProperty.ColorGroup.valueOf(o.getString("color"));
                return new StreetProperty(name, price, position, rentValues, developmentCost, color);
            }
            // Railroad and Utility properties can be built w/ just the standard Property fields.
            else if (type == PropertyType.RAILROAD) {
                return new RailroadProperty(name, price, position);
            } else if (type == PropertyType.UTILITY) {
                return new UtilityProperty(name, price, position);
            } else {
                throw new IllegalArgumentException(String.format("buildProperty: failed because parsed JSON object has unexpected type! %s", o.toString()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return new Property();
    }

    /**
     * Reads and parses JSON-encoded property data in /res/raw/; returns each property as a JSONObject.
     * @param context A context that can access R.raw
     * @return list of JSONObjects, one per property in the data file.
     */
    private static ArrayList<JSONObject> getPropertyResourcesAsJSON(Context context) {
        String jStr = RawUtils.readRawResource(context, R.raw.property_data);
        ArrayList<JSONObject> propObjects = new ArrayList<>(NUM_PROPERTIES);
        try {
            JSONArray propertiesJSON = new JSONArray(jStr);
            for (int i=0; i<propertiesJSON.length(); i++) {
                propObjects.add(propertiesJSON.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.e(TAG, "failed to parse JSON property file!", e);
        }
        return propObjects;
    }

}
