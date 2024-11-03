package org.hn2c.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.Callable;

import org.hypnode.Generator;

public class Main {
    @Command(name = "main", subcommands = { HelloCommand.class }, description = "Main command with subcommands")
    static class MainCommand implements Runnable {
        @Override
        public void run() {
            // Main command logic, if any
        }
    }

    @Command(name = "comp", description = "Compiles cho source file into a C")
    static class HelloCommand implements Runnable {
        @Option(names = {"-f", "--file"}, description = "Source file path")
        private String filePath;

        @Override
        public void run() {
            System.out.printf("Hello, %s!%n", filePath);

            Generator generator = new Generator();
            try {
                String code = generator.generate(new BufferedReader(new FileReader(filePath)));
                System.out.println(code);
            } catch (Exception e) {
                System.err.println("Failed to generate C file");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CommandLine.run(new MainCommand(), args);
    }
}