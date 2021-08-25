/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;

import static org.example.neotaexercise.util.CommandUtils.parseIdOrElseThrow;


/**
 * @author Daniel Slavik
 */
public class StartWorkflowSessionCommand implements CliCommand {

    private static final String COMMAND = "start-workflow";

    private final Function<String, Optional<WorkflowDefinition>> getDefinition;
    private final BiConsumer<String, WorkflowSession> startSession;

    public StartWorkflowSessionCommand(
        Function<String, Optional<WorkflowDefinition>> getDefinition,
        BiConsumer<String, WorkflowSession> startSession
    ) {
        this.getDefinition = getDefinition;
        this.startSession = startSession;
    }

    @Override
    public boolean support(String command) {
        return command.startsWith(COMMAND);
    }

    @Override
    public void consume(PrintStream out, String command) throws CommandException {

        final var id = parseIdOrElseThrow(command, COMMAND, "workflowId");

        final var startingNode = getDefinition.apply(id)
            .map(WorkflowDefinition::getStartingNode)
            .orElseThrow(() -> new CommandFormatException("Workflow definition with id " + id + " does not exist"));

        final var sessionId = UUID.randomUUID().toString();

        startSession.accept(sessionId, new WorkflowSession(startingNode, id));

        out.println("Started a workflow session with id " + sessionId);
    }

    @Override
    public String printHelp() {
        return COMMAND + " workflowId - starts a workflow session";
    }
}
