package org.hn2c.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

import org.hypnode.Features;
import org.hypnode.Generator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

public class Main {
    @Command(name = "main", subcommands = { CompileCommand.class }, description = "Main command with subcommands")
    static class MainCommand implements Runnable {
        @Override
        public void run() {
            // Main command logic, if any
        }
    }

    @Command(name = "comp", description = "Compiles cho source file into a C")
    static class CompileCommand implements Runnable {
        @Option(names = {"-f", "--file"}, description = "Source file path")
        private String inputFilePath;

        @Option(names = {"-t", "--target"}, description = "Target file path")
        private String outputFilePath;
        
        @Option(names = { "-sta", "--standalone"}, description = "Standalone")
        private boolean standalone;

        @Override
        public void run() {
            Features features = new Features(standalone);
            Generator generator = new Generator(features);
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