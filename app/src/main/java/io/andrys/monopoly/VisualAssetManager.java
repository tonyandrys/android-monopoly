package io.andrys.monopoly;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * VisualAssetManager.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 *
 * Keeps track of images and drawable files and other stuff we want to display
 */
public class VisualAssetManager {
    private Context context;

    public VisualAssetManager(Context c) {
        this.context = c;
    }

    /**
     * If the calling Activity is about to be destroyed, eliminate all Context references stored in
     * this class to allow the GC to properly de-allocate the caller.
     */
    public void onDestroy() {
        this.context = null;
    }

    public Drawable getDieFace(int value) {
        Drawable face;
        switch(value) {
            case 1:
                face = context.getDrawable(R.drawable.diceface_1);
                break;
            case 2:
                face = context.getDrawable(R.drawable.diceface_2);
                break;
            case 3:
                face = context.getDrawable(R.drawable.diceface_3);
                break;
            case 4:
                face = context.getDrawable(R.drawable.diceface_4);
                break;
            case 5:
                face = context.getDrawable(R.drawable.diceface_5);
                break;
            case 6:
                face = context.getDrawable(R.drawable.diceface_6);
                break;
            default:
                throw new IllegalArgumentException(String.format("No die face exists for the passed value of '%d'!", value));
        }
        return face;
    }
}
