package io.andrys.monopoly;

/**
 * GameView.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * View contract methods to be triggered by the presenter layer.
 */
public interface GameView {
    void render();
    void drawTokenOntoBoard(int tokenID);
    void drawTokenAtPosition(int tokenID, int p);
    void showPropertyActionModal(int propertyDrawableID);
}
