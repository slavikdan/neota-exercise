/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command.validation;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static java.util.function.Predicate.not;


/**
 * @author Daniel Slavik
 */
public class AllNodesInOneLaneRule implements WorkflowValidationRule {

    @Override
    public void validate(WorkflowDefinitionDto definition) throws ValidationException {
        final var allNodes = new HashSet<String>();

        final var nodesInLines = definition.getSource().getLanes().values().stream()
            .map(WorkflowDefinitionDto.Lane::getNodes)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        final var nodesInMultipleLanes = new HashSet<String>();

        for (var n : nodesInLines) {
            if (allNodes.contains(n)) {
                nodesInMultipleLanes.add(n);
            }
            allNodes.add(n);
        }

        if (!nodesInMultipleLanes.isEmpty()) {
            throw new ValidationException("Nodes in multiple lines: " + nodesInMultipleLanes.stream()
                .sorted()
                .collect(Collectors.joining(", "))
            );

        }

        final var allDefinedNodes = definition.getSource().getNodes().keySet();
        if (!allNodes.containsAll(allDefinedNodes)) {
            throw new ValidationException("Nodes not in lanes: " + allDefinedNodes
                .stream()
                .filter(not(allNodes::contains))
                .sorted()
                .collect(Collectors.joining(", "))
            );
        }

    }
}
