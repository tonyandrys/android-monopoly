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
 * An attempt to have a TickerView animate w/ text colors
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
     * @param incrementColor
     * @param decrementColor
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

    // Animate with text colors maybe?
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
     * Returns the dollar value this BalanceTickerView currently displays as an integer.
     * Prefer this method over getText() to avoid having to manually strip out the dollar sign all the time.
     * @return
     */
    public int getSimpleValue() {
        String[] splitted = this.getText().split("\\$");
        String s = this.getText().split("\\$")[1];
        return Integer.parseInt(s);
    }


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
