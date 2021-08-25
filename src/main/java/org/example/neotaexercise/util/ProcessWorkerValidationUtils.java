/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.util;

import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.domain.WorkflowSession;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.example.neotaexercise.worker.ProcessException;


/**
 * @author Daniel Slavik
 */
public final class ProcessWorkerValidationUtils {
    private ProcessWorkerValidationUtils() {
        //do not permit instantiation
    }

    public static void validateNotStarted(final WorkflowDefinitionDto.Node node, final WorkflowDefinition definition) throws ProcessException {
        if (!definition.getStartingNode().equals(node.getId())) {
            throw new ProcessException("Session has already been started");
        }
    }

    public static void validateNotFinished(final WorkflowDefinitionDto.Node node, final WorkflowDefinition definition) throws ProcessException {
        if (!definition.getEndNode().equals(node.getId())) {
            throw new ProcessException("Session has already been ended");
        }
    }
}
