/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.example.neotaexercise.cli.command.validation.ValidationException;
import org.example.neotaexercise.cli.command.validation.WorkflowValidationRule;
import org.example.neotaexercise.domain.WorkflowDefinition;
import org.example.neotaexercise.dto.WorkflowDefinitionDto;

import static org.example.neotaexercise.util.CommandUtils.parseIdOrElseThrow;


/**
 * @author Daniel Slavik
 */
public class AddWorkflowCommand implements CliCommand {

    private static final String COMMAND = "add-workflow";

    private static final ObjectMapper OB = new ObjectMapper();

    private final BiConsumer<String, WorkflowDefinition> storeDefinition;
    private final List<WorkflowValidationRule> validationRules;

    public AddWorkflowCommand(
        final BiConsumer<String, WorkflowDefinition> storeDefinition,
        final List<WorkflowValidationRule> validationRules
    ) {
        this.storeDefinition = storeDefinition;
        this.validationRules = validationRules;
    }

    @Override
    public boolean support(final String command) {
        return command.startsWith(COMMAND);
    }

    @Override
    public void consume(final PrintStream out, final String command) throws CommandException {

        final var path = parseIdOrElseThrow(command, COMMAND, "path");

        final WorkflowDefinitionDto definition;
        try {
            definition = OB.readValue(new File(path), WorkflowDefinitionDto.class);
        } catch (IOException e) {
            throw new CommandException("Unable to read file " + path + " because: " + e.getMessage(), e);
        }

        try {
            for (final var rule: validationRules) {
                rule.validate(definition);
            }
        } catch (ValidationException e) {
            throw new CommandException(e.getMessage());
        }

        final var definitionId = UUID.randomUUID().toString();
        storeDefinition.accept(definitionId, new WorkflowDefinition(definition));

        out.println("definition stored with id " + definitionId);

    }

    @Override
    public String printHelp() {
        return COMMAND + " /path/to/file - loads a workflow from a file";
    }
}
