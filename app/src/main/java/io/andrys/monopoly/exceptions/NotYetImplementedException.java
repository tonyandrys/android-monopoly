package io.andrys.monopoly.exceptions;

/**
 * NotYetImplementedException.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * A brilliant invention by Jens Bannmann (@JensBannmann) from StackOverflow.
 * See: https://stackoverflow.com/a/50461711/870175
 */
public class NotYetImplementedException extends RuntimeException {
    /**
     * @deprecated Deprecated to remind you to implement the corresponding code
     *             before releasing the software.
     */
    @Deprecated
    public NotYetImplementedException() {
    }

    /**
     * @deprecated Deprecated to remind you to implement the corresponding code
     *             before releasing the software.
     */
    @Deprecated
    public NotYetImplementedException(String message) {
        super(message);
    }
}