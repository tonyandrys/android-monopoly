package io.andrys.monopoly.states;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

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
import io.andrys.monopoly.exceptions.UnownedPropertyException;

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
                try {
                    if (gc.pm.getPropertyOwner(p.getPosition()) != ownerTokenID) {
                        isMonopoly = false;
                    }
                } catch (UnownedPropertyException e) {
                    // an unowned associated property is a guarantee that the owner has no monopoly here.
                    break;
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
        Log.v(TAG, String.format("%s owes %s $%d for landing on %s.", gc.activePlayer.getName(), ownerTokenID, rentPayment, prop.getName()));
        return rentPayment;
    }

    private int calculateRailroadRent(int ownerTokenID, RailroadProperty prop) {
        int ownerRailroadCount = 0;

        // figure out how many railroads the payee owns
        for (int i=0; i<gc.board.POSITIONS_RAILROADS.length; i++) {
            int rp = gc.board.POSITIONS_RAILROADS[i];
            try {
                if ((gc.pm.isPropertyOwned(rp)) && (gc.pm.getPropertyOwner(rp) == ownerTokenID)) {
                    ownerRailroadCount++;
                }
            } catch (UnownedPropertyException e) {
                // an unowned railroad is the same as a railroad owned by someone else; don't increment the count here.
                continue;
            }
        }

        // calculate railroad rent payment
        int rentPayment = prop.calculateRentPayment(ownerRailroadCount);
        Log.v(TAG, String.format("%s owes %s $%d for landing on %s (owns %d railroads total).", gc.activePlayer.getName(), ownerTokenID, rentPayment, prop.getName(), ownerRailroadCount));
        return rentPayment;
    }

    private int calculateUtilityRent(int ownerTokenID, UtilityProperty prop) {
        int ownerUtilityCount = 0;

        // figure out how many utilities the payee owns
        for (int i=0; i<gc.board.POSITIONS_UTILITIES.length; i++) {
            int up = gc.board.POSITIONS_UTILITIES[i];
            try {
                if ((gc.pm.isPropertyOwned(up)) && (gc.pm.getPropertyOwner(up) == ownerTokenID)) {
                    ownerUtilityCount++;
                }
            } catch (UnownedPropertyException e) {
                // an unowned utility is the same as a utility owned by someone else; don't increment the count here.
                continue;
            }

        }
        int diceTotal = gc.board.getDiceSum();
        int rentPayment = prop.calculateRentPayment(ownerUtilityCount, diceTotal);
        Log.v(TAG, String.format("%s owes %s $%d for landing on %s (%s owns %d utilities; dice sum=%d).", gc.activePlayer.getName(), ownerTokenID, rentPayment, prop.getName(), ownerTokenID, ownerUtilityCount, gc.board.getDiceSum()));
        return rentPayment;
    }

    // Transfers 'totalPayment' dollars to a different Player designated by their token id.
    private void payRentToPlayer(int totalPayment, int payeeTokenID) {
        Player[] players = gc.players.toArray(new Player[]{});

        // get a reference to the Player object of the payee; keep track of its position in the list for updating it later.
        Player payee = null;
        int payeePlayerIndex = -1; // position of the owner's Player object in the players list
        for (int i=0; i<players.length; i++) {
            payeePlayerIndex = i;
            Player p = players[i];
            if (p.getToken() == payeeTokenID) {
                payee = p;
                break;
            }
        }

        if (payee != null) {
            // if the current player can afford this payment, make the transfer
            if (gc.activePlayer.getBalance() >= totalPayment) {
                // modify both balances
                gc.activePlayer.deductFromBalance(totalPayment);
                payee.addToBalance(totalPayment);


                // Update the score table to reflect the changed balances of both players involved in this transaction
                scoreTable.updatePlayerBalance(gc.activePlayer, gc.activePlayer.getBalance());
                scoreTable.updatePlayerBalance(payee, payee.getBalance());

                // generate an updated deque of Players including the updated payee Player object
                Log.v(TAG, String.format("players[%d]=%s <- %s", payeePlayerIndex, players[payeePlayerIndex].toString(), payee.toString()));
                players[payeePlayerIndex] = payee;
                ArrayDeque<Player> updatedPlayers = new ArrayDeque<>(Arrays.asList(players));

                // end the player's turn
                GameContext next = new GameContext(gc.board.getDiceValues(), gc.activePlayer, updatedPlayers, gc.board, gc.pm);
                changeState(new EndTurnState(engine, next));
            }
            // otherwise, the current player needs to raise money somehow or go bankrupt
            // ...but for now, we're just going to complain to the console...
            else {
                Log.e(TAG, String.format("Can't process rent payment of %d from %s -> %s; %s has insufficient funds (balance=$%d)!", totalPayment, gc.activePlayer.getName(), payee.getName(), gc.activePlayer.getName(), gc.activePlayer.getBalance()));
            }
        } else {
            throw new IllegalStateException(String.format("Can't process rent payment of $%d to token ID '%d'; can't find Player with token ID in the current game!", totalPayment, payeeTokenID));
        }
    }


    @Override
    public void onStateEnter() {
        scoreTable = engine.getActivity().findViewById(R.id.score_table_tl);
    }

    @Override
    public void execute() {
        int position = gc.board.getTokenPosition(gc.activePlayer.getToken());
        Board.SpaceType spaceType = gc.board.getSpaceTypeForPosition(position);
        Property prop = gc.pm.inspectProperty(position);
        int propertyOwnerID;
        try {
            propertyOwnerID = gc.pm.getPropertyOwner(prop.getPosition());
        } catch (UnownedPropertyException e) {
            throw new IllegalStateException("Can't calculate rent for unowned property!");
        }

        // property type dictates the way we should calculate rent owed
        int rentPayment = 0;
        if (prop instanceof StreetProperty) {
            rentPayment = calculateStreetRent(propertyOwnerID, (StreetProperty) prop);
        } else if (prop instanceof RailroadProperty) {
            rentPayment = calculateRailroadRent(propertyOwnerID, (RailroadProperty) prop);
        } else if (prop instanceof UtilityProperty) {
            rentPayment = calculateUtilityRent(propertyOwnerID, (UtilityProperty) prop);
        }
        payRentToPlayer(rentPayment, propertyOwnerID);


    }

    @Override
    public void onStateExit() {

    }

    @Override
    protected void render() {

    }
}
