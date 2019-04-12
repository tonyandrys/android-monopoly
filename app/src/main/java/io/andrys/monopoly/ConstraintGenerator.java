package io.andrys.monopoly;

/**
 * ConstraintGenerator.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;

/**
 * A singleton that calculates constraints between related Views.
 *
 * UPDATE: UNFORTUNATELY, I guess we can't iterate over an existing ConstraintSet or concatenate the two,
 * so an *existing* ConstraintSet must be passed to every method in this class so the newly
 * calculated constraints can be added to it.
 *
 * This is ugly and tightly couples this class to pretty much only Activity subclasses but I'm
 * just going to have to live with it I guess.
 */
public class ConstraintGenerator {
    private final String TAG = this.getClass().getSimpleName();

    private static ConstraintGenerator instance = null;

    private ConstraintGenerator() {}

    public static ConstraintGenerator getInstance() {
        if (instance == null) {
            instance = new ConstraintGenerator();
        }
        return instance;
    }

    /**
     * Builds a set of constraints sufficient to constrain a new house IV within a property among
     * other houses that may exist.
     * @param newHouseID        ID of the new house's ImageView
     * @param existingHouseIDs  IDs of existing house ImageViews. If no other houses are on this property, pass int[0].
     * @param propID            ID of the enclosing property's ImageView
     * @param propertyBoardSide Side of the board the enclosing property is on
     * @param cs                ConstraintSet to add the newly calculated Constraints to
     * @return
     */
    public ConstraintSet calculateHouseConstraints(int newHouseID, @NonNull int[] existingHouseIDs, int propID, VisualAssetManager.BoardSide propertyBoardSide, ConstraintSet cs) {
        // houses on top and bottom will have identical constraints (i think??)
        if ((propertyBoardSide == VisualAssetManager.BoardSide.BOTTOM) || (propertyBoardSide == VisualAssetManager.BoardSide.TOP)) {
            // add constraints to keep this house within the property it stands on
            cs.connect(newHouseID, ConstraintSet.BOTTOM, propID, ConstraintSet.BOTTOM);        // house bottom -> property bottom
            cs.connect(newHouseID, ConstraintSet.TOP, propID, ConstraintSet.TOP);              // house top -> property top
            // determine which other View the house should be anchored against
            if (existingHouseIDs.length > 0) {
                // anchor this house against the most recently drawn house
                int anchorHouseID = existingHouseIDs[existingHouseIDs.length-1];
                cs.connect(newHouseID, ConstraintSet.START, anchorHouseID, ConstraintSet.END);
            } else {
                // anchor house against starting edge of property
                cs.connect(newHouseID, ConstraintSet.START, propID, ConstraintSet.START);
            }
            // vertical bias constrains houses to stay within the colored stripe of the property
            if (propertyBoardSide == VisualAssetManager.BoardSide.BOTTOM) {
                cs.setVerticalBias(newHouseID, 0.05f);                                    // vertical align within the top 5% of the property
            } else {
                cs.setVerticalBias(newHouseID, 0.95f);                                    // vertical align within the bottom 5% of the property
            }
        }

        // houses on left and right side of board will have identical constraints (I think???)
        else if ((propertyBoardSide == VisualAssetManager.BoardSide.LEFT) || (propertyBoardSide == VisualAssetManager.BoardSide.RIGHT)) {
            cs.connect(newHouseID, ConstraintSet.START, propID, ConstraintSet.START);
            cs.connect(newHouseID, ConstraintSet.END, propID, ConstraintSet.END);
            if (existingHouseIDs.length > 0) {
                // anchor this house against the most recently drawn house
                int anchorHouseID = existingHouseIDs[existingHouseIDs.length-1];
                cs.connect(newHouseID, ConstraintSet.TOP, anchorHouseID, ConstraintSet.BOTTOM);
            } else {
                cs.connect(newHouseID, ConstraintSet.TOP, propID, ConstraintSet.TOP);
            }
            // horizontal bias constrains houses to stay within the colored stripe of the property
            if (propertyBoardSide == VisualAssetManager.BoardSide.LEFT) {
                cs.setHorizontalBias(newHouseID, 0.95f);
            } else {
                cs.setHorizontalBias(newHouseID, 0.05f);
            }
        }
    return cs;
    }
}
