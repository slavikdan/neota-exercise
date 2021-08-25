/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import java.io.PrintStream;


/**
 * @author Daniel Slavik
 */
public interface CliCommand {

    boolean support(String command);

    void consume(PrintStream out, String command) throws CommandException;

    String printHelp();
}
