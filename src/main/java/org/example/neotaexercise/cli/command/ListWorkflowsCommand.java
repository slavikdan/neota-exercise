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
public class ListWorkflowsCommand implements CliCommand {

    private static final String COMMAND = "list-workflows";

    private final Supplier<Set<String>> workflowIds;

    public ListWorkflowsCommand(Supplier<Set<String>> workflowIds) {
        this.workflowIds = workflowIds;
    }

    @Override
    public boolean support(String command) {
        return COMMAND.equalsIgnoreCase(command);
    }

    @Override
    public void consume(final PrintStream out, String command) throws CommandException {
        out.println("Existing workflow definitions:");
        workflowIds.get().forEach(out::println);
    }

    @Override
    public String printHelp() {
        return COMMAND + " - lists all stored workflow definitions";
    }
}
