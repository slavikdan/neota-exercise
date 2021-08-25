/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Daniel Slavik
 */
public class ProcessCommand {

    private final String sessionId;
    private final Command commandType;

    public ProcessCommand(String sessionId, Command command) {
        this.sessionId = sessionId;
        this.commandType = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Command getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("sessionId", sessionId)
            .append("commandType", commandType)
            .toString();
    }

    public enum Command {
        START_PROCESS,
        RESUME_PROCESS
    }


}
