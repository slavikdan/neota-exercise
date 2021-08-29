package org.example.neotaexercise.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Daniel Slavik
 */
class ListWorkflowsCommandTest {

    @Test
    void support_test() {
        final var tested = new ListWorkflowsCommand(Collections::emptySet);
        assertTrue(tested.support("list-workflows"));
        assertFalse(tested.support("someCommand"));
    }

    @Test
    void testHelp() {
        final var tested = new ListWorkflowsCommand(Collections::emptySet);
        assertEquals("list-workflows - lists all stored workflow definitions", tested.printHelp());
    }

    @Test
    void process_test() throws CommandException {
        final var tested = new ListWorkflowsCommand(() -> Set.of("wf1", "wf2"));

        final var out = new ByteArrayOutputStream();

        tested.consume(new PrintStream(out), "list-workflows");
        final var result = Arrays.asList(out.toString().split("\n"));
        assertTrue(result.contains("Existing workflow definitions:"));
        assertTrue(result.contains("wf1"));
        assertTrue(result.contains("wf2"));

    }
}