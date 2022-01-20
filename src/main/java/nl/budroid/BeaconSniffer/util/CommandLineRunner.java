package nl.budroid.BeaconSniffer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CommandLineRunner {
    /**
     * @param command the command to run
     * @return the output of the command
     * @throws IOException if an I/O error occurs
     */
    public static CommandResult run(String... command) throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Process process = pb.start();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return new CommandResult(in.lines().collect(Collectors.toList()));
        }
    }

    /**
     * Prevent construction.
     */
    private CommandLineRunner()
    {
    }
}
