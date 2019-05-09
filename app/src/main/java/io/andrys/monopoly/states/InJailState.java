package io.andrys.monopoly.states;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;

/**
 * InJailState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

public class InJailState extends GameState {

    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    public InJailState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void onStateExit() {

    }

    @Override
    protected void render() {

    }
}
