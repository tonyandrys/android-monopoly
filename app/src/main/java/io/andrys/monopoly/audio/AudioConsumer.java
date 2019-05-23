package io.andrys.monopoly.audio;

/**
 * AudioConsumer.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import io.andrys.monopoly.R;

/**
 * Consumer thread used in the AudioEngine.
 */
public class AudioConsumer extends Thread implements MediaPlayer.OnCompletionListener {

    private final String TAG = String.format("%s[%s]", this.getClass().getSimpleName(), this.getShortCode());

    private final Context context;
    private final BlockingQueue<AudioEvent> queue;

    private MediaPlayer mediaPlayer;

    AudioConsumer(Context c, BlockingQueue q) {
        this.context = c;
        this.queue = q ;
    }

    @Override
    public void run() {
        Log.i(TAG, "READY TO CONSUME AUDIO");

        // Try to consume, thread should block if no data is available to get.
        // Blocking might happen automatically. It might not. Who nose?
        while (true) {
            try {
                // let this thread receive MediaPlayer completion events
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(this);

                // pull data off BlockingQueue
                AudioEvent ae = queue.take();
                Log.i(TAG, String.format("consumed -> %s", ae.toString()));

                // get a reference to the audio file to play as an AssetFileDescriptor
                String resourceName = context.getResources().getResourceName(ae.resourceID);
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(ae.resourceID);
                if (afd == null) {
                    Log.e(TAG, String.format("Could not create AFD for %s; does the referenced file exist?", ae));
                    continue;
                }

                // Prepare to play the audio file
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.prepare();

                // fire the audio
                Log.v(TAG, String.format("playing audio file %s", resourceName));
                mediaPlayer.start();

            } catch (InterruptedException e) {
                Log.i(TAG, "Thread interrupted!");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // When a sound is finished playing, release the MediaPlayer that fired it
    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
    }

    /**
     * This is just the hash code but shortened so I can append it to things in the
     * small debugging window without breaking lines
     * @return
     */
    public int getShortCode() {
        return (this.hashCode() & 0xFF);
    }


}
