package org.example.neotaexercise.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;
import org.junit.jupiter.api.Test;

import static org.example.neotaexercise.domain.ProcessCommand.Command.START_PROCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Daniel Slavik
 */
class StartWorkflowSessionCommandTest {

    @Test
    void support_test() {
        final var tested = new StartWorkflowSessionCommand(id -> Optional.empty(), (s, session) -> {}, command -> {});
        assertTrue(tested.support("start-workflow"));
        assertTrue(tested.support("start-workflow workflowId"));
        assertFalse(tested.support("someCommand"));
    }

    @Test
    void help_test() {
        final var tested = new StartWorkflowSessionCommand(id -> Optional.empty(), (s, session) -> {}, command -> {});
        assertEquals("start-workflow workflowId - starts a workflow session", tested.printHelp());
    }

    @Test
    void process_test() throws CommandException {
        final var commands = new ArrayList<ProcessCommand>(1);
        final var sessionRepo = new HashMap<String, WorkflowSession>(1);

        final var tested = new StartWorkflowSessionCommand(
            id -> Optional.of(new WorkflowDefinition(null, null, null, null, null)),
            sessionRepo::put,
            commands::add);

        final var out = new ByteArrayOutputStream();

        tested.consume(new PrintStream(out), "start-workflow workflowId");
        final var result = out.toString();
        assertTrue(result.startsWith("Started a workflow session with id "));

        final var sessionId = result.substring(35).replaceAll("\\s+", "");

        assertEquals(1, commands.size());
        final var command = commands.get(0);
        assertEquals(START_PROCESS, command.getCommandType());
        assertEquals(sessionId, command.getSessionId());

        assertEquals(1, sessionRepo.size());
        assertTrue(sessionRepo.containsKey(sessionId));
        final var session = sessionRepo.get(sessionId);
        assertEquals(Optional.empty(), session.getCurrentNode());
        assertEquals("workflowId", session.getDefinitionId());
    }

    @Test
    void process_no_workflow_test() throws CommandException {
        final var commands = new ArrayList<ProcessCommand>(1);
        final var sessionRepo = new HashMap<String, WorkflowSession>(1);

        final var tested = new StartWorkflowSessionCommand(
            id -> Optional.empty(),
            sessionRepo::put,
            commands::add);

        final var out = new ByteArrayOutputStream();

        try {
            tested.consume(new PrintStream(out), "start-workflow workflowId");
            fail("No definition exception expected");
        } catch (final CommandFormatException e) {
            assertEquals("No workflow with id workflowId found", e.getMessage());
        }
        assertEquals(0, out.toString().length());
        assertEquals(0, commands.size());
        assertEquals(0, sessionRepo.size());
    }

    @Test
    void process_no_param_test() throws CommandException {
        final var commands = new ArrayList<ProcessCommand>(1);
        final var sessionRepo = new HashMap<String, WorkflowSession>(1);

        final var tested = new StartWorkflowSessionCommand(
            id -> Optional.empty(),
            sessionRepo::put,
            commands::add);

        final var out = new ByteArrayOutputStream();

        try {
            tested.consume(new PrintStream(out), "start-workflow");
            fail("No definition exception expected");
        } catch (final CommandFormatException e) {
            assertEquals("workflowId is missing. Expected command in format start-workflow workflowId. Got start-workflow", e.getMessage());
        }
        assertEquals(0, out.toString().length());
        assertEquals(0, commands.size());
        assertEquals(0, sessionRepo.size());
    }
}