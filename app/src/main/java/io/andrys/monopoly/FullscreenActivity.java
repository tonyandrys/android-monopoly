package io.andrys.monopoly;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private Board board;
    private VisualAssetManager visualAssetManager;

    // tokenID -> ImageView that represents the position of the player using that token
    private SparseIntArray tokenIVMap;


    private View mContentView;
    private ConstraintLayout boardPanelCL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create board activity as a full screen activity
        setContentView(R.layout.activity_fullscreen);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // initialize singleton rendering objects
        visualAssetManager = new VisualAssetManager(this);

        // init layout references
        boardPanelCL = findViewById(R.id.board_panel_cl);
        tokenIVMap = new SparseIntArray();
        startNewGame();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy() has just been called!");

        // tear down all references to this activity we've passed to other classes
        if (visualAssetManager != null) {
            visualAssetManager.onDestroy();
        }

    }

    /**
     * Build (or re-build) all game and state objects from scratch to start a new game.
     */
    protected void startNewGame() {
        Log.v(TAG, "A new game is starting now...");
        this.board = new Board();

        // add tokens to board
        board.addPlayerToken(1);        // data model
        drawTokenOntoBoard(1);          // visual manifestation of data model

        Button rollButton = findViewById(R.id.roll_dice_btn);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });

    }

    /**
     * When the "roll" button is pressed, generate two new dice values and update the images displayed on the dice
     */
    protected void rollDice() {
        // roll & retrieve new values for both dice
        board.rollDice();
        render();

        // show property action popup if we're on the right kind of a space
        int position = board.getTokenPosition(1);

        // check if position is a property space; if it is show a popup
        if (board.getSpaceTypeForPosition(position) == SpaceType.PROPERTY) {
            // get ID reference to the property card graphic for this space on the board; pass it to the dialog
            int propDrawableID = visualAssetManager.getPropertyCardDrawableID(position);
            showPropertyActionModal(propDrawableID);
        } else {
            Log.v(TAG, String.format("position '%d' isn't a property space, so we're not going to do anything yet!", position));
        }



    }

    /**
     * Call to repaint the game's state to the screen.
     */
    private void render() {
        // Repaint dice values
        ImageView die1 = findViewById(R.id.die_1_iv);
        ImageView die2 = findViewById(R.id.die_2_iv);
        int[] r = board.getDiceValues();
        die1.setImageDrawable(visualAssetManager.getDieFace(r[0]));
        die2.setImageDrawable(visualAssetManager.getDieFace(r[1]));

        // move our single token along the board
        board.incrementTokenPosition(1, r[0]+r[1]);
        drawTokenAtPosition(1, board.getTokenPosition(1));
    }

    private void showPropertyActionModal(int propertyDrawableID) {
        // try to show a dialog fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("propertyDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // pass the property card's drawable id to the dialog fragment we're building
        Bundle b = new Bundle();
        b.putInt(PropertyActionDialogFragment.KEY_PROPERTY_DRAWABLE_ID, propertyDrawableID);
        DialogFragment dialogFragment = new PropertyActionDialogFragment();
        dialogFragment.setArguments(b);

        // present the dialog
        dialogFragment.show(ft, "propertyDialog");
    }

    /**
     * Initializes an ImageView for a token and places it on Go.
     * @param tokenID
     */
    private void drawTokenOntoBoard(int tokenID) {
        // create the new token
        ImageView tokenIV = new ImageView(this);
        tokenIV.setId(View.generateViewId());
        Drawable d = visualAssetManager.getTokenDrawable(tokenID);
        tokenIV.setImageDrawable(d);
        tokenIV.setVisibility(View.INVISIBLE);
        int tokenHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        tokenIV.setLayoutParams(new ConstraintLayout.LayoutParams(tokenHeight, tokenHeight));

        // add it to the board layout
        boardPanelCL.addView(tokenIV);
        ConstraintSet newSet = new ConstraintSet();
        newSet.clone(boardPanelCL);

        // center the new token on the Go space
        newSet.centerHorizontally(tokenIV.getId(), R.id.tile_go_iv);
        newSet.centerVertically(tokenIV.getId(), R.id.tile_go_iv);

        // apply new constraints to board layout and display the new token
        newSet.applyTo(boardPanelCL);
        tokenIV.setVisibility(View.VISIBLE);

        // add the *view ID* of this new IV to the map so it can be referenced by tokenID elsewhere
        tokenIVMap.put(tokenID, tokenIV.getId());

    }

    private void drawTokenAtPosition(int tokenID, int p) {
        // get a reference to the ImageView to re-locate
        int viewID = tokenIVMap.get(tokenID);
        ImageView tokenIV = findViewById(viewID);
        //tokenIV.setVisibility(View.INVISIBLE);

        // clear all assoc. constraints
        ConstraintSet newSet = new ConstraintSet();
        newSet.clone(boardPanelCL);
        newSet.clear(viewID);

        // new constraints will center the marker within the new tile on the board
        ImageView newTileIV = visualAssetManager.getIVForBoardPosition(p);
        int tokenHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        newSet.constrainHeight(viewID, tokenHeight);
        newSet.constrainWidth(viewID, tokenHeight);
        newSet.centerVertically(viewID, newTileIV.getId());
        newSet.centerHorizontally(viewID, newTileIV.getId());

        // apply
        TransitionManager.beginDelayedTransition(boardPanelCL);
        newSet.applyTo(boardPanelCL);
        tokenIV.setVisibility(View.VISIBLE);
    }


}
