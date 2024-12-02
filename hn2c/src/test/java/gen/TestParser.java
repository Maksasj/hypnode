package gen;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.hypnode.Generator;

public class TestParser {
    @Test
    public void acceptedTests() throws IOException {
        URL url = this.getClass().getResource("/parser_tests/accepted");
        File testFolder = new File(url.getPath());
        String[] tests = Arrays.stream(Objects.requireNonNull(testFolder.list())).sorted().toArray(String[]::new);

        for (int i = 0; i < tests.length; ++i) {
            String test = tests[i];

            Path path = Paths.get(testFolder.getPath(), test);
            File inputFile = path.toFile();

            FileReader inputReader = new FileReader(inputFile);
            Generator generator = new Generator();

            String out = generator.generate(new BufferedReader(inputReader));
            Assertions.assertNotNull(out);
        }
    }

    @Test
    public void rejectedTests() throws IOException {
        URL url = this.getClass().getResource("/parser_tests/rejected");
        File testFolder = new File(url.getPath());
        String[] tests = Arrays.stream(Objects.requireNonNull(testFolder.list())).sorted().toArray(String[]::new);

        for (int i = 0; i < tests.length; ++i) {
            String test = tests[i];

            Path path = Paths.get(testFolder.getPath(), test);
            File inputFile = path.toFile();

            FileReader inputReader = new FileReader(inputFile);
            Generator generator = new Generator();

            String out = generator.generate(new BufferedReader(inputReader));
            Assertions.assertNull(out);
        }
    }
}
