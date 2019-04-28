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
     * There are four sides of a Monopoly board, and every non-corner space exists on
     * exactly one side of the board. Corner spaces, being in the corner of the monopoly board,
     * have two sides.
     */
    enum BoardSide {
        BOTTOM,
        TOP,
        LEFT,
        RIGHT
    }

    /**
     * Returns the side of the board that a space on the board is on.
     * @param position A valid board position in [0,39]
     * @return BoardSide
     */
    public BoardSide getBoardSideForPosition(int position) {
        if ((position >= 0) && (position <= 10)) {
            return BoardSide.BOTTOM;
        } else if ((position >= 11) && (position <= 20)) {
            return BoardSide.LEFT;
        } else if ((position >= 21) && (position <= 30)) {
            return BoardSide.TOP;
        } else if ((position >= 31) && (position <= 39)) {
            return BoardSide.RIGHT;
        } else {
            throw new IllegalArgumentException(String.format("Can't get BoardSide for position '%d'; argument out of bounds!", position));
        }
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
     * Returns the ID of the property card graphic for a position on the board.
     * @param p
     * @return
     */
    public int getPropertyCardDrawableID(int p) {
        int id;
        switch (p) {
            case 1:
                id = R.drawable.pcard_mediterranean_avenue;
                break;
            case 3:
                id = R.drawable.pcard_baltic_avenue;
                break;
            case 5:
                id = R.drawable.pcard_reading_rr;
                break;
            case 6:
                id = R.drawable.pcard_oriental_avenue;
                break;
            case 8:
                id = R.drawable.pcard_vermont_avenue;
                break;
            case 9:
                id = R.drawable.pcard_connecticut_avenue;
                break;
            case 11:
                id = R.drawable.pcard_st_charles_place;
                break;
            case 12:
                id = R.drawable.pcard_electric_company;
                break;
            case 13:
                id = R.drawable.pcard_states_avenue;
                break;
            case 14:
                id = R.drawable.pcard_virginia_avenue;
                break;
            case 15:
                id = R.drawable.pcard_pennsylvania_rr;
                break;
            case 16:
                id = R.drawable.pcard_st_james_place;
                break;
            case 18:
                id = R.drawable.pcard_tennessee_avenue;
                break;
            case 19:
                id = R.drawable.pcard_new_york_avenue;
                break;
            case 21:
                id = R.drawable.pcard_kentucky_avenue;
                break;
            case 23:
                id = R.drawable.pcard_indiana_avenue;
                break;
            case 24:
                id = R.drawable.pcard_illinois_avenue;
                break;
            case 25:
                id = R.drawable.pcard_bando_rr;
                break;
            case 26:
                id = R.drawable.pcard_atlantic_avenue;
                break;
            case 27:
                id = R.drawable.pcard_ventnor_avenue;
                break;
            case 28:
                id = R.drawable.pcard_water_works;
                break;
            case 29:
                id = R.drawable.pcard_marvin_gardens;
                break;
            case 31:
                id = R.drawable.pcard_pacific_avenue;
                break;
            case 32:
                id = R.drawable.pcard_north_carolina_avenue;
                break;
            case 34:
                id = R.drawable.pcard_pennsylvania_avenue;
                break;
            case 35:
                id = R.drawable.pcard_short_line_rr;
                break;
            case 37:
                id = R.drawable.pcard_park_place;
                break;
            case 39:
                id = R.drawable.pcard_boardwalk;
                break;
            default:
                throw new IllegalArgumentException(String.format("No property card exists for space at position '%d'!", p));
        }

        return id;
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
    public Drawable getPropertyCardDrawable(int p) {

        Drawable card;
        switch (p) {
            case 1:
                card = context.getDrawable(R.drawable.pcard_mediterranean_avenue);
                break;
            case 3:
                card = context.getDrawable(R.drawable.pcard_baltic_avenue);
                break;
            case 5:
                card = context.getDrawable(R.drawable.pcard_reading_rr);
                break;
            case 6:
                card = context.getDrawable(R.drawable.pcard_oriental_avenue);
                break;
            case 8:
                card = context.getDrawable(R.drawable.pcard_vermont_avenue);
                break;
            case 9:
                card = context.getDrawable(R.drawable.pcard_connecticut_avenue);
                break;
            case 11:
                card = context.getDrawable(R.drawable.pcard_st_charles_place);
                break;
            case 12:
                card = context.getDrawable(R.drawable.pcard_electric_company);
                break;
            case 13:
                card = context.getDrawable(R.drawable.pcard_states_avenue);
                break;
            case 14:
                card = context.getDrawable(R.drawable.pcard_virginia_avenue);
                break;
            case 15:
                card = context.getDrawable(R.drawable.pcard_pennsylvania_rr);
                break;
            case 16:
                card = context.getDrawable(R.drawable.pcard_st_james_place);
                break;
            case 18:
                card = context.getDrawable(R.drawable.pcard_tennessee_avenue);
                break;
            case 19:
                card = context.getDrawable(R.drawable.pcard_new_york_avenue);
                break;
            case 21:
                card = context.getDrawable(R.drawable.pcard_kentucky_avenue);
                break;
            case 23:
                card = context.getDrawable(R.drawable.pcard_indiana_avenue);
                break;
            case 24:
                card = context.getDrawable(R.drawable.pcard_illinois_avenue);
                break;
            case 25:
                card = context.getDrawable(R.drawable.pcard_bando_rr);
                break;
            case 26:
                card = context.getDrawable(R.drawable.pcard_atlantic_avenue);
                break;
            case 27:
                card = context.getDrawable(R.drawable.pcard_ventnor_avenue);
                break;
            case 28:
                card = context.getDrawable(R.drawable.pcard_water_works);
                break;
            case 29:
                card = context.getDrawable(R.drawable.pcard_marvin_gardens);
                break;
            case 31:
                card = context.getDrawable(R.drawable.pcard_pacific_avenue);
                break;
            case 32:
                card = context.getDrawable(R.drawable.pcard_north_carolina_avenue);
                break;
            case 34:
                card = context.getDrawable(R.drawable.pcard_pennsylvania_avenue);
                break;
            case 35:
                card = context.getDrawable(R.drawable.pcard_short_line_rr);
                break;
            case 37:
                card = context.getDrawable(R.drawable.pcard_park_place);
                break;
            case 39:
                card = context.getDrawable(R.drawable.pcard_boardwalk);
                break;
            default:
                throw new IllegalArgumentException(String.format("No property card exists for space at position '%d'!", p));
        }

        return card;
    }


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
            case 2:
                token = context.getDrawable(R.drawable.blue_oval);
                break;
            case 3:
                token = context.getDrawable(R.drawable.red_oval);
                break;
            case 4:
                token = context.getDrawable(R.drawable.purple_oval);
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
