package io.andrys.monopoly.states;

import android.util.Log;

import java.util.ArrayList;

import io.andrys.monopoly.Board;
import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Property;
import io.andrys.monopoly.R;
import io.andrys.monopoly.RailroadProperty;
import io.andrys.monopoly.ScoreTableLayout;
import io.andrys.monopoly.StreetProperty;
import io.andrys.monopoly.UtilityProperty;
import io.andrys.monopoly.exceptions.NotYetImplementedException;

/**
 * PayRentState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Using the active player's *current* position on the board, calculates the amount of rent owed,
 * the Player to whom rent is owed, and modifies the balances of both Players to reflect the transaction.
 */
public class PayRentState extends GameState {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    private ScoreTableLayout scoreTable;

    public PayRentState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    private int calculateStreetRent(int ownerTokenID, StreetProperty prop) {
        // figure out how much rent the active player owes to the owner
        int rentPayment = 0;
        int position = prop.getPosition();
        int currLevel = gc.pm.getDevelopmentLevelAtPosition(position);
        if (currLevel > 0) {
            // pay the price dictated by # of houses and/or hotel
            rentPayment = gc.pm.getDevelopmentLevelAtPosition(position);
        } else {
            // TODO: if needed again, this logic should be moved into PropertyManager in a isPropertyAMonopoly(position)-type method
            // determine whether this is an undeveloped monopoly or not.
            ArrayList<StreetProperty> assocProperties = gc.pm.inspectPropertiesInColorGroup(prop.getColorGroup());
            assocProperties.remove(prop);
            boolean isMonopoly = true;
            for(StreetProperty p: assocProperties) {
                if (gc.pm.getPropertyOwner(position) != ownerTokenID) {
                    isMonopoly = false;
                }
            }
            if (isMonopoly) {
                // pay double the basic rent
                rentPayment = prop.calculateRentPayment(0) * 2;
            } else {
                // pay basic rent
                rentPayment = prop.calculateRentPayment(0) * 2;
            }
        }
        return rentPayment;
    }

    private int calculateRailroadRent(int ownerTokenID, RailroadProperty prop) {
        throw new NotYetImplementedException("Implement railroad rent calculation!");
    }

    private int calculateUtilityRent(int ownerTokenID, UtilityProperty prop) {
        throw new NotYetImplementedException("Implement utility rent calculation!");
    }

    // Attempts to transfer 'totalPayment' dollars to a different Player designated by their token id.
    private int payRentToPlayer(int totalPayment, int payeeTokenID) {

    }


    @Override
    public void onStateEnter() {
        Log.v(TAG, "onStateEnter()");
        scoreTable = engine.getActivity().findViewById(R.id.score_table_tl);
    }

    @Override
    public void execute() {
        Log.v(TAG, "execute()");
        int position = gc.board.getTokenPosition(gc.activePlayer.getToken());
        Board.SpaceType spaceType = gc.board.getSpaceTypeForPosition(position);
        Property prop = gc.pm.inspectProperty(position);
        int propertyOwnerID = gc.pm.getPropertyOwner(prop.getPosition());

        // property type dictates the way we should calculate rent owed
        int rentPayment = 0;
        if (prop instanceof StreetProperty) {
            rentPayment = calculateStreetRent(propertyOwnerID, (StreetProperty) prop);
        } else if (prop instanceof RailroadProperty) {
            rentPayment = calculateRailroadRent(propertyOwnerID, (RailroadProperty) prop);
        } else if (prop instanceof UtilityProperty) {
            rentPayment = calculateUtilityRent(propertyOwnerID, (UtilityProperty) prop);
        }


    }

    @Override
    public void onStateExit() {
        Log.v(TAG, "onStateExit()");

    }

    @Override
    protected void render() {
        Log.v(TAG, "render()");

    }
}
