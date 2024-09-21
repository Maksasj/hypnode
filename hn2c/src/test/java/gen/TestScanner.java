package gen;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import java_cup.runtime.Symbol;

public class TestScanner {
    @Test
    public void tokenize_basic_types() throws IOException {
        StringReader input = new StringReader("i8 i16 i32 i64 u8 u16 u32 u64 f32 f64 string bool");
        Scanner scanner = new gen.Scanner(input);

        for(int i = 0; i < 12; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == sym.PRIMITIVE_TYPE;
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
    }

    @Test
    public void tokenize_number_literals() throws IOException {
        StringReader input = new StringReader("1 2 3 4 5 6 7.0 8 9 10");
        Scanner scanner = new gen.Scanner(input);

        for(int i = 0; i < 10; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == sym.NUMBER_LITERAL;
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
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
            return;
        }

        assert 1 == 0;
    }

    @Test
    public void tokenize_character_literal_with_quates() throws IOException {
        StringReader input = new StringReader("'\''");
        Scanner scanner = new gen.Scanner(input);

        Symbol s = scanner.next_token();
        assert s.sym == sym.CHARACTER_LITERAL;
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }

    @Test
    public void tokenize_boolean_literals() throws IOException {
        StringReader input = new StringReader("true false true true false false");
        Scanner scanner = new gen.Scanner(input);

        for(int i = 0; i < 6; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == sym.BOOLEAN_LITERAL;
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }

    
    @Test
    public void tokenize_left_arrow() throws IOException {
        StringReader input = new StringReader("<- <- <-");
        Scanner scanner = new gen.Scanner(input);

        for(int i = 0; i < 3; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == sym.LEFT_ARROW;
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }

    @Test
    public void tokenize_bold_right_arrow() throws IOException {
        StringReader input = new StringReader("=> => => =>");
        Scanner scanner = new gen.Scanner(input);

        for(int i = 0; i < 3; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == sym.LEFT_ARROW;
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }

    @Test
    public void tokenize_punctuation_arrow() throws IOException {
        StringReader input = new StringReader("; : , . = ( ) { }");
        Scanner scanner = new gen.Scanner(input);

        int expect[] = { sym.SEMI, sym.COLON, sym.COMMA, sym.DOT, sym.EQUAL, sym.L_PARENTHESIS, sym.R_PARENTHESIS, sym.L_CURLY_PARENTHESIS, sym.R_CURLY_PARENTHESIS };

        for(int i = 0; i < 9; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == expect[i];
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }

    
    @Test
    public void tokenize_keywords() throws IOException {
        StringReader input = new StringReader("let type node");
        Scanner scanner = new gen.Scanner(input);

        int expect[] = { sym.LET, sym.TYPE, sym.NODE };

        for(int i = 0; i < 3; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == expect[i];
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }

    @Test
    public void tokenize_attribute_keywords() throws IOException {
        StringReader input = new StringReader("@optional @required @trigger @export @import");
        Scanner scanner = new gen.Scanner(input);

        int expect[] = { sym.OPTIONAL, sym.REQUIRED, sym.TRIGGER, sym.EXPORT, sym.IMPORT };

        for(int i = 0; i < 5; ++i) {
            Symbol s = scanner.next_token();
            assert s.sym == expect[i];
        }
        
        // ensure scanner is empty
        try {
            Symbol empty = scanner.next_token();
        } catch (IOException ex) {
            return;
        }

        assert 1 == 0;
    }
}
