package io.andrys.monopoly.exceptions;

/**
 * UnownedPropertyException.java // Monopoly
 * Tony Andrys (tony@andrys.io)
 * Copyright 2019 - All rights reserved
 */

/**
 * Thrown when the ownership of an unowned Property is interrogated.
 */
public class UnownedPropertyException extends Exception {
    public UnownedPropertyException() {}
    public UnownedPropertyException(String message) {
        super(message);
    }
}
