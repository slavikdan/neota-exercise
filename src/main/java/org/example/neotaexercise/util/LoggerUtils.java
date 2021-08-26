/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.util;

import java.io.PrintStream;
import org.example.neotaexercise.config.BaseConfiguration;


/**
 * @author Daniel Slavik
 */
public final class LoggerUtils {

    private LoggerUtils() {
        //do not permit instantiation
    }

    public static void logDebug(final PrintStream out, final String message) {
        if (BaseConfiguration.LogLevel.DEBUG.equals(BaseConfiguration.logLevel)) {
            out.println(message);
        }
    }
}
