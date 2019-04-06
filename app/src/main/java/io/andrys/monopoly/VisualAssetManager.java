package io.andrys.monopoly;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * VisualAssetManager.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 *
 * Keeps track of images and drawable files and other stuff we want to display
 */
public class VisualAssetManager {
    private Activity context;

    public VisualAssetManager(Activity c) {
        this.context = c;
    }

    /**
     * If the calling Activity is about to be destroyed, eliminate all Context references stored in
     * this class to allow the GC to properly de-allocate the caller.
     */
    public void onDestroy() {
        this.context = null;
    }

    /**
     * Returns the Drawable of the face on a die associated with the passed value.
     * @param value An integer in [1,6].
     * @return
     */
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

    /**
     * Returns the drawable of the property card that corresponds to the space at position 'p'.
     * Generally, a valid position is a number from 0 to 39 where 0 represents Go & 39 represents Boardwalk,
     * but since all positions on the board *are not* properties (such as the Jail or Luxury Tax spaces) not all
     * positions will return Drawables!
     *
     * Calling this method with a non-property space position will throw an exception
     *
     * @param p
     * @return
     */
    /*
    public Drawable getPropertyCardDrawable(int p) {

        Drawable card;
        switch (p) {
            case 0:
                //card = context.getDrawable(R.drawable.pcard_go_iv);
                break;
            case 1:
                card = context.getDrawable(R.drawable.pcard_mediterranean_avenue);
                break;
            case 2:
                card = context.getDrawable(R.drawable.pcard_cchest_bottom_iv);
                break;
            case 3:
                card = context.findViewById(R.id.title_baltic_avenue_iv);
                break;
            case 4:
                card = context.findViewById(R.id.title_income_tax_iv);
                break;
            case 5:
                card = context.getDrawable(R.drawable.pcard_reading_rr_iv);
                break;
            case 6:
                card = context.getDrawable(R.drawable.pcard_oriental_avenue_iv);
                break;
            case 7:
                card = context.getDrawable(R.drawable.pcard_chance_bottom_iv);
                break;
            case 8:
                card = context.getDrawable(R.drawable.pcard_vermont_avenue_iv);
                break;
            case 9:
                card = context.getDrawable(R.drawable.pcard_connecticut_avenue_iv);
                break;
            case 10:
                card = context.getDrawable(R.drawable.pcard_jail_iv);
                break;
            case 11:
                card = context.getDrawable(R.drawable.pcard_st_charles_place_iv);
                break;
            case 12:
                card = context.getDrawable(R.drawable.pcard_electric_company_iv);
                break;
            case 13:
                card = context.getDrawable(R.drawable.pcard_states_avenue_iv);
                break;
            case 14:
                card = context.getDrawable(R.drawable.pcard_virginia_avenue_iv);
                break;
            case 15:
                card = context.getDrawable(R.drawable.pcard_pennsylvania_rr_iv);
                break;
            case 16:
                card = context.getDrawable(R.drawable.pcard_st_james_place_iv);
                break;
            case 17:
                card = context.getDrawable(R.drawable.pcard_cchest_left_iv);
                break;
            case 18:
                card = context.getDrawable(R.drawable.pcard_tennessee_avenue_iv);
                break;
            case 19:
                card = context.getDrawable(R.drawable.pcard_new_york_avenue_iv);
                break;
            case 20:
                card = context.getDrawable(R.drawable.pcard_free_parking_iv);
                break;
            case 21:
                card = context.getDrawable(R.drawable.pcard_kentucky_avenue_iv);
                break;
            case 22:
                card = context.getDrawable(R.drawable.pcard_chance_top_iv);
                break;
            case 23:
                card = context.getDrawable(R.drawable.pcard_indiana_avenue_iv);
                break;
            case 24:
                card = context.getDrawable(R.drawable.pcard_illinois_avenue_iv);
                break;
            case 25:
                card = context.getDrawable(R.drawable.pcard_bando_rr_iv);
                break;
            case 26:
                card = context.getDrawable(R.drawable.pcard_atlantic_avenue_iv);
                break;
            case 27:
                card = context.getDrawable(R.drawable.pcard_ventnor_avenue_iv);
                break;
            case 28:
                card = context.getDrawable(R.drawable.pcard_water_works_iv);
                break;
            case 29:
                card = context.getDrawable(R.drawable.pcard_marvin_gardens_iv);
                break;
            case 30:
                card = context.getDrawable(R.drawable.pcard_go_to_jail_iv);
                break;
            case 31:
                card = context.getDrawable(R.drawable.pcard_pacific_avenue_iv);
                break;
            case 32:
                card = context.getDrawable(R.drawable.pcard_north_carolina_avenue_iv);
                break;
            case 33:
                card = context.getDrawable(R.drawable.pcard_cchest_right_iv);
                break;
            case 34:
                card = context.getDrawable(R.drawable.pcard_pennsylvania_avenue_iv);
                break;
            case 35:
                card = context.getDrawable(R.drawable.pcard_short_line_rr_iv);
                break;
            case 36:
                card = context.getDrawable(R.drawable.pcard_chance_right_iv);
                break;
            case 37:
                card = context.getDrawable(R.drawable.pcard_park_place_iv);
                break;
            case 38:
                card = context.getDrawable(R.drawable.pcard_luxury_tax_iv);
                break;
            case 39:
                card = context.getDrawable(R.drawable.pcard_boardwalk_iv);
                break;
            default:
                throw new IllegalArgumentException(String.format("No tile exists at position '%d'!", p));
        }
        return iv;

    }

    */

