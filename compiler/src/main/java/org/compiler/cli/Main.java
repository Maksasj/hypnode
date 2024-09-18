package org.compiler.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "hn2c", mixinStandardHelpOptions = true,
        description = "Converts Hypnode programs to C code", version = "0.1")
public class Main implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("Hello world!");
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}