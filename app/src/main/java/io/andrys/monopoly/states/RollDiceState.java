package io.andrys.monopoly.states;

import android.media.MediaPlayer;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayDeque;
import java.util.Locale;

import io.andrys.monopoly.Board;
import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Player;
import io.andrys.monopoly.Property;
import io.andrys.monopoly.R;
import io.andrys.monopoly.ScoreTableLayout;
import io.andrys.monopoly.exceptions.UnownedPropertyException;

/**
 * RollDiceState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Rolls the dice for the active player, lands on a space, constructs the next state based on the
 * type of space we land on and the context of the game.
 */
public class RollDiceState extends GameState implements Transition.TransitionListener {
    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    // reference to the "Roll" button in the parent activity
    private Button rollButton;

    public RollDiceState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {

        // let the roll dice button accept clicks
        rollButton = engine.getActivity().findViewById(R.id.roll_dice_btn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prevent double clicks
                rollButton.setEnabled(false);
                rollDiceButtonPressed();
            }
        });
        rollButton.setEnabled(true);

        // increment the on-screen turn counter
        engine.getActivity().incrementTurnCount();

    }

    @Override
    public void execute() {
        Log.v(TAG, String.format("waiting for the active player ('%s', token '%d') to roll the dice...", gc.activePlayer.getName(), gc.activePlayer.getToken()));
    }

    @Override
    public void onStateExit() {
        // silence roll button events
        rollButton.setOnClickListener(null);
    }

    @Override
    protected void render() {
        // Repaint dice values
        ImageView die1 = engine.getActivity().findViewById(R.id.die_1_iv);
        ImageView die2 = engine.getActivity().findViewById(R.id.die_2_iv);
        int[] r = gc.board.getDiceValues();
        die1.setImageDrawable(engine.getActivity().visualAssetManager.getDieFace(r[0]));
        die2.setImageDrawable(engine.getActivity().visualAssetManager.getDieFace(r[1]));

        // move the active player's token forward along the board
        engine.getActivity().drawTokenAtPosition(gc.activePlayer.getToken(), gc.board.getTokenPosition(gc.activePlayer.getToken()), this);
    }


     // Fired when the "Roll" UI button is touched
    private void rollDiceButtonPressed() {
        // roll the dice, increment the position of the active player
        gc.board.rollDice();
        int[] r = gc.board.getDiceValues();
        gc.board.incrementTokenPosition(gc.activePlayer.getToken(), r[0]+r[1]);
        render();
    }

    // Returns the next state based on the type of space the player is standing on.
    private GameState buildNextStateForPosition(int position, Board.SpaceType sType) {
        GameState newState;
        GameContext next;
        switch (sType) {

            case PROPERTY:
                // i) if this is an unowned property, show the purchasing modal
                if (!gc.pm.isPropertyOwned(position)) {
                    next = new GameContext(gc.board.getDiceValues(), gc.activePlayer, gc.players, gc.board, gc.pm);
                    newState = new UnownedPropertyState(engine, next);
                    break;
                } else {
                    // ii) if this property is owned by the current player, end turn.
                    try {
                        int ownerTokenID = gc.pm.getPropertyOwner(position);
                        Property prop = gc.pm.inspectProperty(position);
                        if (ownerTokenID == gc.activePlayer.getToken()) {
                            // end this turn
                            Log.v(TAG, String.format(Locale.US, "%s owns %s; nothing happens.", gc.activePlayer.getName(), prop.getName()));
                            next = new GameContext(gc.board.getDiceValues(), gc.activePlayer, gc.players, gc.board, gc.pm);
                            newState = new EndTurnState(engine, next);
                            break;
                        } else {
                            // iii) if property is owned by someone else, pay them their rent.
                            next = new GameContext(gc.board.getDiceValues(), gc.activePlayer, gc.players, gc.board, gc.pm);
                            newState = new PayRentState(engine, next);
                            break;
                        }
                    } catch (UnownedPropertyException e) {
                        throw new IllegalStateException("Inconsistent state!");
                    }
                }

            case GO_TO_JAIL:
                // TODO: play the police siren sound effect
                // send the player to jail!
                int jailPosition = gc.board.POSITION_JAIL;
                gc.board.setTokenPosition(gc.activePlayer.getToken(), jailPosition);
                engine.getActivity().drawTokenAtPosition(gc.activePlayer.getToken(), jailPosition, null);
                Log.v(TAG, String.format("%s has been sent to jail!", gc.activePlayer.getName()));

                // update the active player's model to show that they're not just visiting
                Player jailedPlayer = gc.activePlayer;
                jailedPlayer.setIsInJail(true);

                // end turn after moving the token to Jail
                next = new GameContext(gc.board.getDiceValues(), jailedPlayer, gc.players, gc.board, gc.pm);
                newState = new EndTurnState(engine, next);
                break;

            default:
                // end this turn
                next = new GameContext(gc.board.getDiceValues(), gc.activePlayer, gc.players, gc.board, gc.pm);
                newState = new EndTurnState(engine, next);
                break;
        }
        return newState;
    }

    /**
     * Once the token movement animation is over, determine & build the next appropriate state
     * based on the space we've landed on, then transition to it.
     * @param transition
     */
    @Override
    public void onTransitionEnd(Transition transition) {
        // the type of the property we landed on determines the next state
        int p = gc.board.getTokenPosition(gc.activePlayer.getToken());
        Board.SpaceType sType = gc.board.getSpaceTypeForPosition(p);
        changeState(buildNextStateForPosition(p, sType));
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }



    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }
}
