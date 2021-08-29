package org.example.neotaexercise.cli.command.validation;

import java.util.Map;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Daniel Slavik
 */
class HasEndNodeRuleTest {

    private final HasEndNodeRule tested = new HasEndNodeRule();

    @Test
    void noEndNode_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
                Map.of(
                    "id1", new WorkflowDefinitionDto.Node("id1", WorkflowDefinitionDto.Node.Type.START_NODE, "start1")
                ),
                emptyMap(),
                emptyMap()
            )));
        });
        assertEquals("No end node found", exception.getMessage());
    }

    @Test
    void multipleEndNode_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
                Map.of(
                    "id1", new WorkflowDefinitionDto.Node("id1", WorkflowDefinitionDto.Node.Type.START_NODE, "start1"),
                    "id2", new WorkflowDefinitionDto.Node("id2", WorkflowDefinitionDto.Node.Type.END_NODE, "end1"),
                    "id3", new WorkflowDefinitionDto.Node("id3", WorkflowDefinitionDto.Node.Type.END_NODE, "end2")
                ),
                emptyMap(),
                emptyMap()
            )));
        });
        assertEquals("More then one end node found", exception.getMessage());
    }

}