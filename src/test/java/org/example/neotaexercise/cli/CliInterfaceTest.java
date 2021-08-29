package org.example.neotaexercise.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.neotaexercise.cli.command.CliCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Daniel Slavik
 */
class CliInterfaceTest {

    @Test
    void cliInterface_exitCommandTest() {
        final var baos = new ByteArrayOutputStream();

        CliInterface.start(
            Collections.emptyList(),
            new PrintStream(baos, true),
            new ByteArrayInputStream("exit".getBytes())
        );

        assertArrayEquals(
            baos.toString().split("\n"),
            List.of("I do recommend starting with command 'help' ;-)", "Have a nice one :-)").toArray()
        );
    }

    @Test
    void cliInterface_helpCommandTest() {
        final var baos = new ByteArrayOutputStream();
        final var input = "help\nexit";

        final var helpMessage = "awesome-command - where the magic happens";

        CliInterface.start(
            List.of(new CliCommand() {
                @Override
                public boolean support(String command) {
                    throw new UnsupportedOperationException("Not implemented yet.");
                }

                @Override
                public void consume(PrintStream out, String command) {
                    throw new UnsupportedOperationException("Not implemented yet.");
                }

                @Override
                public String printHelp() {
                    return helpMessage;
                }
            }),
            new PrintStream(baos, true),
            new ByteArrayInputStream(input.getBytes())
        );

        final var result = baos.toString().split("\n");

        assertEquals(4, result.length);
        assertEquals(helpMessage, result[1]);
    }

    @Test
    void cliInterface_unknownCommandTest() {
        final var baos = new ByteArrayOutputStream();

        final var input = "do something\n"
            + "exit";

        CliInterface.start(
            Collections.emptyList(),
            new PrintStream(baos, true),
            new ByteArrayInputStream(input.getBytes())
        );

        assertArrayEquals(
            baos.toString().split("\n"),
            List.of("I do recommend starting with command 'help' ;-)", "Unknown command: do something", "Have a nice one :-)").toArray()
        );
    }

    @Test
    void cliInterface_aCommandTest() {
        final var baos = new ByteArrayOutputStream();

        final var testedCommand = "command1";

        final var input = testedCommand + "\n" + "exit";

        final var calls = new ArrayList<String>(1);

        CliInterface.start(
            List.of(new CliCommand() {
                @Override
                public boolean support(String command) {
                    return testedCommand.equalsIgnoreCase(command);
                }

                @Override
                public void consume(PrintStream out, String command) {
                    calls.add(command);
                }

                @Override
                public String printHelp() {
                    throw new UnsupportedOperationException("Not implemented yet.");
                }
            }),
            new PrintStream(baos, true),
            new ByteArrayInputStream(input.getBytes())
        );

        assertArrayEquals(List.of(testedCommand).toArray(), calls.toArray());
    }

}