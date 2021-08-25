/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import java.io.PrintStream;
import java.util.Set;
import java.util.function.Supplier;


/**
 * @author Daniel Slavik
 */
public class ListWorkflowSessionsCommand implements CliCommand {

    private static final String COMMAND = "list-sessions";

    private Supplier<Set<String>> allSessionIds;

    public ListWorkflowSessionsCommand(Supplier<Set<String>> getAllSessionIds) {
        this.allSessionIds = getAllSessionIds;
    }

    @Override
    public boolean support(String command) {
        return command.equals(COMMAND);
    }

    @Override
    public void consume(PrintStream out, String command) throws CommandException {
        out.println("Existing workflow sessions:");
        allSessionIds.get().forEach(out::println);
    }

    @Override
    public String printHelp() {
        return COMMAND + " - lists all workflow sessions";
    }
}
