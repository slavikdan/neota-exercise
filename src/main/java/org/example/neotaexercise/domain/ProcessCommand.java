/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.domain;

/**
 * @author Daniel Slavik
 */
public class ProcessCommand {

    private final String sessionId;
    private final Command command;

    public ProcessCommand(String sessionId, Command command) {
        this.sessionId = sessionId;
        this.command = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Command getCommand() {
        return command;
    }

    public enum Command {
        RESUME_PROCESS
    }
}
