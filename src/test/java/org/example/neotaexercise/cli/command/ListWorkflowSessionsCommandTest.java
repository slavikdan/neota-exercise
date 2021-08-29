package org.example.neotaexercise.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Daniel Slavik
 */
class ListWorkflowSessionsCommandTest {

    @Test
    void support_test() {
        final var tested = new ListWorkflowSessionsCommand(Collections::emptySet);
        assertTrue(tested.support("list-sessions"));
        assertFalse(tested.support("someCommand"));
    }

    @Test
    void help_test() {
        final var tested = new ListWorkflowSessionsCommand(Collections::emptySet);
        assertEquals("list-sessions - lists all workflow sessions", tested.printHelp());
    }

    @Test
    void process_test() throws CommandException {
        final var tested = new ListWorkflowSessionsCommand(() -> Set.of("session1", "session2"));

        final var out = new ByteArrayOutputStream();

        tested.consume(new PrintStream(out), "list-sessions");
        final var result = Arrays.asList(out.toString().split("\n"));
        assertTrue(result.contains("Existing workflow sessions:"));
        assertTrue(result.contains("session1"));
        assertTrue(result.contains("session2"));
    }

}