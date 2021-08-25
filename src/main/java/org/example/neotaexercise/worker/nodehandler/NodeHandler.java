/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.worker.nodehandler;

import java.io.PrintStream;
import java.util.Optional;
import org.example.neotaexercise.domain.ProcessCommand;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;
import org.example.neotaexercise.worker.ProcessException;


/**
 * @author Daniel Slavik
 */
public interface NodeHandler {

    boolean support(WorkflowDefinitionDto.Node.Type type);

    Optional<String> process(ProcessContext context) throws ProcessException;

    class ProcessContext {
        private final PrintStream out;
        private final WorkflowDefinitionDto.Node node;
        private final Optional<ProcessCommand> command;
        private final WorkflowDefinition definition;

        public ProcessContext(PrintStream out, WorkflowDefinitionDto.Node node, Optional<ProcessCommand> command, WorkflowDefinition definition) {
            this.out = out;
            this.node = node;
            this.command = command;
            this.definition = definition;
        }

        public PrintStream getOut() {
            return out;
        }

        public WorkflowDefinitionDto.Node getNode() {
            return node;
        }

        public Optional<ProcessCommand> getCommand() {
            return command;
        }

        public WorkflowDefinition getDefinition() {
            return definition;
        }
    }
}
