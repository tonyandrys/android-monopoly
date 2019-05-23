package io.andrys.monopoly.audio;

/**
 * AudioEngine.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.content.Context;
import android.support.annotation.RawRes;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// TODO: onDestroy cleanup methods!!
/**
 * A threaded (asynchronous) audio engine designed around the producer/consumer model.
 *
 * An object can trigger playback of an audio file by calling the {@link #enqueueAudio(int)} method
 * and passing the resource ID for an audio file in the /res/raw directory as an argument. This creates
 * an AudioEvent object which represents the request.
 *
 * AudioEvents are added to a shared queue that is processed by multiple threads (AudioConsumers).
 * When an AudioEvent is consumed by a thread 't', t instantiates a MediaPlayer, loads the requested
 * audio file into the player, plays the audio, then de-allocates the MediaPlayer.
 */
public class AudioEngine {

    private final String TAG = this.getClass().getSimpleName();

    // Context used by all consumers
    private Context context;

    // The shared audio queue
    final private int BLOCKING_QUEUE_CAPCITY = 10;
    final private BlockingQueue<AudioEvent> audioQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPCITY);

    // Threads
    private AudioConsumer ac1;
    private AudioConsumer ac2;

    /**
     * Constructs a new AudioEngine instance and initializes two child AudioConsumers.
     * @param context
     */
    public AudioEngine(Context context) {
        this.context = context;
        this.ac1 = new AudioConsumer(context, audioQueue);
        this.ac2 = new AudioConsumer(context, audioQueue);
        this.ac1.start();
        this.ac2.start();
        Log.v(TAG, "AudioEngine started with 2 threads.");
    }

    /**
     * Adds a request to play an audio file to the audio engine's shared queue.
     * @param resourceID identifier of an audio resource in /res/raw/
     */
    public void enqueueAudio(@RawRes int resourceID) {
        AudioEvent ae = new AudioEvent(resourceID);
        boolean wasAdded = audioQueue.offer(ae);
        if (!wasAdded) {
            Log.v(TAG, String.format(TAG, "Discarded %s; shared AudioEvent queue is full!", ae.toString()));
        }
    }


}
