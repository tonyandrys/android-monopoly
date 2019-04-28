package io.andrys.monopoly.states;

import android.util.Log;

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

    private void payStreetRent(StreetProperty prop) {
        throw new NotYetImplementedException("Implement street property rent calculation!");
    }

    private void payRailroadRent(RailroadProperty prop) {
        throw new NotYetImplementedException("Implement railroad rent calculation!");
    }

    private void payUtilityRent(UtilityProperty prop) {
        throw new NotYetImplementedException("Implement utility rent calculation!");
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

        // property type dictates the way we should calculate rent owed
        if (prop instanceof StreetProperty) {
            payStreetRent((StreetProperty) prop);
        } else if (prop instanceof RailroadProperty) {
            payRailroadRent((RailroadProperty) prop);
        } else if (prop instanceof UtilityProperty) {
            payUtilityRent((UtilityProperty) prop);
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
