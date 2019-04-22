package io.andrys.monopoly;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

/**
 * ScoreTableLayout.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */
public class ScoreTableLayout extends TableLayout {
    private final String TAG = this.getClass().getSimpleName();

    private int ICON_VIEW_HEIGHT = 10;
    private int ICON_END_MARGIN = 5;
    private float ICON_LAYOUT_WEIGHT = 2f;
    private float NAME_LAYOUT_WEIGHT = 7f;
    private float BALANCE_LAYOUT_WEIGHT = 1f;

    private final int INDEX_PLAYER_ICON = 0;
    private final int INDEX_PLAYER_NAME = 1;
    private final int INDEX_PLAYER_BALANCE = 2;

    // Maps Players to the Views associated with them in this table
    private SparseArray<int[]> viewIDMap;

    public ScoreTableLayout(Context context) {
        super(context);
        viewIDMap = new SparseArray<>();
    }

    public ScoreTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewIDMap = new SparseArray<>();
    }

    /**
     * Adds a Player to the score table by creating a new row w/ the player's icon, name, and balance.
     * @param p Player to add
     */
    public void addPlayerRow(Player p) {
        TableRow tr = new TableRow(getContext());

        // create the color icon for this player
        ImageView iconIV = new ImageView(getContext());
        iconIV.setId(View.generateViewId());
        iconIV.setBackgroundColor(p.getOpaqueColor());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_VIEW_HEIGHT, getContext().getResources().getDisplayMetrics());
        //TableRow.LayoutParams iconLP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, height, ICON_LAYOUT_WEIGHT);
        TableRow.LayoutParams iconLP = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, ICON_LAYOUT_WEIGHT);
        iconLP.setMarginEnd((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_END_MARGIN, getContext().getResources().getDisplayMetrics()));
        iconIV.setLayoutParams(iconLP);

        // create the name label
        TextView nameTV = new TextView(getContext());
        nameTV.setId(View.generateViewId());
        nameTV.setText(p.getName());
        TableRow.LayoutParams nameLP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, NAME_LAYOUT_WEIGHT);
        nameTV.setLayoutParams(nameLP);

        // create the balance label
        TextView balanceTV = new TextView(getContext());
        balanceTV.setId(View.generateViewId());
        balanceTV.setText(String.format(Locale.US, "$%d", p.getBalance()));
        TableRow.LayoutParams balanceLP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, BALANCE_LAYOUT_WEIGHT);
        balanceTV.setLayoutParams(balanceLP);

        // add the row
        tr.addView(iconIV);
        tr.addView(nameTV);
        tr.addView(balanceTV);
        this.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        // store references to these views in this instance
        writeViewsToModel(p, iconIV, nameTV, balanceTV);
    }

    /**
     * Stores the ID of this Player's icon, name, and balance Views for future retrieval.
     * @param player Player to associate these views with
     * @param iconIV ImageView of Player's icon
     * @param nameTV TextView that displays the player's name
     * @param balanceTV TextView that displays the player's balance
     */
    private void writeViewsToModel(Player player, ImageView iconIV, TextView nameTV, TextView balanceTV) {
        if (viewIDMap.get(player.getToken(), null) == null) {
            int[] views = new int[]{iconIV.getId(), nameTV.getId(), balanceTV.getId()};
            viewIDMap.put(player.getToken(), views);
        } else {
            throw new IllegalStateException(String.format(Locale.US, "ScoreTable views for Player w/ token='%d' have already been created! This method should only be called once per Player.", player.getToken()));
        }
    }

    /**
     * Changes a Player's displayed balance to a new value
     * @param player Player whose balance should be modified
     * @param newBalance New value to be displayed as Player's balance
     */
    public void updatePlayerBalance(Player player, int newBalance) {
        int[] views = viewIDMap.get(player.getToken(), new int[0]);
        if (views.length != 0) {
            int balanceTVID = views[INDEX_PLAYER_BALANCE];
            TextView balanceTV = findViewById(balanceTVID);
            balanceTV.setText(String.format(Locale.US, "$%d", newBalance));
            Log.v(TAG, String.format("%s's display balance updated to %s", player.getName(), balanceTV.getText()));
        } else {
            throw new IllegalStateException(String.format(Locale.US, "No views stored in ScoreTable for player w/ token='%d'! writeViewsToModel() must be called before modifying display data on a ScoreTableLayout!", player.getToken()));
        }


    }
}
