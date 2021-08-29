/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command.validation;

/**
 * @author Daniel Slavik
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
