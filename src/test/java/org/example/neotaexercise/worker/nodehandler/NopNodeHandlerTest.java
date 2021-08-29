package org.example.neotaexercise.worker.nodehandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.example.neotaexercise.config.BaseConfiguration;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Daniel Slavik
 */
class NopNodeHandlerTest {


    private static final NopNodeHandler TESTED = new NopNodeHandler();

    @Test
    void support_test() {
        assertTrue(TESTED.support(WorkflowDefinitionDto.Node.Type.NOP));
        assertFalse(TESTED.support(WorkflowDefinitionDto.Node.Type.TASK_NODE));
    }

    @Test
    void process_in_chain_test() {
        BaseConfiguration.handlerSleepDuration = Duration.ofMillis(1);

        final var out = new ByteArrayOutputStream();

        final var result = TESTED.process(new NodeHandler.ProcessContext(
            new PrintStream(out),
            new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.NOP, "Cool name"),
            Optional.empty(),
            new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
        ));

        assertEquals(Optional.of("node2"), result);
        assertEquals("NOP completed\n", out.toString());
    }

    @Test
    void process_resume_test() {
        BaseConfiguration.handlerSleepDuration = Duration.ofMillis(1);

        final var out = new ByteArrayOutputStream();

        final var result = TESTED.process(new NodeHandler.ProcessContext(
            new PrintStream(out),
            new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.NOP, "Cool name"),
            Optional.of(new ProcessCommand("sessionId", ProcessCommand.Command.RESUME_PROCESS)),
            new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
        ));

        assertEquals(Optional.of("node2"), result);
        assertEquals("NOP completed\n", out.toString());
    }

    @Test
    void process_unknown_command_test() {
        BaseConfiguration.handlerSleepDuration = Duration.ofMillis(1);

        final var out = new ByteArrayOutputStream();

        final var result = TESTED.process(new NodeHandler.ProcessContext(
            new PrintStream(out),
            new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.NOP, "Cool name"),
            Optional.of(new ProcessCommand("sessionId", ProcessCommand.Command.START_PROCESS)),
            new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
        ));

        assertEquals(Optional.empty(), result);
        assertEquals("Command START_PROCESS not supported for node type NOP\n", out.toString());
    }

}