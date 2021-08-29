/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.worker.nodehandler;

import java.util.Optional;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.example.neotaexercise.worker.ProcessException;

import static org.example.neotaexercise.domain.ProcessCommand.Command.START_PROCESS;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.START_NODE;


/**
 * @author Daniel Slavik
 */
public class StartNodeHandler implements NodeHandler {
    @Override
    public boolean support(WorkflowDefinitionDto.Node.Type type) {
        return START_NODE.equals(type);
    }

    @Override
    public Optional<String> process(ProcessContext context) throws ProcessException {
        if(context.getCommand().isEmpty()) {
            throw new ProcessException("Workflow is not started yet");
        }

        if (!START_PROCESS.equals(context.getCommand().get().getCommandType())) {
            context.getOut().println("Command " + context.getCommand().get().getCommandType() + " not supported for node type " + context.getNode().getType());
            return Optional.empty();
        }

        return context.getDefinition().getNextNode(context.getNode().getId());
    }
}
