/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Function;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static org.example.neotaexercise.util.CommandUtils.parseIdOrElseThrow;


/**
 * @author Daniel Slavik
 */
public class GetCurrentSessionStatusCommand implements CliCommand {

    private static final String COMMAND = "session-state";

    private final Function<String, Optional<WorkflowSession>> workflowSession;

    private final Function<String, Optional<WorkflowDefinition>> workflowDefinition;

    public GetCurrentSessionStatusCommand(
        Function<String, Optional<WorkflowSession>> workflowSession,
        Function<String, Optional<WorkflowDefinition>> workflowDefinition
    ) {
        this.workflowSession = workflowSession;
        this.workflowDefinition = workflowDefinition;
    }

    @Override
    public boolean support(String command) {
        return command.startsWith(COMMAND);
    }

    @Override
    public void consume(PrintStream out, String command) throws CommandException {
        final var id = parseIdOrElseThrow(command, COMMAND, "sessionId");

        final var session = workflowSession.apply(id)
            .orElseThrow(() -> new CommandFormatException("No session with id " + id + " found"));
        final var definition = workflowDefinition.apply(session.getDefinitionId())
            .orElseThrow(() -> new CommandFormatException("No definition with id " + session.getDefinitionId() + " found"));

        if (isStarting(session)) {
            out.println("Starting");
            return;
        }

        if (isFinished(session, definition)) {
            out.println("Ended");
            return;
        }

        if (isProcessed(session)) {
            out.println(session.getCurrentNode()
                .flatMap(definition::getNode)
                .map(n -> Optional.ofNullable(n.getName()).orElse(n.getId()))
                .orElse("There should be one ") + " in progress");
            return;
        }

        session.getCurrentNode()
            .flatMap(definition::getNextNode)
            .flatMap(definition::getNodeLane)
            .map(WorkflowDefinitionDto.Lane::getName)
            .ifPresentOrElse(out::println, () -> out.println("Something is missing somewhere"));


    }

    private boolean isProcessed(WorkflowSession session) {
        return session.isBeingProcessed();
    }

    private static boolean isFinished(WorkflowSession session, WorkflowDefinition definition) {
        return session.getCurrentNode()
            .map(n -> n.equals(definition.getEndNode()))
            .orElse(false)
            ;
    }

    private static boolean isStarting(WorkflowSession session) {
        return session.getCurrentNode().isEmpty();
    }

    @Override
    public String printHelp() {
        return COMMAND + " sessionId - gets current session state";
    }
}
