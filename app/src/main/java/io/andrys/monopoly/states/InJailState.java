package io.andrys.monopoly.states;

import android.util.Log;

import io.andrys.monopoly.GameContext;
import io.andrys.monopoly.GameEngine;
import io.andrys.monopoly.InJailActionDialogFragment;
import io.andrys.monopoly.Player;
import io.andrys.monopoly.R;
import io.andrys.monopoly.ScoreTableLayout;
import io.andrys.monopoly.exceptions.NotYetImplementedException;

/**
 * InJailState.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

public class InJailState extends GameState implements InJailActionDialogFragment.ButtonListener {

    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    private ScoreTableLayout scoreTable;

    // cost to leave jail immediately
    private final int JAIL_FINE = 50;

    public InJailState(GameEngine engine, GameContext gameContext) {
        super(engine, gameContext);
    }

    @Override
    public void onStateEnter() {
        scoreTable = engine.getActivity().findViewById(R.id.score_table_tl);
    }

    @Override
    public void execute() {
        // check if the player has a get out of jail free card and/or can afford to pay the fine right now
        boolean hasCard = false;
        if (gc.activePlayer.getGetOutOfJailFreeCount() > 0) {
            hasCard = true;
        }

        boolean canPayFine = false;
        if (gc.activePlayer.getBalance() >= JAIL_FINE) {
            canPayFine = true;
        }

        // show the jail action modal
        engine.getActivity().showInJailActionModal(this, canPayFine, hasCard);
        Log.v(TAG, String.format("Presenting jail action modal for '%s'", gc.activePlayer.getName()));
    }

    @Override
    public void onStateExit() {

    }

    @Override
    protected void render() {

    }

    // Removes player from jail & restarts their turn normally (i.e. allows them to roll dice, make trades, etc.)
    // Player should (obviously) be the active player
    private void freePlayerFromJail(Player player) {
        // freedom sound!
        engine.playAudio(R.raw.jail_freed);

        player.setIsInJail(false);
        GameContext next = new GameContext(gc.board.getDiceValues(), player, gc.players, gc.board, gc.pm);
        changeState(new RollDiceState(engine, next));
    }

    @Override
    public void onRollButtonClicked(InJailActionDialogFragment df) {
        throw new NotYetImplementedException();
    }

    @Override
    public void onPayButtonClicked(InJailActionDialogFragment df) {
        gc.activePlayer.deductFromBalance(JAIL_FINE);
        scoreTable.updatePlayerBalance(gc.activePlayer, gc.activePlayer.getBalance());
        Log.v(TAG, String.format(TAG, "%s has paid the $%d fine to leave jail.", gc.activePlayer.getName(), JAIL_FINE));
        df.dismiss();
        freePlayerFromJail(gc.activePlayer);
    }

    @Override
    public void onUseCardButtonClicked(InJailActionDialogFragment df) {
        gc.activePlayer.removeGetOutOfJailFree();
        df.dismiss();
        freePlayerFromJail(gc.activePlayer);
    }
}
