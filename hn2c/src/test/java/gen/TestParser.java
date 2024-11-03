package gen;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Type;

import java_cup.runtime.Symbol;

public class TestParser {
    static class SerializedSymbol {
        public String symbol;
        public Object value;

        public SerializedSymbol(String symbol, Object value) {
            this.symbol = symbol;
            this.value = value;
        }
    }

    public static final Type PROGRAM_TYPE = new com.google.gson.reflect.TypeToken<ArrayList<SerializedSymbol>>() {
    }.getType();

    @Test
    public void main() throws IOException {
        /*
        URL url = this.getClass().getResource("/parser_tests");
        File testFolder = new File(url.getPath());
        String[] tests = Arrays.stream(Objects.requireNonNull(testFolder.list())).sorted().toArray(String[]::new);

        Gson gson = new GsonBuilder().create();

        for (String test : tests) {
            Path path = Paths.get(testFolder.getPath(), test);
            File inputFile = path.resolve("input.hn").toFile();
            File outputFile = path.resolve("output.json").toFile();

            FileReader inputReader = new FileReader(inputFile);
            Scanner scanner = new gen.Scanner(inputReader);

            ArrayList<SerializedSymbol> received = new ArrayList<>();

            while (!scanner.yyatEOF()) {
                Symbol s = scanner.next_token();
                received.add(new SerializedSymbol(sym.terminalNames[s.sym], s.value));
            }

            // Write to output.json
            // FileWriter writer = new FileWriter(outputFile);
            // gson.toJson(received, PROGRAM_TYPE, writer);
            // writer.close();

            // Read and check the output.json
            JsonReader reader = new JsonReader(new FileReader(outputFile));
            ArrayList<SerializedSymbol> expected = gson.fromJson(reader, PROGRAM_TYPE);
            Assertions.assertEquals(expected.size(), received.size());
            for (int i = 0; i < expected.size(); ++i) {
                Assertions.assertEquals(expected.get(i).symbol, received.get(i).symbol);
                Assertions.assertEquals(expected.get(i).value, received.get(i).value);
            }
        }
        */
    }
}
