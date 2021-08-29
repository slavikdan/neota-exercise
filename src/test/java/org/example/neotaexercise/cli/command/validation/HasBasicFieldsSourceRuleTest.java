package org.example.neotaexercise.cli.command.validation;

import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Daniel Slavik
 */
class HasBasicFieldsSourceRuleTest {

    private final HasBasicFieldsSourceRule tested = new HasBasicFieldsSourceRule();

    @Test
    void noSource_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(null));
        });
        assertEquals("Source field must not be empty", exception.getMessage());
    }

    @Test
    void noNode_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(null, null, null)));
        });
        assertEquals("Nodes field must not be empty", exception.getMessage());
    }

    @Test
    void noLinks_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(emptyMap(), null, null)));
        });
        assertEquals("Links field must not be empty", exception.getMessage());
    }

    @Test
    void noLanes_test() {

        final var exception = assertThrows(ValidationException.class, () -> {
            tested.validate(new WorkflowDefinitionDto(new WorkflowDefinitionDto.Source(emptyMap(), null, emptyMap())));
        });
        assertEquals("Lanes field must not be empty", exception.getMessage());
    }

}