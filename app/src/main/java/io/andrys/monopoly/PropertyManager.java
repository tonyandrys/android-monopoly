package io.andrys.monopoly;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * PropertyManager.java // Android Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */
public class PropertyManager {
    private final String TAG = this.getClass().getSimpleName();
    private SparseArray<PropertyAssignment> positionPropertyMap;

    public PropertyManager(Context context) {
        ArrayList<Property> properties = PropertyBuilder.loadProperties(context);
        this.positionPropertyMap = buildPositionPropertyMap(properties);
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
     * Interrogates ownership of the Property at a specific position on the board.
     * @param p A board position with purchasable property
     * @return true if owned, false if the bank owns it
     */
    public boolean isPropertyOwned(int p) {
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else {
            return positionPropertyMap.get(p).hasOwner();
        }
    }

    /**
     * Retrieves the token of the Player that owns the property at a specific position on the board.
     * @param p A board position with purchasable property
     * @return tokenID of the owner
     */
    public int getPropertyOwner(int p) {
        int tokenID;
        if ((p < 0) || (p > 39)) {
            throw new IllegalArgumentException(String.format("Invalid board position '%d'!", p));
        } else if (positionPropertyMap.get(p) == null) {
            throw new IllegalArgumentException(String.format("No purchasable Property at position '%d'!", p));
        } else if (positionPropertyMap.get(p).getOwnerToken() == PropertyAssignment.NO_OWNER) {
            throw new IllegalArgumentException(String.format("Bank owns property at position '%d'!", p));
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

}
