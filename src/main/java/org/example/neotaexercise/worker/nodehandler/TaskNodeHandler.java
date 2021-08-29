/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.worker.nodehandler;

import java.util.Optional;
import org.example.neotaexercise.config.BaseConfiguration;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static org.example.neotaexercise.domain.ProcessCommand.Command.RESUME_PROCESS;
import static org.example.neotaexercise.dto.WorkflowDefinitionDto.Node.Type.TASK_NODE;


/**
 * @author Daniel Slavik
 */
public class TaskNodeHandler implements NodeHandler {

    @Override
    public boolean support(WorkflowDefinitionDto.Node.Type type) {
        return TASK_NODE.equals(type);
    }

    @Override
    public Optional<String> process(ProcessContext context) {
        if (context.getCommand().isPresent() && !RESUME_PROCESS.equals(context.getCommand().get().getCommandType())) {
            context.getOut().println("Command " + context.getCommand().get().getCommandType() + " not supported for node type " + context.getNode().getType());
            return Optional.empty();
        }

        try {
            Thread.sleep(BaseConfiguration.handlerSleepDuration.toMillis());
        } catch (final InterruptedException e) {
            e.printStackTrace(context.getOut());
        }

        context.getOut().println(context.getNode().getName() + " completed");
        return context.getDefinition().getNextNode(context.getNode().getId());
    }
}
