package org.example.neotaexercise.cli.command.validation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * @author Daniel Slavik
 */
class AllNodesInOneLaneRuleTest {

    private final AllNodesInOneLaneRule tested = new AllNodesInOneLaneRule();

    @Test
    void not_in_lane_test() {
        try {
            tested.validate(
                new WorkflowDefinitionDto(
                    new WorkflowDefinitionDto.Source(
                        Map.of(
                            "node1", new WorkflowDefinitionDto.Node("node1", null, null),
                            "node2", new WorkflowDefinitionDto.Node("node2", null, null),
                            "node3", new WorkflowDefinitionDto.Node("node3", null, null)
                        ),
                        Map.of(
                            "lane1", new WorkflowDefinitionDto.Lane("lane1", "Lane 1", List.of("node1")),
                            "lane2", new WorkflowDefinitionDto.Lane("lane2", "Lane 2", List.of())
                        ),
                        Collections.emptyMap()
                    )
                )
            );
            fail("validation exception expected");
        } catch (final ValidationException e) {
            assertEquals("Nodes not in lanes: node2, node3", e.getMessage());
        }
    }

    @Test
    void in_multiple_lanes_test() {
        try {
            tested.validate(
                new WorkflowDefinitionDto(
                    new WorkflowDefinitionDto.Source(
                        Map.of(
                            "node1", new WorkflowDefinitionDto.Node("node1", null, null),
                            "node2", new WorkflowDefinitionDto.Node("node2", null, null),
                            "node3", new WorkflowDefinitionDto.Node("node3", null, null)
                        ),
                        Map.of(
                            "lane1", new WorkflowDefinitionDto.Lane("lane1", "Lane 1", List.of("node1")),
                            "lane2", new WorkflowDefinitionDto.Lane("lane2", "Lane 2", List.of("node1", "node2")),
                            "lane3", new WorkflowDefinitionDto.Lane("lane2", "Lane 2", List.of("node2", "node3"))
                        ),
                        Collections.emptyMap()
                    )
                )
            );
            fail("validation exception expected");
        } catch (final ValidationException e) {
            assertEquals("Nodes in multiple lines: node1, node2", e.getMessage());
        }
    }
}