package org.example.neotaexercise.worker.nodehandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Daniel Slavik
 */
class EndNodeHandlerTest {

    private static final EndNodeHandler TESTED = new EndNodeHandler();

    @Test
    void support_test() {
        assertTrue(TESTED.support(WorkflowDefinitionDto.Node.Type.END_NODE));
        assertFalse(TESTED.support(WorkflowDefinitionDto.Node.Type.NOP));
    }

    @Test
    void process_in_chain_test() {

        final var out = new ByteArrayOutputStream();

        final var result = TESTED.process(new NodeHandler.ProcessContext(
            new PrintStream(out),
            new WorkflowDefinitionDto.Node("node1", WorkflowDefinitionDto.Node.Type.END_NODE, "Cool name"),
            Optional.empty(),
            new WorkflowDefinition(null, null, Map.of("node1", "node2"), emptyMap(), emptyMap())
        ));

        assertEquals(Optional.empty(), result);
        assertEquals("Ended\n", out.toString());

    }
}