    /**
     * Returns the drawable representing a player token mapped to the token ID value provided.
     * @param tokenID
     * @return
     */
    public Drawable getTokenDrawable(int tokenID) {
        Drawable token;
        switch(tokenID) {
            case 1:
                token = context.getDrawable(R.drawable.green_oval);
                break;
            default:
                throw new IllegalArgumentException(String.format("No drawable exists for the player token id '%d'!", tokenID));
        }
        return token;
    }

    /**
     * Returns the ImageView that represents the 'p'th position on the board.
     * A valid position is a number from 0 to 39 where 0 represents Go & 39 represents Boardwalk.
     * @param p a non-negative integer in [0, 39]
     * @return
     */
    public ImageView getIVForBoardPosition(int p) throws IllegalArgumentException {
        ImageView iv;
        switch (p) {
            case 0:
                iv = context.findViewById(R.id.tile_go_iv);
                break;
            case 1:
                iv = context.findViewById(R.id.tile_medit_avenue_iv);
                break;
            case 2:
                iv = context.findViewById(R.id.tile_cchest_bottom_iv);
                break;
            case 3:
                iv = context.findViewById(R.id.title_baltic_avenue_iv);
                break;
            case 4:
                iv = context.findViewById(R.id.title_income_tax_iv);
                break;
            case 5:
                iv = context.findViewById(R.id.tile_reading_rr_iv);
                break;
            case 6:
                iv = context.findViewById(R.id.tile_oriental_avenue_iv);
                break;
            case 7:
                iv = context.findViewById(R.id.tile_chance_bottom_iv);
                break;
            case 8:
                iv = context.findViewById(R.id.tile_vermont_avenue_iv);
                break;
            case 9:
                iv = context.findViewById(R.id.tile_connecticut_avenue_iv);
                break;
            case 10:
                iv = context.findViewById(R.id.tile_jail_iv);
                break;
            case 11:
                iv = context.findViewById(R.id.tile_st_charles_place_iv);
                break;
            case 12:
                iv = context.findViewById(R.id.tile_electric_company_iv);
                break;
            case 13:
                iv = context.findViewById(R.id.tile_states_avenue_iv);
                break;
            case 14:
                iv = context.findViewById(R.id.tile_virginia_avenue_iv);
                break;
            case 15:
                iv = context.findViewById(R.id.tile_pennsylvania_rr_iv);
                break;
            case 16:
                iv = context.findViewById(R.id.tile_st_james_place_iv);
                break;
            case 17:
                iv = context.findViewById(R.id.tile_cchest_left_iv);
                break;
            case 18:
                iv = context.findViewById(R.id.tile_tennessee_avenue_iv);
                break;
            case 19:
                iv = context.findViewById(R.id.tile_new_york_avenue_iv);
                break;
            case 20:
                iv = context.findViewById(R.id.tile_free_parking_iv);
                break;
            case 21:
                iv = context.findViewById(R.id.tile_kentucky_avenue_iv);
                break;
            case 22:
                iv = context.findViewById(R.id.tile_chance_top_iv);
                break;
            case 23:
                iv = context.findViewById(R.id.tile_indiana_avenue_iv);
                break;
            case 24:
                iv = context.findViewById(R.id.tile_illinois_avenue_iv);
                break;
            case 25:
                iv = context.findViewById(R.id.tile_bando_rr_iv);
                break;
            case 26:
                iv = context.findViewById(R.id.tile_atlantic_avenue_iv);
                break;
            case 27:
                iv = context.findViewById(R.id.tile_ventnor_avenue_iv);
                break;
            case 28:
                iv = context.findViewById(R.id.tile_water_works_iv);
                break;
            case 29:
                iv = context.findViewById(R.id.tile_marvin_gardens_iv);
                break;
            case 30:
                iv = context.findViewById(R.id.tile_go_to_jail_iv);
                break;
            case 31:
                iv = context.findViewById(R.id.tile_pacific_avenue_iv);
                break;
            case 32:
                iv = context.findViewById(R.id.tile_north_carolina_avenue_iv);
                break;
            case 33:
                iv = context.findViewById(R.id.tile_cchest_right_iv);
                break;
            case 34:
                iv = context.findViewById(R.id.tile_pennsylvania_avenue_iv);
                break;
            case 35:
                iv = context.findViewById(R.id.tile_short_line_rr_iv);
                break;
            case 36:
                iv = context.findViewById(R.id.tile_chance_right_iv);
                break;
            case 37:
                iv = context.findViewById(R.id.tile_park_place_iv);
                break;
            case 38:
                iv = context.findViewById(R.id.tile_luxury_tax_iv);
                break;
            case 39:
                iv = context.findViewById(R.id.tile_boardwalk_iv);
                break;
            default:
                throw new IllegalArgumentException(String.format("No tile exists at position '%d'!", p));
        }
        return iv;
    }

}
