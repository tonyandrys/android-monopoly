package io.andrys.monopoly.states;

/**
 * EmptyState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.util.Log;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;

/**
 * Doesn't do anything but wait; use this as a placeholder/TODO state.
 */
public class EmptyState extends GameState {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    public EmptyState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        Log.v(TAG, "onStateEnter()");

    }

    @Override
    public void execute() {
        Log.v(TAG, "onStateEnter()");
        Log.v(TAG, "I'm waaaiittttiiinnggg to do something");
    }

    @Override
    public void onStateExit() {
        Log.v(TAG, "onStateEnter()");

    }

    @Override
    protected void render() {
        Log.v(TAG, "onStateEnter()");

    }
}
