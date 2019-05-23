package io.andrys.monopoly.audio;

/**
 * AudioEvent.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.support.annotation.RawRes;

/**
 * A request to play an audio file referenced by its resource id (in /res/raw).
 *
 * These requests are handled by the AudioEngine class.
 */
public class AudioEvent {

    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    int eventID;
    long ts;
    int resourceID;

    /**
     * Creates a new event that plays the audio file specified by its raw resource ID.
     * @param resourceID
     */
    public AudioEvent(@RawRes int resourceID) {
        this.eventID = getShortCode();
        this.ts = System.currentTimeMillis() / 1000L;
        this.resourceID = resourceID;
    }

    /**
     * Truncated hash code of this object. Identical to the implementation in GameState.
     * @return
     */
    public int getShortCode() {
        return (this.hashCode() & 0xFF);
    }

    @Override
    public String toString() {
        return "AudioEvent{" +
                "eventID=" + eventID +
                ", ts=" + ts +
                ", resID=" + resourceID +
                '}';
    }
}
