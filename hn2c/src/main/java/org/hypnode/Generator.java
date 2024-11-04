package org.hypnode;

import java.io.Reader;

import org.hypnode.ast.HypnodeModule;

import gen.Parser;
import gen.Scanner;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;

public class Generator {
    public Generator() {

    }

    public String generate(Reader in) {
        Scanner scanner = new gen.Scanner(in);
        Parser parser = new Parser(scanner);

        try {
            Symbol s = parser.parse();
            HypnodeModule module = (HypnodeModule) s.value;
            
            // String result = program.visit();
            // return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        /*
        TypeChecker typeChecker = new TypeChecker();
        typeChecker.doTypeCheck(program);

        GeneratorVisitor visitor = new GeneratorVisitor();

        // return visitor.visit(program);
        */
        return "Poggers";
    }
}
