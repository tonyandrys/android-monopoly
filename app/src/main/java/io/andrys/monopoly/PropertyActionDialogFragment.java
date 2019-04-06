package io.andrys.monopoly;

import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * PropertyActionDialogFragment.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */
public class PropertyActionDialogFragment extends DialogFragment {
    private final String TAG = this.getClass().getSimpleName();

    // Bundle keys
    public static final String KEY_PROPERTY_DRAWABLE_ID = "KEY_PROPERTY_DRAWABLE_ID";

    private int propertyDrawableID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null) {
            this.propertyDrawableID = b.getInt(KEY_PROPERTY_DRAWABLE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.property_action_modal, container, false);

        // load the property card imageview w/ the correct drawable for this position on the board.
        ImageView propertyCardIV = v.findViewById(R.id.property_card_container_iv);
        Drawable d = ContextCompat.getDrawable(getActivity(), propertyDrawableID);
        propertyCardIV.setImageDrawable(d);

        // attach button listeners
        Button buyBtn = v.findViewById(R.id.buy_btn);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyButtonPressed();
            }
        });

        Button auctionBtn = v.findViewById(R.id.auction_btn);
        auctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auctionButtonPressed();
            }
        });

        Button manageBtn = v.findViewById(R.id.manage_btn);
        manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageButtonPressed();
            }
        });

        return v;
    }

    /**
     * Fired when the Manage button is pressed on this dialog.
     */
    public void manageButtonPressed() {
        Log.v(TAG, "Manage button clicked!");

        this.dismiss();

    }

    /**
     * Fired when the Buy button is pressed on this dialog.
     */
    public void buyButtonPressed() {
        Log.v(TAG, "Buy button clicked!");
    }

    /**
     * Fired when the Auction button is pressed on this dialog.
     */
    public void auctionButtonPressed() {
        Log.v(TAG, "Auction button clicked!");

    }

}
