package io.andrys.monopoly;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;

import com.robinhood.ticker.TickerView;

/**
 * BalanceTickerView.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * A BalanceTickerView is a subclass of TickerView that we use to display Player balances in the
 * score table.
 *
 * It inherits most of its animation behavior from TickerView, but adds the ability to change the
 * font color of the view while the display value is changing. We use this to set a specific increment
 * font color and decrement font color; the display text changes to this color depending on the direction
 * of the animation.
 */

public class BalanceTickerView extends TickerView implements Animator.AnimatorListener {
    private final String TAG = this.getClass().getSimpleName();

    private final int COLOR_ANIMATE_DIRECTION_INC = 0x1;
    private final int COLOR_ANIMATE_DIRECTION_DEC = 0x2;

    private int defaultColor;
    private int incrementColor;
    private int decrementColor;

    private boolean shouldColorAnimate;
    private int colorAnimateDirection;

    public BalanceTickerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    /**
     * Allows adding colors to the increment/decrement animations of this ticker.
     * @param context
     * @param incrementColor an Android int color
     * @param decrementColor an Android int color
     */
    public BalanceTickerView(Context context, int incrementColor, int decrementColor) {
        super(context);
        init(context, null, 0, 0);
        this.defaultColor = this.getTextColor();
        this.incrementColor = incrementColor;
        this.decrementColor = decrementColor;
        // let this class listen to animation events which we will use to modify the text color
        this.addAnimatorListener(this);
    }

    public BalanceTickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BalanceTickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BalanceTickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Set the display value of this view using this method to change the text color during the
     * ticker change animation. Must call w/ colorAnimate=true, otherwise text color values will be ignored.
     *
     * The increment or decrement color is chosen depending on the difference between the new value
     * and the existing one. If the new value is a larger number than the existing value, the increment
     * color is chosen. If the new value is smaller than the existing value, the decrement color is chosen.
     *
     * @param text
     * @param animate
     * @param colorAnimate
     */
    public void setText(String text, boolean animate, boolean colorAnimate) {
        if (colorAnimate) {
            shouldColorAnimate = true;
            // figure out if we're going up or down
            int existingVal = this.getSimpleValue();
            int newVal = Integer.parseInt(text.split("\\$")[1]);
            if (newVal > existingVal) {
                colorAnimateDirection = COLOR_ANIMATE_DIRECTION_INC;
            } else if (newVal < existingVal) {
                colorAnimateDirection = COLOR_ANIMATE_DIRECTION_DEC;
            } else {
                shouldColorAnimate = false;
            }

        }
        setText(text, animate);
    }

    /**
     * Returns the number that this BalanceTickerView is currently displaying as an integer.
     * Prefer this method over getText() to avoid having to manually strip out the dollar sign all the time.
     * @return display value as an integer
     */
    public int getSimpleValue() {
        String s = this.getText().split("\\$")[1];
        return Integer.parseInt(s);
    }


    // Listen to TickerView's animation events to change the text color appropriately.
    @Override
    public void onAnimationStart(Animator animation) {
        if (shouldColorAnimate) {
            if (colorAnimateDirection == COLOR_ANIMATE_DIRECTION_INC) {
                setTextColor(incrementColor);
            } else if (colorAnimateDirection == COLOR_ANIMATE_DIRECTION_DEC) {
                setTextColor(decrementColor);
            }
        }

    }

    // Listen to TickerView's animation events to change the text color appropriately.
    @Override
    public void onAnimationEnd(Animator animation) {
        if (shouldColorAnimate) {
            this.setTextColor(this.defaultColor);
            shouldColorAnimate = false;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
