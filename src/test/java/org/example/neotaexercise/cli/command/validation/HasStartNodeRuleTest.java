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
class HasStartNodeRuleTest {

    private final HasStartNodeRule tested = new HasStartNodeRule();

    @Test
    void noStartNode_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
                emptyMap(),
                emptyMap(),
                emptyMap()
            )));
        });
        assertEquals("No starting node found", exception.getMessage());
    }

    @Test
    void multipleStartNode_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
                Map.of(
                    "id1", new WorkflowDefinitionDto.Node("id1", WorkflowDefinitionDto.Node.Type.START_NODE, "start1"),
                    "id2", new WorkflowDefinitionDto.Node("id2", WorkflowDefinitionDto.Node.Type.START_NODE, "start2")
                ),
                emptyMap(),
                emptyMap()
            )));
        });
        assertEquals("More then one starting node found", exception.getMessage());
    }

}