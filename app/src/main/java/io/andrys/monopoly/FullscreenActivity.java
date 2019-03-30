package io.andrys.monopoly;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private View mContentView;


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

    }
}
