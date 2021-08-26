/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.config;

import java.time.Duration;


/**
 * @author Daniel Slavik
 */
public class BaseConfiguration {

    public static volatile Duration handlerSleepDuration = Duration.ofSeconds(60);

    public static volatile LogLevel logLevel = LogLevel.DEBUG;

    public enum LogLevel {
        INFO,
        DEBUG
    }
}
