package io.andrys.monopoly.states;

import android.util.Log;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Player;
import io.andrys.monopoly.R;
import io.andrys.monopoly.ScoreTableLayout;

/**
 * EndTurnState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Finishes the active player's turn, selects the next player to roll, then starts the next player's initial state.
 *
 * This is where we can remove touch listeners from the active player when we add AI/other human players.
 */
public class EndTurnState extends GameState {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    private ScoreTableLayout scoreTable;

    public EndTurnState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        scoreTable = engine.getActivity().findViewById(R.id.score_table_tl);
    }

    @Override
    public void execute() {
        // let the next player in the queue be the active player
        gc.players.addLast(gc.activePlayer);
        Player nextPlayer = gc.players.pollFirst();
        Log.v(TAG, String.format("Ending %s's turn; '%s' is up next.", gc.activePlayer.getName(), nextPlayer.getName()));

        // highlight the next player in the score table
        scoreTable.setActivePlayer(nextPlayer);

        // the first thing the next player will do when their turn starts is roll the dice UNLESS they're in jail.
        GameContext next = new GameContext(gc.board.getDiceValues(), nextPlayer, gc.players, gc.board, gc.pm);
        if (nextPlayer.isInJail()) {
            changeState(new InJailState(engine, next));
        } else {
            changeState(new RollDiceState(engine, next));
        }
    }

    @Override
    public void onStateExit() {

    }

    @Override
    protected void render() {

    }
}
