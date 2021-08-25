/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.example.neotaexercise.cli.CliInterface;
import org.example.neotaexercise.cli.command.AddWorkflowCommand;
import org.example.neotaexercise.cli.command.GetCurrentSessionStatusCommand;
import org.example.neotaexercise.cli.command.ListWorkflowSessionsCommand;
import org.example.neotaexercise.cli.command.ListWorkflowsCommand;
import org.example.neotaexercise.cli.command.ResumeSessionCommand;
import org.example.neotaexercise.cli.command.StartWorkflowSessionCommand;
import org.example.neotaexercise.db.WorkflowRepository;
import org.example.neotaexercise.db.WorkflowSessionRepository;
import org.example.neotaexercise.domain.ProcessCommand;


/**
 * @author Daniel Slavik
 */
public class App {

    public static PrintStream OUT = System.out;

    public static InputStream IN = System.in;

    private static final WorkflowRepository WORKFLOW_REPOSITORY = new WorkflowRepository();

    private static final WorkflowSessionRepository WORKFLOW_SESSION_REPOSITORY = new WorkflowSessionRepository();

    private static final Queue<ProcessCommand> COMMAND_QUEUE = new ConcurrentLinkedQueue<>();

    public static void main(String... params) {
        OUT.println("Starting cli interface");

        CliInterface.start(
            List.of(
                new AddWorkflowCommand(WORKFLOW_REPOSITORY::store),
                new ListWorkflowsCommand(WORKFLOW_REPOSITORY::getDefinitionIds),
                new StartWorkflowSessionCommand(WORKFLOW_REPOSITORY::getDefinition, WORKFLOW_SESSION_REPOSITORY::store),
                new ListWorkflowSessionsCommand(WORKFLOW_SESSION_REPOSITORY::getAllSessions),
                new GetCurrentSessionStatusCommand(WORKFLOW_SESSION_REPOSITORY::getSession, WORKFLOW_REPOSITORY::getDefinition),
                new ResumeSessionCommand(WORKFLOW_SESSION_REPOSITORY::existById, COMMAND_QUEUE::add)
            ),
            OUT,
            IN
        );
    }
}
