package org.example.neotaexercise.cli.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Daniel Slavik
 */
class GetCurrentSessionStatusCommandTest {

    @Test
    void testSupport() {
        final var tested = new GetCurrentSessionStatusCommand(s -> Optional.empty(), s -> Optional.empty());
        assertTrue(tested.support("session-state"));
        assertTrue(tested.support("session-state sessionId"));
        assertFalse(tested.support("someCommand"));
    }

    @Test
    void testHelp() {
        final var tested = new GetCurrentSessionStatusCommand(s -> Optional.empty(), s -> Optional.empty());
        assertEquals("session-state sessionId - gets current session state", tested.printHelp());
    }

    @Test
    void testProcess() throws CommandException {
        final var definitionRepo = Map.of("def1", new WorkflowDefinition(
            "starting",
            "ended",
            Map.of(
                "starting", "task1",
                "task1", "nop1",
                "nop1", "ended"
            ),
            Map.of(
                "starting", new WorkflowDefinitionDto.Node("starting", WorkflowDefinitionDto.Node.Type.START_NODE, "Start node"),
                "ended", new WorkflowDefinitionDto.Node("ended", WorkflowDefinitionDto.Node.Type.END_NODE, "End node"),
                "nop1", new WorkflowDefinitionDto.Node("nop1", WorkflowDefinitionDto.Node.Type.NOP, null),
                "task1", new WorkflowDefinitionDto.Node("task1", WorkflowDefinitionDto.Node.Type.TASK_NODE, "Task node")
            ),
            Map.of(
                "starting", new WorkflowDefinitionDto.Lane("line1", "Lane 1", List.of("starting")),
                "task1", new WorkflowDefinitionDto.Lane("line2", "Lane 2", List.of("task1", "nop1")),
                "nop1", new WorkflowDefinitionDto.Lane("line2", "Lane 2", List.of("task1", "nop1")),
                "ended", new WorkflowDefinitionDto.Lane("line3", "Lane 3", List.of("ended"))
            )
        ));

        final var inProgressTask = new WorkflowSession("def1")
            .setCurrentNode("task1");
        inProgressTask.tryToAllocate();
        final var inProgressNop = new WorkflowSession("def1")
            .setCurrentNode("nop1");
        inProgressNop.tryToAllocate();

        final var sessionRepo = Map.of(
            "starting", new WorkflowSession("def1").setCurrentNode(null),
            "ended", new WorkflowSession("def1").setCurrentNode("ended"),
            "on-line", new WorkflowSession("def1").setCurrentNode("starting"),
            "in-progress-task", inProgressTask,
            "in-progress-nop", inProgressNop
        );

        final var tested = new GetCurrentSessionStatusCommand(id -> Optional.ofNullable(sessionRepo.get(id)), id -> Optional.ofNullable(definitionRepo.get(id)));

        final var out = new ByteArrayOutputStream();

        tested.consume(new PrintStream(out), "session-state starting");
        assertEquals("Starting\n", out.toString());
        out.reset();

        tested.consume(new PrintStream(out), "session-state ended");
        assertEquals("Ended\n", out.toString());
        out.reset();

        tested.consume(new PrintStream(out), "session-state on-line");
        assertEquals("Lane 2\n", out.toString());
        out.reset();

        tested.consume(new PrintStream(out), "session-state in-progress-task");
        assertEquals("Task node in progress\n", out.toString());
        out.reset();

        tested.consume(new PrintStream(out), "session-state in-progress-nop");
        assertEquals("nop1 in progress\n", out.toString());
        out.reset();

    }
}