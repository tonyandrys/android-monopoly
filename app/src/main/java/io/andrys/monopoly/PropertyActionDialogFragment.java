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
public class PropertyActionDialogFragment extends DialogFragment implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();

    // Bundle keys
    public static final String KEY_PROPERTY_DRAWABLE_ID = "KEY_PROPERTY_DRAWABLE_ID";
    public static final String KEY_PROPERTY_POSITION = "KEY_PROPERTY_POSITION";
    public static final String KEY_BUY_BUTTON_STATE = "KEY_BUY_BUTTON_STATE";

    private int propertyPosition;
    private int propertyDrawableID;
    private boolean buyButtonState;
    private ButtonListener listener;

    public interface ButtonListener {
        void onBuyButtonClicked(PropertyActionDialogFragment df, int position);
        void onAuctionButtonClicked(PropertyActionDialogFragment df, int position);
        void onManageButtonClicked(PropertyActionDialogFragment df);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null) {
            this.propertyDrawableID = b.getInt(KEY_PROPERTY_DRAWABLE_ID);
            this.propertyPosition = b.getInt(KEY_PROPERTY_POSITION);
            this.buyButtonState = b.getBoolean(KEY_BUY_BUTTON_STATE);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.property_action_modal, container, false);

        // load the property card imageview w/ the correct drawable for this position on the board.
        ImageView propertyCardIV = v.findViewById(R.id.property_card_container_iv);
        Drawable d = ContextCompat.getDrawable(getActivity(), propertyDrawableID);
        propertyCardIV.setImageDrawable(d);

        // configure buttons
        Button buyBtn = v.findViewById(R.id.buy_btn);
        buyBtn.setEnabled(buyButtonState);

        /* this fragment receives the first click events, then calls the appropriate ButtonListener
           method on the delegate object (listener) */
        if (listener != null) {
            Button auctionBtn = v.findViewById(R.id.auction_btn);
            Button manageBtn = v.findViewById(R.id.manage_btn);
            buyBtn.setOnClickListener(this);
            auctionBtn.setOnClickListener(this);
            manageBtn.setOnClickListener(this);
        } else {
            throw new IllegalStateException("Fragment shown before being assigned an OnClickListener! Call .setOnClickListener() first!");
        }

        // When this modal is on-screen, back button presses and touches outside of the modal should be ignored
        // (i.e. they should not dismiss the dialog).
        setCancelable(false);
        if (this.getDialog() != null) {
            this.getDialog().setCanceledOnTouchOutside(false);
        }
        return v;
    }

    /**
     * Sets the object that should receive buy/auction/manage click events from the buttons in this modal.
     * This method *must* be called before showing this fragment for proper functionality.
     * @param listener object that implements the ButtonListener interface in this class
     */
    public void setButtonListener(ButtonListener listener) {
        this.listener = listener;
    }

    /**
     * Based on the button touched in the modal, call the appropriate ButtonListener
     * method on the object listening for click events.
     * @param v Button clicked in the modal
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buy_btn:
                listener.onBuyButtonClicked(this, propertyPosition);
                break;
            case R.id.auction_btn:
                listener.onAuctionButtonClicked(this, propertyPosition);
                break;
            case R.id.manage_btn:
                listener.onManageButtonClicked(this);

        }

    }

    // *** Click events start below this line ***
//    /**
//     * Fired when the Manage button is pressed on this dialog.
//     */
//
//    public void manageButtonPressed() {
//        Log.v(TAG, "Manage button clicked!");
//
//        this.dismiss();
//    }
//
//    /**
//     * Fired when the Buy button is pressed on this dialog.
//     */
//    public void buyButtonPressed() {
//        Log.v(TAG, "Buy button clicked!");
//    }
//
//    /**
//     * Fired when the Auction button is pressed on this dialog.
//     */
//    public void auctionButtonPressed() {
//        Log.v(TAG, "Auction button clicked!");
//
//    }


}
