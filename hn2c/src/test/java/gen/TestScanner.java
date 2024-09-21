package gen;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TestScanner {
    @Test
    public void test() throws IOException {
        StringReader input = new StringReader("some");

        Scanner scanner = new gen.Scanner(input);

        // Symbol s = scanner.next_token();
        // assert s.sym == sym.CBRACKET;
    }
}
