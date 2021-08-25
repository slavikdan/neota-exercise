/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.util;

import org.example.neotaexercise.cli.command.CommandFormatException;


/**
 * @author Daniel Slavik
 */
public final class CommandUtils {

    private CommandUtils() {
        //do not permit instantiation
    }

    public static String parseIdOrElseThrow(final String input, final String command, final String idName) throws CommandFormatException {
        if (input.length() <= command.length() + 1) {
            throw new CommandFormatException(idName + " is missing. Expected command in format " + command + " " + idName + ". Got " + command);
        }

        return input.substring(command.length() + 1);

    }
}
