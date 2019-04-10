package io.andrys.monopoly;

/**
 * GameEngine.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * This will be the game engine that manages the current state, performs the actions in the state object, modifies the
 * game context (which I think will be Game.java) appropriately,
 */

import android.util.Log;

import java.util.ArrayDeque;
import java.util.EmptyStackException;

import io.andrys.monopoly.states.GameState;

/**
 * The state objects I think should be something like
 *
 * public class StateObj {
 *     + Game stateContext
 *     + ENUM NAME_OF_STATE
 *     function onStateEnter()
 *     function execute()
 *     function onStateExit()
 * }
 *
 * It'll also need a way to send view updates to FullScreenActivity, so the activity will prob need to be
 * passed in GameEngine's constructor
 */
public class GameEngine {
    private final String TAG = this.getClass().getSimpleName();

    FullscreenActivity activity;

    // The "current" state is always at the top of the stack.
    // Popping the current state off the stack will transition to the one below it, and the popped
    // state will be destroyed.
    // Pushing a new state onto the stack
    ArrayDeque<GameState> stateStack;
    boolean isRunning;

    public GameEngine(FullscreenActivity activity) {
        this.activity = activity;
        this.stateStack = new ArrayDeque<GameState>();
        this.isRunning = true;
    }

    /**
     * Pushes a new state 's' onto the state stack; this will transition out of the current state
     * and into the state we just pushed.
     * @param newState
     */
    public void pushState(GameState newState) {
        // transition out of the old state if one exists
        if (stateStack.size() > 0) {
            stateStack.peek().onStateExit();
        }

        // transition into the state we just pushed
        stateStack.push(newState);
        assert stateStack.peek() != null;
        stateStack.peek().onStateEnter();
        stateStack.peek().execute();
    }

    /**
     * Transitions out of the current state (the state at the top of the stack) and into the state
     * below it on the stack.
     *
     * The popped state is destroyed. (though we could return it here if necessary?)
     */
    public void popState() {
        try {
            assert stateStack.peek() != null;
            // transition out of the current state
            stateStack.peek().onStateExit();
            GameState currentState = stateStack.pop();  // made a local variable so you can extract the gameContext out of this if you want

            // if there is a state below this, resume it by transitioning into it
            if (stateStack.peek() != null) {
                GameState prevState = stateStack.peek();
                prevState.onStateEnter();
                prevState.execute();
            } else {
                isRunning = false;
            }
        } catch (EmptyStackException e) {
            Log.e(TAG, "Cannot pop an empty state stack!");
        } catch (NullPointerException e) {
            Log.e(TAG, "popState() tried to peek() an empty stack!");
        }
    }

    /**
     * Transition to a new GameState; make the provided GameState the new active state.
     * @param s State to transition to
     */
    public void changeState(GameState s) {
        if (stateStack.size() > 0) {
            popState();
        }
        pushState(s);
        stateStack.peek().onStateEnter();
        stateStack.peek().execute();
    }

    /**
     * Wipes out all states in the stack
     */
    public void clearStack() {
        int deletedStates = stateStack.size();
        stateStack.clear();
        Log.v(TAG, String.format("Cleared '%d' states from the stack.", deletedStates));
    }

    private void reportStatus() {
        if (this.isRunning) {
            Log.v(TAG, "Engine is running.");
        } else {
            Log.v(TAG, "Engine has stopped.");
        }
    }

    /**
     * Returns an instance of the activity that holds the game views.
     * Right now, that's a Fullscreen activity.
     * @return FullscreenActivity
     */
    public FullscreenActivity getParentActivity() {
        return this.activity;
    }
}
