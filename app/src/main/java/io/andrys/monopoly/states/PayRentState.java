package io.andrys.monopoly.states;

import android.util.Log;

import java.util.ArrayList;

import io.andrys.monopoly.Board;
import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Player;
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
            rentPayment = prop.calculateRentPayment(gc.pm.getDevelopmentLevelAtPosition(position));
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
                rentPayment = prop.calculateRentPayment(0);
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
        Player owner = null;
        int ownerPlayerIndex = -1; // position of the owner's Player object in the players list

        for(Player p: gc.players) {
            if (p.getToken() == payeeTokenID) {
                owner = p;
            }
            ownerPlayerIndex++;
        }

        if (owner != null) {
            // if the current player can afford this payment, make the transfer
            if (gc.activePlayer.getBalance() >= totalPayment) {
                // modify both balances
                gc.activePlayer.deductFromBalance(totalPayment);
                owner.addToBalance(totalPayment);
                // TODO: START HERE vvv
                // write the updated owner object back into the players list.
                // TODO: ^^^ This is going to be annoying because 'players' is a deque and not a list, so we can't do list.set(idx, newVal) to update an object in place...
            }
            // otherwise, the current player needs to raise money somehow or go bankrupt
            // ...but for now, we're just going to complain to the console...
            else {
                Log.e(TAG, String.format("Can't process rent payment of %d from %s -> %s; %s has insufficient funds (balance=$%d)!", totalPayment, gc.activePlayer.getName(), owner.getName(), gc.activePlayer.getName(), gc.activePlayer.getBalance()));
            }
        } else {
            throw new IllegalStateException(String.format("Can't process rent payment of $%d to token ID '%d'; can't find Player with token ID in the current game!", totalPayment, payeeTokenID));
        }
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
