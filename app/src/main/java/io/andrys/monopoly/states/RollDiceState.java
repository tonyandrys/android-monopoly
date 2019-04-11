package io.andrys.monopoly.states;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.andrys.monopoly.Board;
import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.R;

/**
 * RollDiceState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */
public class RollDiceState extends GameState {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    public RollDiceState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        Log.v(TAG, "onStateEnter()");

        // setup dice roll button click listener
        Button rollButton = engine.getParentActivity().findViewById(R.id.roll_dice_btn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDiceButtonPressed();
            }
        });
    }

    @Override
    public void execute() {
        Log.v(TAG, "execute()");
        Log.v(TAG, String.format("waiting for the active player ('%s', token '%d') to roll the dice...", gc.activePlayer.getName(), gc.activePlayer.getToken()));
    }

    @Override
    public void onStateExit() {
        Log.v(TAG, "onStateExit()");

    }

    @Override
    protected void render() {
        Log.v(TAG, "render()");

        // Repaint dice values
        ImageView die1 = engine.getParentActivity().findViewById(R.id.die_1_iv);
        ImageView die2 = engine.getParentActivity().findViewById(R.id.die_2_iv);
        int[] r = gc.board.getDiceValues();
        die1.setImageDrawable(engine.getParentActivity().visualAssetManager.getDieFace(r[0]));
        die2.setImageDrawable(engine.getParentActivity().visualAssetManager.getDieFace(r[1]));

        // move the active player's token forward along the board
        engine.getParentActivity().drawTokenAtPosition(gc.activePlayer.getToken(), gc.board.getTokenPosition(1));
    }

    private void rollDiceButtonPressed() {
        // roll the dice, increment the position of the active player
        gc.board.rollDice();
        int[] r = gc.board.getDiceValues();
        gc.board.incrementTokenPosition(gc.activePlayer.getToken(), r[0]+r[1]);
        render();

        // advance to the next state (now is an emptystate, should fire property modal when I get back)
        GameContext next = new GameContext(r, gc.activePlayer, gc.players, gc.board, gc.pm);
        changeState(new RollDiceState(engine, next));

    }
}
