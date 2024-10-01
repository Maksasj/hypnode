package gen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java_cup.runtime.Symbol;

public class TestScanner {
    static class SerializedSymbol {
        private String symbol;
        private Object value;

        public SerializedSymbol(String symbol, Object value) {
            this.symbol = symbol;
            this.value = value;
        }
    }

    @Test
    public void main() throws IOException {
        URL url = this.getClass().getResource("/scanner_tests");
        File testFolder = new File(url.getPath());
        String[] tests = Arrays.stream(Objects.requireNonNull(testFolder.list())).sorted().toArray(String[]::new);

        Gson gson = new GsonBuilder().create();

        for (String test : tests) {
            Path path = Paths.get(testFolder.getPath(), test);
            File inputFile = path.resolve("input.txt").toFile();
            File outputFile = path.resolve("output.txt").toFile();

            FileReader inputReader = new FileReader(inputFile);
            Scanner scanner = new gen.Scanner(inputReader);

            FileWriter writer = new FileWriter(outputFile);



            try {
                ArrayList<SerializedSymbol> symbols = new ArrayList<>();

                while (!scanner.yyatEOF()) {
                    Symbol s = scanner.next_token();
                    symbols.add(new SerializedSymbol(sym.terminalNames[s.sym], s.value));
                }

                gson.toJson(symbols, new com.google.gson.reflect.TypeToken<ArrayList<SerializedSymbol>>() {
                }.getType(), writer);
            } catch (IOException e) {
                // e.printStackTrace();
                // noop
            } finally {
                writer.close();
            }
        }

    }

//    @Test
//    public void ignore_whitespace() throws IOException {
//        StringReader input = new StringReader(" i8   i64 \n  bool \r\n      \n\n\n i8");
//        Scanner scanner = new gen.Scanner(input);
//
//        for (int i = 0; i < 4; ++i) {
//            scanner.next_token();
//        }
//
//        try {
//            scanner.next_token();
//        } catch (IOException e) {
//            Assertions.fail();
//        }
//    }

    @Test
    public void single_line_comment() throws IOException {
        StringReader input = new StringReader("  # Hello world!\n");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        Assertions.assertEquals("# Hello world!", s.value);
        Assertions.assertEquals(sym.SINGLE_LINE_COMMENT, s.sym);

        try {
            scanner.next_token();
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_string_literal() throws IOException {
        StringReader input = new StringReader("\"Some string literal\"");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.STRING_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_string_literal_empty() throws IOException {
        StringReader input = new StringReader("\"\"");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.STRING_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_string_literal_with_quotes() throws IOException {
        StringReader input = new StringReader("\"Some string literal with \\\" \"");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.STRING_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_number_literal() throws IOException {
        StringReader input = new StringReader("123");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.NUMBER_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_number_float_literal() throws IOException {
        StringReader input = new StringReader("123.123");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.NUMBER_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_number_literals() throws IOException {
        StringReader input = new StringReader("1 2 3 4 5 6 7.0 8 9 10");
        Scanner scanner = new gen.Scanner(input);

        for (int i = 0; i < 10; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == sym.NUMBER_LITERAL;
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_character_literal() throws IOException {
        StringReader input = new StringReader("'c'");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.CHARACTER_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_character_literal_with_slash() throws IOException {
        StringReader input = new StringReader("'\\'");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.CHARACTER_LITERAL;

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_character_literal_with_quates() throws IOException {
        StringReader input = new StringReader("'\''");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        Assertions.assertEquals(sym.CHARACTER_LITERAL, s.sym);

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_boolean_literals() throws IOException {
        StringReader input = new StringReader("true false true true false false");
        Scanner scanner = new gen.Scanner(input);

        for (int i = 0; i < 6; ++i) {
            Symbol s = scanner.next_token();
            Assertions.assertEquals(sym.BOOLEAN_LITERAL, s.sym);
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_left_arrow() throws IOException {
        StringReader input = new StringReader("<- <- <-");
        Scanner scanner = new gen.Scanner(input);

        for (int i = 0; i < 3; ++i) {
            Symbol s = scanner.next_token();
            Assertions.assertEquals(sym.LEFT_ARROW, s.sym);
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_bold_right_arrow() throws IOException {
        StringReader input = new StringReader("=> => => =>");
        Scanner scanner = new gen.Scanner(input);

        for (int i = 0; i < 4; ++i) {
            Symbol s = scanner.next_token();
            Assertions.assertEquals(sym.BOLD_RIGHT_ARROW, s.sym);
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_punctuation_arrow() throws IOException {
        StringReader input = new StringReader("; : , . = ( ) { }");
        Scanner scanner = new gen.Scanner(input);

        int expect[] = {sym.SEMI, sym.COLON, sym.COMMA, sym.DOT, sym.EQUAL, sym.L_PARENTHESIS, sym.R_PARENTHESIS,
                sym.L_CURLY_PARENTHESIS, sym.R_CURLY_PARENTHESIS};

        for (int i = 0; i < 9; ++i) {
            Symbol s = scanner.next_token();
            Assertions.assertEquals(expect[i], s.sym);
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_keywords() throws IOException {
        StringReader input = new StringReader("let type node");
        Scanner scanner = new gen.Scanner(input);

        int expect[] = {sym.LET, sym.TYPE, sym.NODE};

        for (int i = 0; i < 3; ++i) {
            Symbol s = scanner.next_token();
            Assertions.assertEquals(expect[i], s.sym);
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }

    @Test
    public void tokenize_attribute_keywords() throws IOException {
        StringReader input = new StringReader("@optional @required @trigger @export @import");
        Scanner scanner = new gen.Scanner(input);

        int expect[] = {sym.OPTIONAL, sym.REQUIRED, sym.TRIGGER, sym.EXPORT, sym.IMPORT};

        for (int i = 0; i < 5; ++i) {
            Symbol s = scanner.next_token();
            Assertions.assertEquals(expect[i], s.sym);
        }

        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            Assertions.fail();
        }
    }
}
