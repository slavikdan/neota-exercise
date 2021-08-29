/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command.validation;

import java.util.stream.Collectors;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.END_NODE;


/**
 * @author Daniel Slavik
 */
public class HasEndNodeRule implements WorkflowValidationRule {

    @Override
    public void validate(WorkflowDefinitionDto definition) throws ValidationException {
        final var nodes = definition.getSource().getNodes().values().stream()
            .map(WorkflowDefinitionDto.Node::getType)
            .filter(END_NODE::equals)
            .collect(Collectors.toList());

        if (nodes.isEmpty()) {
            throw new ValidationException("No end node found");
        }

        if (nodes.size() > 1) {
            throw new ValidationException("More then one end node found");
        }

    }
}
