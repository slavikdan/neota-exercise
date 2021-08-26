package org.example.neotaexercise.util;

import java.util.Map;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.example.neotaexercise.util.WorkflowValidationUtils.validateWorkflow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Daniel Slavik
 */
class WorkflowValidationUtilsTest {

    @Test
    void noSource_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(null));
        });
        assertEquals("Source field must not be empty", exception.getMessage());
    }

    @Test
    void noNode_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(null, null, null)));
        });
        assertEquals("Nodes field must not be empty", exception.getMessage());
    }

    @Test
    void noLinks_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(emptyMap(), null, null)));
        });
        assertEquals("Links field must not be empty", exception.getMessage());
    }

    @Test
    void noLanes_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(emptyMap(), null, emptyMap())));
        });
        assertEquals("Lanes field must not be empty", exception.getMessage());
    }

    @Test
    void noStartNode_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
                emptyMap(),
                emptyMap(),
                emptyMap()
            )));
        });
        assertEquals("No starting node found", exception.getMessage());
    }

    @Test
    void multipleStartNode_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
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

    @Test
    void noEndNode_test() {

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
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

        final var exception = assertThrows(WorkflowValidationUtils.ValidationException.class, () -> {
            validateWorkflow(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(
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