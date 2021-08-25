/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.worker.nodehandler;

import java.util.Optional;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.END_NODE;


/**
 * @author Daniel Slavik
 */
public class EndNodeHandler implements NodeHandler {
    @Override
    public boolean support(WorkflowDefinitionDto.Node.Type type) {
        return END_NODE.equals(type);
    }

    @Override
    public Optional<String> process(ProcessContext context) {
        context.getOut().println("Ended");
        return Optional.empty();
    }
}
