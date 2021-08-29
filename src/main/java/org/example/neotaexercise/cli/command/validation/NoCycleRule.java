/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static java.util.function.Predicate.not;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.END_NODE;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.START_NODE;


/**
 * @author Daniel Slavik
 */
public class NoCycleRule implements WorkflowValidationRule {
    @Override
    public void validate(WorkflowDefinitionDto definition) throws ValidationException {
        final var links = new HashMap<String, String>(definition.getSource().getLinks().size());

        for (final var link : definition.getSource().getLinks().values()) {
            if (links.containsKey(link.getFrom())) {
                throw new ValidationException("Node " + link.getFrom() + " has two output links");
            }
            if (links.containsValue(link.getTo())) {
                throw new ValidationException("Node " + link.getTo() + " has two input links");
            }
            links.put(link.getFrom(), link.getTo());
        }


        //TODO slavik I believe that at this point this could be solved by just comparing it number of links is to number of nodes - 1
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
}
