package org.example.neotaexercise.cli.command.validation;

import java.util.Collections;
import java.util.Map;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Daniel Slavik
 */
class NoCycleRuleTest {

    private final NoCycleRule tested = new NoCycleRule();

    @Test
    void more_output_links() {
        try {
            tested.validate(
                new WorkflowDefinitionDto(
                    new WorkflowDefinitionDto.Source(
                        Collections.emptyMap(),
                        Collections.emptyMap(),
                        Map.of(
                           "link1", new WorkflowDefinitionDto.Link("link1", "a", "b"),
                           "link2", new WorkflowDefinitionDto.Link("link2", "b", "c"),
                           "link3", new WorkflowDefinitionDto.Link("link1", "b", "d")
                        )
                    )
                ));
            fail("Validation exception expected");
        } catch (final ValidationException e) {
            assertEquals("Node b has two output links", e.getMessage());
        }
    }

    @Test
    void more_input_links() {
        try {
            tested.validate(
                new WorkflowDefinitionDto(
                    new WorkflowDefinitionDto.Source(
                        Collections.emptyMap(),
                        Collections.emptyMap(),
                        Map.of(
                           "link1", new WorkflowDefinitionDto.Link("link1", "a", "b"),
                           "link2", new WorkflowDefinitionDto.Link("link2", "c", "b"),
                           "link3", new WorkflowDefinitionDto.Link("link1", "b", "e")
                        )
                    )
                ));
            fail("Validation exception expected");
        } catch (final ValidationException e) {
            assertEquals("Node b has two input links", e.getMessage());
        }
    }

    @Test
    void end_not_reachable_missing_links() {
        try {
            tested.validate(
                new WorkflowDefinitionDto(
                    new WorkflowDefinitionDto.Source(
                        Map.of(
                            "a", new WorkflowDefinitionDto.Node("a", WorkflowDefinitionDto.Node.Type.START_NODE, null),
                            "b", new WorkflowDefinitionDto.Node("b", WorkflowDefinitionDto.Node.Type.TASK_NODE, null),
                            "c", new WorkflowDefinitionDto.Node("c", WorkflowDefinitionDto.Node.Type.TASK_NODE, null),
                            "d", new WorkflowDefinitionDto.Node("d", WorkflowDefinitionDto.Node.Type.TASK_NODE, null),
                            "e", new WorkflowDefinitionDto.Node("e", WorkflowDefinitionDto.Node.Type.END_NODE, null)
                        ),
                        Collections.emptyMap(),
                        Map.of(
                           "link1", new WorkflowDefinitionDto.Link("link1", "a", "b"),
                           "link2", new WorkflowDefinitionDto.Link("link2", "b", "c"),
                           "link3", new WorkflowDefinitionDto.Link("link3", "d", "e")
                        )
                    )
                ));
            fail("Validation exception expected");
        } catch (final ValidationException e) {
            assertEquals("Unable to reach the end node from the start one", e.getMessage());
        }
    }
}