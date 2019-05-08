package io.andrys.monopoly.states;

/**
 * GameState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.util.Log;

import io.andrys.monopoly.Game;
import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;

/**
 * States must inherit this to be used in the game engine
 */
public abstract class GameState {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    GameEngine engine;

    /** Game Context object, also known as 'gc' from here on out. */
    GameContext gc;

    public GameState(GameEngine gameEngine, GameContext gameContext) {
        this.engine = gameEngine;
        this.gc = gameContext;
    }

    // work to do as we're transitioning to this state
    // like disabling input or whatever
    public abstract void onStateEnter();

    // do the bulk of the work of this state
    public abstract void execute();

    // do cleanup, re-enable buttons, called when this state is on the way out
    public abstract void onStateExit();

    // touch the UI; gameContext contains a reference to the activity you can use I think?
    protected abstract void render();

    /**
     * Create a new GameState w/ updated game context object and pass it to this method
     * to transition to it.
     * @param newState GameState to transition to
     */
    protected void changeState(GameState newState) {
        Log.v(TAG, String.format("=> Trying to transition to new state %s... =>", newState.toString()));
        engine.changeState(newState);
    }

    /**
     * This is just the hash code but shortened so I can append it to things in the
     * small debugging window without breaking lines
     * @return
     */
    public int getShortCode() {
        return (this.hashCode() & 0xFF);

    }

    @Override
    public String toString() {
        return String.format("<%s[%s]>", this.getClass().getSimpleName(), this.getShortCode());
    }
}
