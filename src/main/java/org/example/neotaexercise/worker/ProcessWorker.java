/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.worker;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.example.neotaexercise.worker.nodehandler.NodeHandler;


/**
 * @author Daniel Slavik
 */
public class ProcessWorker extends Thread {
    private boolean shouldRun;

    private int sleepDuration;

    private final String name;

    private final PrintStream out;

    private final Function<String, Optional<WorkflowDefinition>> workflowSupp;

    private final Function<String, Optional<WorkflowSession>> sessionSupp;

    private final Supplier<Optional<ProcessCommand>> commandSupplier;

    private final List<NodeHandler> nodeHandlers;

    public ProcessWorker(
        String name,
        PrintStream out,
        Function<String, Optional<WorkflowDefinition>> workflowSupp,
        Function<String, Optional<WorkflowSession>> sessionSupp,
        Supplier<Optional<ProcessCommand>> commandSupplier,
        List<NodeHandler> nodeHandlers
    ) {
        this.sleepDuration = 2000;
        this.shouldRun = true;
        this.name = name;
        this.out = out;
        this.workflowSupp = workflowSupp;
        this.sessionSupp = sessionSupp;
        this.commandSupplier = commandSupplier;
        this.nodeHandlers = nodeHandlers;
    }

    public void stopThread() {
        shouldRun = false;
    }

    @Override
    public void run() {
        out.println("Thread " + name + " started");

        while (shouldRun) {
            commandSupplier.get().ifPresent(this::processCommand);
            sleep();
        }

        out.println("Thread " + name + " stopped");
    }

    public void processCommand(final ProcessCommand command) {

        out.println("Thread " + name + " processing command " + command);

        WorkflowSession session = null;

        try {
            try {
                session = getAndLockSession(command.getSessionId());
                final var workflow = getWorkflow(session.getDefinitionId());
                processCommand(command, session, workflow);
            } catch (final ProcessException e) {
                out.println("Unable to process command " + command + ": " + e.getMessage());
            }
        } finally {
            Optional.ofNullable(session).ifPresent(WorkflowSession::release);
        }
    }

    private void processCommand(final ProcessCommand command, final WorkflowSession session, final WorkflowDefinition workflow) throws ProcessException {

        var node = session.getCurrentNode()
            .map(workflow::getNextNode)
            .orElseGet(() -> Optional.of(workflow.getStartingNode()));

        var currentCommand = Optional.of(command);

        while (node.isPresent()) {


            final var processedNode = node.get();
            out.println("Thread " + name + " working with node " + processedNode);

            session.setCurrentNode(processedNode);

            node = processNode(
                workflow.getNode(processedNode).orElseThrow(() -> new ProcessException("Node with id " + processedNode + " not found")),
                currentCommand,
                workflow
            );

            if (node.map(n -> workflow.isNodeInDifferentLane(processedNode, n)).orElse(false)) {
                out.println("Thread " + name + " we are on a line here :-)");
                return;
            }

            currentCommand = Optional.empty();
        }
    }


    private Optional<String> processNode(WorkflowDefinitionDto.Node node, Optional<ProcessCommand> command, WorkflowDefinition workflowDefinition) throws ProcessException {
        return nodeHandlers.stream()
            .filter(h -> h.support(node.getType()))
            .findAny()
            .orElseThrow(() -> new ProcessException("No handler for type " + node.getType() + " found"))
            .process(new NodeHandler.ProcessContext(out, node, command, workflowDefinition));
    }

    private WorkflowSession getAndLockSession(final String id) throws ProcessException {
        final var session = sessionSupp.apply(id).orElseThrow(() -> new ProcessException("No session with id " + id + " found"));
        if (session.isBeingProcessed()) {
            throw new ProcessException("Session with id " + id + " is being processed");
        }

        if (!session.tryToAllocate()) {
            //TODO slavik this is a weird state ... should I return the command into the queue?
            throw new ProcessException("Another thread is currently using session with id " + id);
        }

        return session;
    }

    private WorkflowDefinition getWorkflow(final String id) throws ProcessException {
        return workflowSupp.apply(id).orElseThrow(() -> new ProcessException("No workflow definition with id " + id + " found"));
    }

    private void sleep() {
        try {
            Thread.sleep(sleepDuration);
        } catch (final InterruptedException e) {
            out.println("Thread " + name + " interrupted!");
            e.printStackTrace(out);
        }
    }
}
