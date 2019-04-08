package io.andrys.monopoly;

/**
 * RawUtils.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Methods that deal with reading/writing/manipulating files in the /res/raw directory
 */
public class RawUtils {
    final private static String TAG = "RawUtils";

    private static InputStream openRawResource(@NonNull final Context context, @RawRes final int rawResourceId) {
        return context.getResources().openRawResource(rawResourceId);
    }

    public static String readRawResource(Context context, @RawRes int rawResourceId) {
        InputStream inputStream = null;
        try {
            inputStream = openRawResource(context, rawResourceId);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            return new String(b);
        } catch (Exception e) {
            Log.e(TAG, "readRawResource: FAILED!", e);
            return "";
        } finally {
            try {
                inputStream.close();
            } catch (Throwable closeError) {
                Log.e(inputStream.getClass().getSimpleName(), "Failed to close raw resource", closeError);
            }
        }
    }
}
