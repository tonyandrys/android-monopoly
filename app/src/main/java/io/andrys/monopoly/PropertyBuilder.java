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
import java.util.Iterator;

/**
 * De-serializes property data from JSON & converts each object to its proper Property subclass.
 */
public class PropertyBuilder {
    final private static String TAG = this.getClass().getSimpleName();
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
        return builtProperties;
    }

    /**
     * Converts a JSON string-encoded property object into the Java-equivalent Property subclass.
     * @param o JSONObject to convert
     * @return a subclass of Property containing the data in 'o'.
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
            return propObjects;

        } catch (JSONException e) {
            Log.e(TAG, "failed to parse JSON property file!", e);
        }
    }

}
