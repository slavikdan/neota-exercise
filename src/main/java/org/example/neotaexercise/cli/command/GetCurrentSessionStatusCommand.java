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

        if (isStarting(session, definition)) {
            out.println("Starting");
            return;
        }

        if (isFinished(session, definition)) {
            out.println("Ended");
            return;
        }

        out.println(getLine(session, definition) + " " + getTask(session, definition) + " " + getProcessStatus(session));

    }

    private static String getProcessStatus(WorkflowSession session) {
        return session.isBeingProcessed() ? "in progress" : "completed";
    }

    private static String getTask(WorkflowSession session, WorkflowDefinition definition) throws CommandException {
        return definition.getNode(session.getCurrentNode())
            .map(WorkflowDefinitionDto.Node::getName)
            .orElseThrow(() -> new CommandException("No node " + session.getCurrentNode() + " specified"));
    }

    private static String getLine(WorkflowSession session, WorkflowDefinition definition) throws CommandException {
        return definition.getNodeLane(session.getCurrentNode())
            .map(WorkflowDefinitionDto.Lane::getName)
            .orElseThrow(() -> new CommandException("No lane for node " + session.getCurrentNode() + " specified"));
    }

    private static boolean isFinished(WorkflowSession session, WorkflowDefinition definition) {
        return session.getCurrentNode().equals(definition.getEndNode());
    }

    private static boolean isStarting(WorkflowSession session, WorkflowDefinition definition) {
        return session.getCurrentNode().equals(definition.getStartingNode());
    }

    @Override
    public String printHelp() {
        return COMMAND + " sessionId - gets current session state";
    }
}
