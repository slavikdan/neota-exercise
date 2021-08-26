/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static java.util.function.Predicate.not;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.END_NODE;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.START_NODE;


/**
 * @author Daniel Slavik
 */
public final class WorkflowValidationUtils {

    private WorkflowValidationUtils() {
        //do not permit instantiation
    }

    public static void validateWorkflow(final WorkflowDefinitionDto definition) throws ValidationException {
        hasBasicFieldsSource(definition);
        validateStartNode(definition);
        validateEndNode(definition);
        allNodesInOneLane(definition);
        validateCycle(definition);
    }

    public static void hasBasicFieldsSource(final WorkflowDefinitionDto definition) throws ValidationException {
        if (Optional.ofNullable(definition.getSource()).isEmpty()) {
            throw new ValidationException("Source field must not be empty");
        }

        if (Optional.ofNullable(definition.getSource().getNodes()).isEmpty()) {
            throw new ValidationException("Nodes field must not be empty");
        }

        if (Optional.ofNullable(definition.getSource().getLinks()).isEmpty()) {
            throw new ValidationException("Links field must not be empty");
        }

        if (Optional.ofNullable(definition.getSource().getLanes()).isEmpty()) {
            throw new ValidationException("Lanes field must not be empty");
        }
    }

    public static void validateStartNode(final WorkflowDefinitionDto definition) throws ValidationException {
        final var nodes = definition.getSource().getNodes().values().stream()
            .map(WorkflowDefinitionDto.Node::getType)
            .filter(START_NODE::equals)
            .collect(Collectors.toList());

        if (nodes.size() == 0) {
            throw new ValidationException("No starting node found");
        }

        if (nodes.size() > 1) {
            throw new ValidationException("More then one starting node found");
        }
    }

    public static void validateEndNode(final WorkflowDefinitionDto definition) throws ValidationException {
        final var nodes = definition.getSource().getNodes().values().stream()
            .map(WorkflowDefinitionDto.Node::getType)
            .filter(END_NODE::equals)
            .collect(Collectors.toList());

        if (nodes.size() == 0) {
            throw new ValidationException("No end node found");
        }

        if (nodes.size() > 1) {
            throw new ValidationException("More then one end node found");
        }
    }

    public static void allNodesInOneLane(final WorkflowDefinitionDto definition) throws ValidationException {
        final var allNodes = new HashSet<String>();

        final var nodesInLines = definition.getSource().getLanes().values().stream()
            .map(WorkflowDefinitionDto.Lane::getNodes)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        for (var n : nodesInLines) {
            if (allNodes.contains(n)) {
                throw new ValidationException("Node " + n + " is in two lines");
            }
            allNodes.add(n);
        }

        final var allDefinedNodes = definition.getSource().getNodes().keySet();
        if (!allNodes.containsAll(allDefinedNodes)) {
            throw new ValidationException("Nodes not in lanes: " + allDefinedNodes
                .stream()
                .filter(not(allNodes::contains))
                .collect(Collectors.joining(", "))
            );
        }
    }

    public static void validateCycle(final WorkflowDefinitionDto definition) throws ValidationException {

        final var links = new HashMap<String, String>(definition.getSource().getLinks().size());

        for (final var link : definition.getSource().getLinks().values()) {
            if (links.containsKey(link.getFrom())) {
                throw new ValidationException("Node " + link.getFrom() + " has two output lines");
            }
            if (links.containsValue(link.getTo())) {
                throw new ValidationException("Node " + link.getTo() + " has two input lines");
            }
            links.put(link.getFrom(), link.getTo());
        }

        final var startNode = definition.getSource().getNodes().values().stream()
            .filter(n -> START_NODE.equals(n.getType()))
            .map(WorkflowDefinitionDto.Node::getId)
            .findAny()
            .orElseThrow(() -> new ValidationException("Start node is missing"));
        final var endNode = definition.getSource().getNodes().values().stream()
            .filter(n -> END_NODE.equals(n.getType()))
            .map(WorkflowDefinitionDto.Node::getId)
            .findAny()
            .orElseThrow(() -> new ValidationException("End node is missing"));

        var currentNode = Optional.of(startNode);

        final var numberOfNodes = definition.getSource().getNodes().keySet().size();

        final var visitedNodes = new HashSet<String>(numberOfNodes);
        while (currentNode.isPresent()) {
            final var node = currentNode.get();
            visitedNodes.add(node);
            if (node.equals(endNode)) {
                if (visitedNodes.size() != numberOfNodes) {
                    throw new ValidationException("Workflow contains unreachable nodes: " + definition.getSource().getNodes().keySet().stream()
                        .filter(not(visitedNodes::contains))
                        .collect(Collectors.joining(", "))
                    );
                }
                return;
            }
            currentNode = Optional.ofNullable(links.get(node));
        }

        throw new ValidationException("Unable to reach the end node from the start one");

    }


    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
