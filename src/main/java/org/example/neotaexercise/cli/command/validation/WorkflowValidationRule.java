/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command.validation;

import org.example.neotaexercise.dto.WorkflowDefinitionDto;


/**
 * @author Daniel Slavik
 */
public interface WorkflowValidationRule {

    void validate(WorkflowDefinitionDto definition) throws ValidationException;
}
