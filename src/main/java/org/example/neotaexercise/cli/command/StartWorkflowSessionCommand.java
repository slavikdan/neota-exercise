/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import java.io.PrintStream;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;

import static org.example.neotaexercise.util.CommandUtils.parseIdOrElseThrow;


/**
 * @author Daniel Slavik
 */
public class StartWorkflowSessionCommand implements CliCommand {

    private static final String COMMAND = "start-workflow";

    private final Function<String, Optional<WorkflowDefinition>> getDefinition;
    private final BiConsumer<String, WorkflowSession> storeSession;
    private final Consumer<ProcessCommand> startSession;

    public StartWorkflowSessionCommand(
        Function<String, Optional<WorkflowDefinition>> getDefinition,
        BiConsumer<String, WorkflowSession> storeSession,
        Consumer<ProcessCommand> startSession
    ) {
        this.getDefinition = getDefinition;
        this.storeSession = storeSession;
        this.startSession = startSession;
    }

    @Override
    public boolean support(String command) {
        return command.startsWith(COMMAND);
    }

    @Override
    public void consume(PrintStream out, String command) throws CommandException {

        final var id = parseIdOrElseThrow(command, COMMAND, "workflowId");

        final var sessionId = UUID.randomUUID().toString();

        storeSession.accept(sessionId, new WorkflowSession(id));
        startSession.accept(new ProcessCommand(sessionId, ProcessCommand.Command.START_PROCESS));

        out.println("Started a workflow session with id " + sessionId);
    }

    @Override
    public String printHelp() {
        return COMMAND + " workflowId - starts a workflow session";
    }
}
