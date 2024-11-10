package org.hn2c.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import org.hypnode.Generator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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
        private String inputFilePath;

        @Option(names = {"-t", "--target"}, description = "Target file path")
        private String outputFilePath;
        
        @Override
        public void run() {
            Generator generator = new Generator();
            try {
                PrintWriter out = new PrintWriter(outputFilePath);
                String code = generator.generate(new BufferedReader(new FileReader(inputFilePath)));

                out.print(code);

                out.close();
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