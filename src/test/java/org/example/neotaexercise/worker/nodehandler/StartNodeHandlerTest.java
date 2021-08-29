package org.example.neotaexercise.worker.nodehandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.example.neotaexercise.worker.ProcessException;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Daniel Slavik
 */
class StartNodeHandlerTest {

    private static final StartNodeHandler TESTED = new StartNodeHandler();

    @Test
    void support_test() {
        assertTrue(TESTED.support(WorkflowDefinitionDto.Node.Type.START_NODE));
        assertFalse(TESTED.support(WorkflowDefinitionDto.Node.Type.NOP));
    }

    @Test
    void process_in_chain_test() {

        final var out = new ByteArrayOutputStream();

        try {
            TESTED.process(new NodeHandler.ProcessContext(
                new PrintStream(out),
                new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.START_NODE, "Cool name"),
                Optional.empty(),
                new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
            ));
            fail("not started wf exception expected");
        } catch (final ProcessException e) {
            assertEquals("Workflow is not started yet", e.getMessage());
        }
    }

    @Test
    void process_start_test() throws ProcessException {

        final var out = new ByteArrayOutputStream();

        final var result = TESTED.process(new NodeHandler.ProcessContext(
            new PrintStream(out),
            new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.START_NODE, "Cool name"),
            Optional.of(new ProcessCommand("sessionId", ProcessCommand.Command.START_PROCESS)),
            new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
        ));

        assertEquals(Optional.of("node2"), result);
        assertEquals(0, out.toString().length());
    }

    @Test
    void process_unknown_command_test() throws ProcessException {

        final var out = new ByteArrayOutputStream();

        final var result = TESTED.process(new NodeHandler.ProcessContext(
            new PrintStream(out),
            new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.START_NODE, "Cool name"),
            Optional.of(new ProcessCommand("sessionId", ProcessCommand.Command.RESUME_PROCESS)),
            new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
        ));

        assertEquals(Optional.empty(), result);
        assertEquals("Command RESUME_PROCESS not supported for node type TASK_NODE\n", out.toString());
    }

}