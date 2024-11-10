package org.hypnode;

import java.io.Reader;

import org.hypnode.ast.HypnodeModule;

import gen.Parser;
import gen.Scanner;
import java_cup.runtime.Symbol;

public class Generator {
    public String generate(Reader in) {
        Scanner scanner = new gen.Scanner(in);
        Parser parser = new Parser(scanner);

        try {
            Symbol s = parser.parse();
            HypnodeModule module = (HypnodeModule) s.value;
            
            // Flatten all inlined and anonymous types 
            // FlattenTypesVisitor flattenTypes = new FlattenTypesVisitor();
            // Integer typeCheckResult = flattenTypes.visit(module);

            // Do type checking
            // TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
            // Integer typeCheckResult = typeChecker.visit(module);

            GeneratorVisitor generator = new GeneratorVisitor();
            
            return generator.visit(module);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
