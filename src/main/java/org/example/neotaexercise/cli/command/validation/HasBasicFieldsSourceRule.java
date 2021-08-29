/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command.validation;

import java.util.Optional;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;


/**
 * @author Daniel Slavik
 */
public class HasBasicFieldsSourceRule implements WorkflowValidationRule {
    @Override
    public void validate(WorkflowDefinitionDto definition) throws ValidationException {
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
}
