package org.example.neotaexercise.worker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.example.neotaexercise.worker.nodehandler.NodeHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Daniel Slavik
 */
class ProcessWorkerTest {

    @Test
    void process_test() throws InterruptedException {

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

        final var queue = new ConcurrentLinkedQueue<ProcessCommand>();

        final var out = new ByteArrayOutputStream();

        final var handler = new TestHandler();

        final var tested = new ProcessWorker(
            100,
            "Thread1",
            new PrintStream(out),
            s -> Optional.ofNullable(definitionRepo.get(s)),
            s -> Optional.ofNullable(sessionRepo.get(s)),
            () -> Optional.ofNullable(queue.poll()),
            Collections.singletonList(handler)
        );

        tested.start();

        queue.add(new ProcessCommand("starting", ProcessCommand.Command.START_PROCESS));

        Thread.sleep(500);

        tested.stopThread();

        assertEquals(Optional.of("starting"), sessionRepo.get("starting").getCurrentNode());
        assertFalse(sessionRepo.get("starting").isBeingProcessed());

        assertEquals(1, handler.contexts.size());
        final var context = handler.contexts.get(0);

        assertEquals("starting", context.getNode().getId());
        assertTrue(context.getCommand().isPresent());
        assertEquals("starting", context.getCommand().get().getSessionId());
        assertEquals(ProcessCommand.Command.START_PROCESS, context.getCommand().get().getCommandType());

    }

    public static class TestHandler implements NodeHandler {

        private final List<ProcessContext> contexts = new ArrayList<>();

        public void clearContexts() {
            contexts.clear();
        }

        public List<ProcessContext> getContexts() {
            return contexts;
        }

        @Override
        public boolean support(WorkflowDefinitionDto.Node.Type type) {
            return true;
        }

        @Override
        public Optional<String> process(ProcessContext context) throws ProcessException {
            contexts.add(context);
            return Optional.empty();
        }
    }
}