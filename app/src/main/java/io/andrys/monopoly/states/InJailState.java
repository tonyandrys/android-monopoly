package io.andrys.monopoly.states;

import android.util.Log;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.InJailActionDialogFragment;
import io.andrys.monopoly.exceptions.NotYetImplementedException;

/**
 * InJailState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

public class InJailState extends GameState implements InJailActionDialogFragment.ButtonListener {

    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    public InJailState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {

    }

    @Override
    public void execute() {
        // show the jail action modal
        boolean hasCard = false;
        if (gc.activePlayer.getGetOutOfJailFreeCount() > 0) {
            hasCard = true;
        }
        engine.getActivity().showInJailActionModal(this, hasCard);
        Log.v(TAG, String.format("%s is in jail; presenting jail action modal", gc.activePlayer.getName()));
    }

    @Override
    public void onStateExit() {

    }

    @Override
    protected void render() {

    }

    @Override
    public void onRollButtonClicked(InJailActionDialogFragment df) {
        throw new NotYetImplementedException();
    }

    @Override
    public void onPayButtonClicked(InJailActionDialogFragment df) {
        throw new NotYetImplementedException();
    }

    @Override
    public void onUseCardButtonClicked(InJailActionDialogFragment df) {
        throw new NotYetImplementedException();
    }
}
