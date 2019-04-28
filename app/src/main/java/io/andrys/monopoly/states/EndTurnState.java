package io.andrys.monopoly.states;

import android.util.Log;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Player;

/**
 * EndTurnState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Finishes the active player's turn, selects the next player to roll, then starts the new player's Roll Dice state.
 *
 * This is where we can remove touch listeners from the active player when we add AI/other human players.
 */
public class EndTurnState extends GameState {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    public EndTurnState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        Log.v(TAG, "onStateEnter()");
    }

    @Override
    public void execute() {
        Log.v(TAG, "execute()");
        gc.players.addLast(gc.activePlayer);
        Player nextPlayer = gc.players.pollFirst();
        Log.v(TAG, String.format("Ending %s's turn; '%s' is up next.", gc.activePlayer.getName(), nextPlayer.getName()));
        GameContext next = new GameContext(gc.board.getDiceValues(), nextPlayer, gc.players, gc.board, gc.pm);
        changeState(new RollDiceState(engine, next));
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
