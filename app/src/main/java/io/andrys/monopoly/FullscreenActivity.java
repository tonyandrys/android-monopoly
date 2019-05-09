package io.andrys.monopoly;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import io.andrys.monopoly.states.NewGameState;
import io.andrys.monopoly.states.UnownedPropertyState;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private Board board;
    public VisualAssetManager visualAssetManager;
    private GameEngine engine;

    // Maps tokenIDs to their ImageViews that display their position on the board.
    private SparseIntArray tokenIVMap;

    // Maps board positions to drawables of houses/hotels drawn on top of them
    private SparseArray<int[]> positionDevIDMap;

    // turn counter
    private int turnCount;

    // View references
    private View mContentView;
    private ConstraintLayout boardPanelCL;
    private ScoreTableLayout scoreTableTL;
    private TextView turnCountTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create board activity as a full screen activity
        setContentView(R.layout.exp_game_layout);
        mContentView = findViewById(R.id.root_cl);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // initialize singleton rendering objects
        visualAssetManager = new VisualAssetManager(this);

        // init layout references and data structures
        boardPanelCL = findViewById(R.id.root_cl);
        scoreTableTL = findViewById(R.id.score_table_tl);
        tokenIVMap = new SparseIntArray();
        positionDevIDMap = new SparseArray<>();

        // initialize turn counter & draw it on the screen
        turnCount = 0;
        //turnCountTV = findViewById(R.id.turn_count_tv);
        //repaintTurnCounter();

        // start the game engine
        startGameEngine();

    }

    @Override
    public void onResume() {
        super.onResume();
        // re-hide the system bars when this activity regains focus
        updateUI();
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
     * Call to re-apply the UI visibility flags & hide the system bars again.
     */
    public void updateUI() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                }
            }
        });
    }

    /**
     * Builds the core game objects from scratch, creates a set of players (just me for now),
     * and starts the game engine.
     */
    protected void startGameEngine() {
        Log.v(TAG, "Starting the game engine now...");

        // build core objects
        Board b = new Board();
        PropertyManager pm = new PropertyManager(this);

        // build players and player list
        ArrayDeque<Player> players = new ArrayDeque<>();
        String p1_color = "#FFB8E986";
        Player me = new Player("Tony", 1, p1_color);
        players.add(me);
        String p2_color = "#FF006497";
        Player player2 = new Player("Alice", 2, p2_color);
        players.add(player2);
        String p3_color = "#FFD22630";
        Player player3 = new Player("Bob", 3, p3_color);
        players.add(player3);
        String p4_color = "#FF8B6A8B";
        Player player4 = new Player("Eve", 4, p4_color);
//        players.add(player4);

        // add players to the score table layout
        for (Player p: players) {
            scoreTableTL.addPlayerRow(p);
        }

        // build the game engine and construct the initial game context state
        engine = new GameEngine(this);
        GameContext next = new GameContext(null, null, players, b, pm);
        engine.changeState(new NewGameState(engine, next));

    }

    /**
     * Build (or re-build) all game and state objects from scratch to start a new game.
     */
    /*protected void startNewGame() {
        Log.v(TAG, "A new game is starting now...");
        this.board = new Board();

        // initialize the property manager; this loads all properties from disk and provides
        // an interface to assign them to players
        PropertyManager pm = new PropertyManager(this);

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
    }*/





    /**
     * When the "roll" button is pressed, generate two new dice values and update the images displayed on the dice
     */
    protected void rollDice() {
        // roll & retrieve new values for both dice
        board.rollDice();

        // show property action popup if we're on the right kind of a space
        int position = board.getTokenPosition(1);

        // check if position is a property space; if it is show a popup
        if (board.getSpaceTypeForPosition(position) == Board.SpaceType.PROPERTY) {
            // get ID reference to the property card graphic for this space on the board; pass it to the dialog
            int propDrawableID = visualAssetManager.getPropertyCardDrawableID(position);
            //showPropertyActionModal(propDrawableID, 1);
        } else {
            Log.v(TAG, String.format("position '%d' isn't a property space, so we're not going to do anything yet!", position));
        }

    }

    private void repaintTurnCounter() {
        String ts = String.format(Locale.US, "turn=%d", turnCount);
        turnCountTV.setText(ts);
    }

    public void incrementTurnCount() {
        turnCount++;
        //repaintTurnCounter();
    }

    /**
     * Updates the visual representation of a Property space on the board by
     * tinting it with the owner's color and re-drawing the number of houses/hotels on it.
     * @param position              Board position of the space to update
     * @param owner                 Player that owns the property
     * @param levelOfDevelopment    Level of development to render (pass 0 if not a street property!)
     */
    public void redrawPropertyAtPosition(int position, Player owner, int levelOfDevelopment) {
        ImageView propertyIV = visualAssetManager.getIVForBoardPosition(position);
        propertyIV.setColorFilter(owner.getTransparentColor(), PorterDuff.Mode.SRC_OVER);
    }

    /**
     * Displays the modal with Buy/Auction/Manage command buttons for a specific property.
     * @param caller UnownedPropertyState instance that requested that we display this modal
     * @param position Board position of the property the player is currently on
     * @param shouldEnableBuyButton state of the buy button in the modal
     */
    public void showPropertyActionModal(UnownedPropertyState caller, int position, boolean shouldEnableBuyButton) {
        if (BuildConfig.DEBUG && ((position < 0) || (position >= 40))) {
            throw new AssertionError(String.format("showPropertyActionModal got an invalid position -> %d", position));
        }

        // get the ID of the picture of the property card, use it to inflate the modal
        int propertyDrawableID = visualAssetManager.getPropertyCardDrawableID(position);

        // try to show a dialog fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("propertyDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // pass the property card's drawable id and position to the dialog fragment we're building
        Bundle b = new Bundle();
        b.putInt(PropertyActionDialogFragment.KEY_PROPERTY_DRAWABLE_ID, propertyDrawableID);
        b.putInt(PropertyActionDialogFragment.KEY_PROPERTY_POSITION, position);
        b.putBoolean(PropertyActionDialogFragment.KEY_BUY_BUTTON_STATE, shouldEnableBuyButton);
        PropertyActionDialogFragment dialogFragment = new PropertyActionDialogFragment();
        dialogFragment.setArguments(b);

        // the state that called this method should be sent click events
        dialogFragment.setButtonListener(caller);

        // present the dialog
        dialogFragment.show(ft, "propertyActionDialog");
    }

    /**
     * Displays a new house drawable at a position on the board.
     * @param position position in [0,39]
     */
    public void drawHouseAtPosition(int position) {
        // don't do anything if there are already four houses on this property
        if (positionDevIDMap.get(position, new int[0]).length == 4) {
            throw new IllegalStateException(String.format("Position '%d' has the maximum amount of houses! Build a hotel instead!", position));
        }
        // get important building objects
        ViewBuilder vb = ViewBuilder.getInstance();
        ConstraintGenerator cg = ConstraintGenerator.getInstance();

        // construct a new house IV for this side of the board this position is on
        int[] existingHouseIDs = positionDevIDMap.get(position, new int[0]);
        boolean propHasExistingHouses = !(existingHouseIDs.length == 0);
        VisualAssetManager.BoardSide posBoardSide = visualAssetManager.getBoardSideForPosition(position);
        ImageView houseIV = vb.buildHouseIV(this, posBoardSide, propHasExistingHouses);

        // add the new house to the view & clone the existing layout
        boardPanelCL.addView(houseIV);
        ConstraintSet newSet = new ConstraintSet();
        newSet.clone(boardPanelCL);

        // calculate & apply new constraints for this house
        int propID = visualAssetManager.getIVForBoardPosition(position).getId();
        newSet = cg.calculateHouseConstraints(houseIV.getId(), existingHouseIDs, propID, posBoardSide, newSet);
        newSet.applyTo(boardPanelCL);
        houseIV.setVisibility(View.VISIBLE);

        // Add the ID of the new house to this property's house cache
        int numExistingHouses = existingHouseIDs.length;
        int[] newHouseIDCache = new int[numExistingHouses+1];
        for (int i=0; i<numExistingHouses; i++) {
            newHouseIDCache[i] = existingHouseIDs[i];
        }
        newHouseIDCache[newHouseIDCache.length-1] = houseIV.getId();
        positionDevIDMap.put(position, newHouseIDCache);
    }

    /**
     * Removes a house drawable from a position on the board
     * @param position position in [0,39]
     */
    public void removeHouseAtPosition(int position) {
        int[] existingHouseIDs = positionDevIDMap.get(position, new int[0]);
        if (existingHouseIDs.length > 0) {
            // get a reference to the last-built house so we can remove it
            int rhsHouseID = existingHouseIDs[existingHouseIDs.length-1];
            boardPanelCL.removeView(findViewById(rhsHouseID));
            Log.v(TAG, String.format("Removed a house drawable at position '%d'.", position));

            // store the remaining house IDs back in the cache if there are any.
            int remainingHouseCount = existingHouseIDs.length-1;
            if (remainingHouseCount > 0) {
                int[] newHouseIDCache = new int[remainingHouseCount];
                for (int i=0; i<remainingHouseCount; i++) {
                    newHouseIDCache[i] = existingHouseIDs[i];
                }
                positionDevIDMap.put(position, newHouseIDCache);
            } else {
                // wipe out the cache for this property if we've removed all the houses
                positionDevIDMap.delete(position);
            }
        } else {
            throw new IllegalArgumentException(String.format("Position '%d' has no house drawables that can be removed!", position));
        }
    }

    /**
     * Initializes an ImageView for a token and places it on Go.
     * @param tokenID
     */
    public void drawTokenOntoBoard(int tokenID) {
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

    // TODO: Now this method would make more sense semantically if it were called something like "moveTokenPosition" or "updateTokenPosition"
    /**
     * Moves a token's ImageView to a specific position on the board.
     * @param tokenID
     * @param p
     * @param listener a TransitionListener that should receive transition status notifications
     */
    public void drawTokenAtPosition(int tokenID, int p, Transition.TransitionListener listener) {
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

        // apply & animate changes
        Transition t = new AutoTransition();
        t.addListener(listener);
        TransitionManager.beginDelayedTransition(boardPanelCL, t);
        newSet.applyTo(boardPanelCL);
        tokenIV.setVisibility(View.VISIBLE);
    }


}
