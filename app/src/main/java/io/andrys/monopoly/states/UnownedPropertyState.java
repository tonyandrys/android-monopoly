package io.andrys.monopoly.states;

import android.util.Log;
import android.view.View;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.Property;
import io.andrys.monopoly.PropertyActionDialogFragment;
import io.andrys.monopoly.R;
import io.andrys.monopoly.ScoreTableLayout;
import io.andrys.monopoly.exceptions.NotYetImplementedException;

/**
 * UnownedPropertyState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Presents the purchase/auction/manage modal for the property the active player has landed on
 * and waits for their input.
 */
public class UnownedPropertyState extends GameState implements PropertyActionDialogFragment.ButtonListener {

    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    private ScoreTableLayout scoreTable;

    private Property prop;

    public UnownedPropertyState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        scoreTable = engine.getActivity().findViewById(R.id.score_table_tl);

        // cache the Property the player has landed on
        int position = gc.board.getTokenPosition(gc.activePlayer.getToken());
        prop = gc.pm.inspectProperty(position);
    }

    @Override
    public void execute() {
        // can the active player afford to buy what they landed on?
        boolean hasSufficientFunds = gc.activePlayer.getBalance() > prop.getPrice();

        // Show the property action modal for the position the player is on
        engine.getActivity().showPropertyActionModal(this, prop.getPosition(), hasSufficientFunds);
        Log.v(TAG, String.format("Presenting property action modal for '%s'", prop.getName()));
    }

    @Override
    public void onStateExit() {

    }

    @Override
    protected void render() {
        // color the property that was just purchased & update the buying player's balance
        engine.getActivity().redrawPropertyAtPosition(prop.getPosition(), gc.activePlayer, gc.pm.getDevelopmentLevelAtPosition(prop.getPosition()));
        scoreTable.updatePlayerBalance(gc.activePlayer, gc.activePlayer.getBalance());
    }


    @Override
    public void onBuyButtonClicked(PropertyActionDialogFragment df, int position) {
        // cha-ching!
        engine.playAudio(R.raw.cash_register_single_ding);

        // deduct purchase price from their balance and assign them ownership
        gc.activePlayer.deductFromBalance(prop.getPrice());
        gc.pm.assignPropertyToOwner(position, gc.activePlayer.getToken());
        Log.v(TAG, String.format("%s has purchased %s for $%d.", gc.activePlayer.getName(), prop.getName(), prop.getPrice()));

        // redraw the newly purchased property & kill the modal
        render();
        df.dismiss();

        // end this turn
        GameContext next = new GameContext(gc.board.getDiceValues(), gc.activePlayer, gc.players, gc.board, gc.pm);
        changeState(new EndTurnState(engine, next));
    }

    @Override
    public void onAuctionButtonClicked(PropertyActionDialogFragment df, int position) {
        // auction this property
        throw new NotYetImplementedException();
    }

    @Override
    public void onManageButtonClicked(PropertyActionDialogFragment df) {
        // go to the active player's resource management screen
        throw new NotYetImplementedException();
    }
}
