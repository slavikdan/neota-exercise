/*
 * Copyright (c) 2021 Price f(x), s.r.o.
 */
package org.example.neotaexercise.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.example.neotaexercise.cli.command.CliCommand;
import org.example.neotaexercise.cli.command.CommandException;
import org.example.neotaexercise.config.BaseConfiguration;


/**
 * @author Daniel Slavik
 */
public class CliInterface {

    private static final String EXIT_COMMAND = "exit";

    private static final String HELP_COMMAND = "help";

    private CliInterface() {
        //do not permit instantiation
    }


    public static void start(
        final List<CliCommand> commands,
        final PrintStream out,
        final InputStream in
    ) {
        out.println("I do recommend starting with command 'help' ;-)");
        boolean shouldRun = true;
        final var scanner = new Scanner(in);

        while (shouldRun) {
            final var command = scanner.nextLine().toLowerCase(Locale.ROOT);

            if (HELP_COMMAND.equalsIgnoreCase(command)) {
                commands.stream().map(CliCommand::printHelp).forEach(out::println);
                out.println("exit - I think you know :-)");
                continue;
            }

            if (EXIT_COMMAND.equalsIgnoreCase(command)) {
                shouldRun = false;
                continue;
            }

            commands.stream()
                .filter(c -> c.support(command))
                .findAny()
                .ifPresentOrElse(
                    c -> consumeCommand(c, command, out),
                    () -> out.println("Unknown command: " + command)
                );
        }

        out.println("Have a nice one :-)");
    }

    private static void consumeCommand(
        final CliCommand c,
        final String command,
        final PrintStream out
    ) {
        try {
            c.consume(out, command);
        } catch (final CommandException e) {
            out.println("Unable to process command: " + e.getMessage());
            if (BaseConfiguration.LogLevel.DEBUG.equals(BaseConfiguration.logLevel)) {
                e.printStackTrace(out);
            }
        }
    }
}
