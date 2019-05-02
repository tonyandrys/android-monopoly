package io.andrys.monopoly;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;

import io.andrys.monopoly.exceptions.UnownedPropertyException;

/**
 * PropertyManager.java // Android Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Provides information about all purchasable properties on the board, keeps track of property
 * ownership, and maintains each property's current development level.
 */
public class PropertyManager {
    private final String TAG = this.getClass().getSimpleName();

    private SparseArray<PropertyAssignment> positionPropertyMap;
    // associates color groups w/ their associated street properties
    private EnumMap<StreetProperty.ColorGroup, ArrayList<StreetProperty>> colorPropertyMap;

    public PropertyManager(Context context) {
        ArrayList<Property> properties = PropertyBuilder.loadProperties(context);
        this.positionPropertyMap = buildPositionPropertyMap(properties);
        this.colorPropertyMap = buildColorPropertyMap(properties);
    }

    /**
     * Builds a mapping of each purchasable board position -> a PropertyAssignment containing
     * both the Property data and owner.
     *
     * Since not all positions on the board are property spaces, this will not return a
     * PropertyAssignment for every valid position on the board.
     *
     * @param properties List of Property objects to map to board positions
     * @return map w/ len(properties) entries s.t.
     *     (pos -> PropertyAssignment(Property.position==pos, owner=null))
     */
    private SparseArray<PropertyAssignment> buildPositionPropertyMap(ArrayList<Property> properties) {
        SparseArray<PropertyAssignment> m = new SparseArray<PropertyAssignment>(properties.size());
        Iterator<Property> itr = properties.iterator();
        while (itr.hasNext()) {
            Property p = itr.next();
            m.put(p.getPosition(), new PropertyAssignment(p));
        }
        return m;
    }

    // Builds a mapping of each StreetProperty ColorGroup to its associated StreetProperties
    private EnumMap<StreetProperty.ColorGroup, ArrayList<StreetProperty>> buildColorPropertyMap(ArrayList<Property> properties) {
        StreetProperty.ColorGroup[] groups = StreetProperty.ColorGroup.values();
        EnumMap<StreetProperty.ColorGroup, ArrayList<StreetProperty>> map = new EnumMap<>(StreetProperty.ColorGroup.class);

        // initialize empty lists for every color group in our mapping
        for (int i=0; i<groups.length; i++) {
            StreetProperty.ColorGroup cg = groups[i];
            if (!map.containsKey(cg)) {
                map.put(cg, new ArrayList<StreetProperty>(3));    // no color group contains more than 3 properties
            }
        }

        // assign each street property to its color group
        Iterator<Property> itr = properties.iterator();
        while (itr.hasNext()) {
            Property p = itr.next();
            if (p instanceof StreetProperty) {
                StreetProperty sp = (StreetProperty)p;
                ArrayList<StreetProperty> props = map.get(sp.getColorGroup());
                props.add(sp);
                map.put(sp.getColorGroup(), props);
            }
        }
        return map;
    }

    // True if p is a valid board position with purchasable property
    private boolean isPositionValid(int p) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else {
            return true;
        }
    }

    /**
     * Returns the Property at a specific position on the board.
     * @param p A board position with purchasable property
     * @return
     */
    public Property inspectProperty(int p) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else {
            return positionPropertyMap.get(p).getProperty();
        }
    }

    /**
     * Returns a list of all StreetProperties in a given ColorGroup.
     * @return ArrayList<StreetProperty>
     */
    public ArrayList<StreetProperty> inspectPropertiesInColorGroup(StreetProperty.ColorGroup group) {
        return colorPropertyMap.get(group);
    }

    /**
     * Interrogates ownership of the Property at a specific position on the board.
     * @param p A board position with purchasable property
     * @return true if owned, false if the bank owns it
     */
    public boolean isPropertyOwned(int p) {
        if (positionPropertyMap.indexOfKey(p) >= 0) {
            return positionPropertyMap.get(p).hasOwner();
        } else {
            return false;
        }
    }

    /**
     * Retrieves the token of the Player that owns the property at a specific position on the board.
     * @param p A board position with purchasable property
     * @return tokenID of the owner
     * @throws UnownedPropertyException if property is unowned
     */
    public int getPropertyOwner(int p) throws UnownedPropertyException {
        int tokenID;
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else if (positionPropertyMap.get(p).getOwnerToken() == PropertyAssignment.NO_OWNER) {
            throw new UnownedPropertyException(String.format("Bank owns property at position '%d'!", p));
            //throw new IllegalArgumentException(String.format("Bank owns property at position '%d'!", p));
        } else {
            tokenID = positionPropertyMap.get(p).getOwnerToken();
        }
        return tokenID;
    }

    /**
     * Assigns ownership to the Property at a specific board position to a player by their tokenID.
     * @param p A board position with purchasable property
     * @param tokenID tokenID of the new owner
     */
    public void assignPropertyToOwner(int p, int tokenID) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else {
            PropertyAssignment pa = positionPropertyMap.get(p);
            pa.updateOwner(tokenID);
            Log.v(TAG, String.format("+ '%s' is now owned by token '%d'.", pa.getProperty().getName(), pa.getOwnerToken()));
        }
    }

    /**
     * Returns the development level of the Property at position p.
     * @param p A board position with purchasable property
     * @return
     */
    public int getDevelopmentLevelAtPosition(int p) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else {
            PropertyAssignment pa = positionPropertyMap.get(p);
            return pa.getDevelopmentLevel();

        }
    }

    /**
     * Updates the development level of the Property at position p if it can be developed.
     * @param p A board position with purchasable property
     */
    public void updateDevelopmentLevelAtPosition(int p, int newDevLevel) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else {
            PropertyAssignment pa = positionPropertyMap.get(p);
            if (pa.isPropertyDevelopable()) {
                pa.updateDevelopmentLevel(newDevLevel);
            } else {
                throw new IllegalArgumentException(String.format("Cannot modify development level of '%s'; property cannot be developed!", pa.getProperty().getName()));
            }
        }
    }

}
