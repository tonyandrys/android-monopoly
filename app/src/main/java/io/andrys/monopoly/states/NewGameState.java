package io.andrys.monopoly.states;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;

import io.andrys.monopoly.Board;
import io.andrys.monopoly.Game;
import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Player;
import io.andrys.monopoly.R;

/**
 * NewGameState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Starts a new game from scratch
 */
public class NewGameState extends GameState {
    private final String TAG = this.getClass().getSimpleName();

    public NewGameState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        Log.v(TAG, "onStateEnter()");
    }

    @Override
    public void execute() {
        Log.v(TAG, "execute()");

        // Add each player's token to the board
        Iterator<Player> itr = gc.players.iterator();
        while (itr.hasNext()) {
            Player p = itr.next();
            gc.board.addPlayerToken(p.getToken());
            Log.v(TAG, String.format("Token ID '%d' added to the board.", p.getToken()));
        }

        // tell the view to re-render because we're done changing the model
        render();

        // set the first player in the list as the active player; move to the dice roll phase.

    }

    @Override
    public void onStateExit() {
        Log.v(TAG, "onStateExit()");
        // enable the UI controls, maybe do this in render
    }

    @Override
    protected void render() {
        Log.v(TAG, "render()");
        // Draw each token we added to the game onto the board on Go
        Iterator<Player> itr = gc.players.iterator();
        while (itr.hasNext()) {
            Player p = itr.next();
            engine.getParentActivity().drawTokenOntoBoard(p.getToken());
        }

    }

}
