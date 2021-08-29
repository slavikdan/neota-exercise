/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Objects;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Daniel Slavik
 */
class SimpleProcessIT {


    @Test
    void simpleProcessTest() throws IOException, InterruptedException {
        final var pipe = new PipedOutputStream();
        final var out = new ByteArrayOutputStream();

        App.IN = new PipedInputStream(pipe);
        App.OUT = new PrintStream(out);

        new Thread(() -> App.main("sleep", "PT0.1S")).start();
        Thread.sleep(1000);
        out.reset();

        sendCommandAndWait(pipe, "add-workflow " + getDefinitionPath());

        final var workflowId = validateCreatedAndWorkflowId(consumeLine(out));

        sendCommandAndWait(pipe, "start-workflow " + workflowId);

        final var sessionId = validateCreatedAndSessionId(consumeLine(out));

        Thread.sleep(2000);

        sendCommandAndWait(pipe, "session-state " + sessionId);
        assertEquals("Task1 completed\nTask2 completed\nlane2\n", consumeLine(out));

        sendCommandAndWait(pipe, "resume-session " + sessionId);
        Thread.sleep(2000);
        sendCommandAndWait(pipe, "session-state " + sessionId);
        assertEquals("Task3 completed\nlane3\n", consumeLine(out));

        sendCommandAndWait(pipe, "resume-session " + sessionId);
        Thread.sleep(2000);

        assertEquals("NOP completed\nTask4 completed\nEnded\n", consumeLine(out));

        sendCommandAndWait(pipe, "exit");
        assertEquals("Have a nice one :-)\n", consumeLine(out));
    }


    private static void sendCommandAndWait(final PipedOutputStream pipe, final String command) throws IOException, InterruptedException {
        sendCommandAndWait(pipe, command, 1000);
    }

    private static void sendCommandAndWait(final PipedOutputStream pipe, final String command, final long millis) throws IOException, InterruptedException {
        pipe.write((command + "\n").getBytes());
        Thread.sleep(millis);
    }

    private static String validateCreatedAndWorkflowId(final String consumeLine) {
        assertTrue(consumeLine.startsWith("definition stored with id "), "workflow created info expected");
        return consumeLine.substring(26).replaceAll("\\s+", "");
    }

    private static String validateCreatedAndSessionId(final String consumeLine) {
        assertTrue(consumeLine.startsWith("Started a workflow session with id "), "session created info expected");
        return consumeLine.substring(35).replaceAll("\\s+", "");
    }

    private static String getDefinitionPath() {
        return Objects.requireNonNull(
            SimpleProcessIT.class.getClassLoader().getResource("workflow-definition/sample.json")
        ).getPath();
    }

    private static String consumeLine(final ByteArrayOutputStream out) {
        final var line = out.toString();
        out.reset();
        return line;
    }
}
