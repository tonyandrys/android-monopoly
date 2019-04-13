package io.andrys.monopoly;

/**
 * ViewBuilder.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

/**
 * A singleton that builds complex View objects.
 */
public class ViewBuilder {
    private final String TAG = this.getClass().getSimpleName();

    private static ViewBuilder instance = null;

    private int HOUSE_VIEW_HEIGHT = 7;
    private int HOUSE_VIEW_WIDTH = 7;
    private int HOUSE_VIEW_MARGIN = 1;

    private ViewBuilder() {}

    public static ViewBuilder getInstance() {
        if (instance == null) {
            instance = new ViewBuilder();
        }
        return instance;
    }

    /**
     * Constructs a new house ImageView for a certain side of the board.
     * Margin is added to the appropriate side of the ImageView if isFirstOnProperty is true.
     * @param context
     * @param boardSide
     * @param isFirstOnProperty
     * @return
     */
    public ImageView buildHouseIV(Context context, @NonNull VisualAssetManager.BoardSide boardSide, boolean isFirstOnProperty) {

        // construct a new ImageView w/ a house image
        ImageView houseIV = new ImageView(context);
        houseIV.setId(View.generateViewId());
        houseIV.setTag("h");  // how about this means "house"?
        houseIV.setImageDrawable(context.getDrawable(R.drawable.house4x));
        houseIV.setVisibility(View.INVISIBLE);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HOUSE_VIEW_HEIGHT, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HOUSE_VIEW_WIDTH, context.getResources().getDisplayMetrics());
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, height);

        // add 1dp of margin to this view if this is the first house to go on the property
        if (isFirstOnProperty) {
            if ((boardSide == VisualAssetManager.BoardSide.BOTTOM) || (boardSide == VisualAssetManager.BoardSide.TOP)) {
                // start side margin
                lp.setMarginStart((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HOUSE_VIEW_MARGIN, context.getResources().getDisplayMetrics()));
            } else if ((boardSide == VisualAssetManager.BoardSide.LEFT) || (boardSide == VisualAssetManager.BoardSide.RIGHT)) {
                // top side margin
                lp.setMargins(lp.leftMargin, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HOUSE_VIEW_MARGIN, context.getResources().getDisplayMetrics()), lp.rightMargin, lp.bottomMargin);
            }
        }
        houseIV.setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
        return houseIV;
    }
}
