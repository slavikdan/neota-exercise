package org.example.neotaexercise.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.example.neotaexercise.domain.ProcessCommand;
import org.junit.jupiter.api.Test;

import static org.example.neotaexercise.domain.ProcessCommand.Command.RESUME_PROCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Daniel Slavik
 */
class ResumeSessionCommandTest {

    @Test
    void support_test() {
        final var tested = new ResumeSessionCommand(id -> true, c -> {});
        assertTrue(tested.support("resume-session"));
        assertTrue(tested.support("resume-session sessionId"));
        assertFalse(tested.support("someCommand"));
    }

    @Test
    void help_test() {
        final var tested = new ResumeSessionCommand(id -> true, c -> {});
        assertEquals("resume-session sessionId - resumes a workflow session", tested.printHelp());
    }

    @Test
    void process_test() throws CommandException {
        final var commands = new ArrayList<ProcessCommand>(1);

        final var tested = new ResumeSessionCommand(id -> true, commands::add);

        final var out = new ByteArrayOutputStream();

        tested.consume(new PrintStream(out), "resume-session sessionId");
        assertEquals(0, out.toString().length());

        assertEquals(1, commands.size());

        final var command = commands.get(0);

        assertEquals(RESUME_PROCESS, command.getCommandType());
        assertEquals("sessionId", command.getSessionId());
    }

    @Test
    void process_no_session_test() throws CommandException {
        final var commands = new ArrayList<ProcessCommand>(1);

        final var tested = new ResumeSessionCommand(id -> false, commands::add);

        final var out = new ByteArrayOutputStream();

        try {
            tested.consume(new PrintStream(out), "resume-session sessionId");
            fail("No definition exception expected");
        } catch (final CommandFormatException e) {
            assertEquals("No session with id sessionId found", e.getMessage());
        }
        assertEquals(0, out.toString().length());
        assertEquals(0, commands.size());
    }

    @Test
    void process_no_param_test() throws CommandException {
        final var commands = new ArrayList<ProcessCommand>(1);

        final var tested = new ResumeSessionCommand(id -> false, commands::add);

        final var out = new ByteArrayOutputStream();

        try {
            tested.consume(new PrintStream(out), "resume-session");
            fail("No definition exception expected");
        } catch (final CommandFormatException e) {
            assertEquals("sessionId is missing. Expected command in format resume-session sessionId. Got resume-session", e.getMessage());
        }
        assertEquals(0, out.toString().length());
        assertEquals(0, commands.size());
    }

}