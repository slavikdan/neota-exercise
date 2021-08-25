/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli.command;

import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Function;
import org.example.neotaexercise.domain.ProcessCommand;

import static org.example.neotaexercise.util.CommandUtils.parseIdOrElseThrow;


/**
 * @author Daniel Slavik
 */
public class ResumeSessionCommand implements CliCommand {

    private static final String COMMAND = "resume-session";

    private final Function<String, Boolean> sessionValidator;

    private final Consumer<ProcessCommand> sendCommand;

    public ResumeSessionCommand(Function<String, Boolean> sessionValidator, Consumer<ProcessCommand> sendCommand) {
        this.sessionValidator = sessionValidator;
        this.sendCommand = sendCommand;
    }

    @Override
    public boolean support(String command) {
        return command.startsWith(COMMAND);
    }

    @Override
    public void consume(PrintStream out, String command) throws CommandException {
        final var sessionId = parseIdOrElseThrow(command, COMMAND, "sessionId");

        if(Boolean.FALSE.equals(sessionValidator.apply(sessionId))) {
            throw new CommandFormatException("No session with id " + sessionId + " found");
        }

        sendCommand.accept(new ProcessCommand(sessionId, ProcessCommand.Command.RESUME_PROCESS));
    }

    @Override
    public String printHelp() {
        return COMMAND + " sessionId - resumes a workflow session";
    }
}
