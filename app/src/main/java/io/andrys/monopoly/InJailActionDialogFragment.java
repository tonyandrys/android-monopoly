package io.andrys.monopoly;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * InJailActionDialogFragment.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */
public class InJailActionDialogFragment extends DialogFragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private boolean useCardButtonState;
    private boolean payFineButtonState;
    private ButtonListener listener;

    // Bundle keys
    public static final String KEY_ENABLE_GET_OUT_OF_JAIL_FREE = "KEY_ENABLE_GET_OUT_OF_JAIL_FREE";
    public static final String KEY_ENABLE_PAY_FINE = "KEY_ENABLE_PAY_FINE";

    /**
     * Classes can receive click events by implementing the ButtonListener interface. They can then be paseed as an argument to the {@link #setButtonListener} method.
     */
    public interface ButtonListener {
        void onRollButtonClicked(InJailActionDialogFragment df);
        void onPayButtonClicked(InJailActionDialogFragment df);
        void onUseCardButtonClicked(InJailActionDialogFragment df);
    }

    /**
     * Sets the object that should receive roll/pay/card click events from the buttons in this modal.
     * This method *must* be called before showing this fragment for proper functionality.
     * @param listener object that implements the ButtonListener interface in this class
     */
    public void setButtonListener(ButtonListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null) {
            this.useCardButtonState = b.getBoolean(KEY_ENABLE_GET_OUT_OF_JAIL_FREE, false);
            this.payFineButtonState = b.getBoolean(KEY_ENABLE_PAY_FINE, false);
        } else {
            // if no bundle is sent on initialization, use default values for all instance variables
            this.useCardButtonState = false;
            this.payFineButtonState = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.in_jail_action_modal, container, false);

        // set state of buttons appropriately, send button click events back to this instance
        Button rollDiceButton = v.findViewById(R.id.jail_roll_button);

        Button payFineButton = v.findViewById(R.id.jail_pay_button);
        payFineButton.setEnabled(payFineButtonState);

        Button useCardButton = v.findViewById(R.id.jail_get_out_free_button);
        if (useCardButtonState) {
            useCardButton.setVisibility(View.VISIBLE);
            useCardButton.setEnabled(true);
        } else {
            // if player does not have a get out of jail free card, hide the use card button entirely to save vertical screen space.
            useCardButton.setVisibility(View.GONE);
            useCardButton.setEnabled(false);
        }

        /* this fragment receives the first click events, then calls the appropriate ButtonListener
           method on the delegate object (listener) */
        if (listener != null) {
            rollDiceButton.setOnClickListener(this);
            payFineButton.setOnClickListener(this);
            useCardButton.setOnClickListener(this);
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
     * Based on the button touched in the modal, call the appropriate ButtonListener
     * method on the object listening for click events.
     * @param v Button clicked in the modal
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.jail_roll_button:
                listener.onRollButtonClicked(this);
                break;
            case R.id.jail_pay_button:
                listener.onPayButtonClicked(this);
                break;
            case R.id.jail_get_out_free_button:
                listener.onUseCardButtonClicked(this);

        }

    }
}
