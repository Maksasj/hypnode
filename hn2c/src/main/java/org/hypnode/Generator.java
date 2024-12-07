package org.hypnode;

import java.io.Reader;

import org.hypnode.ast.HypnodeModule;

import gen.Parser;
import gen.Scanner;
import java_cup.runtime.Symbol;

public class Generator {
    private Features features;

    public Generator(Features features) {
        this.features = features;
    }

    public String generate(Reader in) {
        Scanner scanner = new gen.Scanner(in);
        Parser parser = new Parser(scanner);

        try {
            Symbol s = parser.parse();
            HypnodeModule module = (HypnodeModule) s.value;
            
            // Flatten all inlined and anonymous types 
            FlattenTypesVisitor flattenTypes = new FlattenTypesVisitor(module);

            Integer flattenResult;

            do {
                flattenResult = flattenTypes.flatten();
                System.out.println("Runned type flatten with " + flattenResult + " changes");
            } while (flattenResult > 0);

            // Link all type references
            TypeReferenceLinkerVisitor typeReferenceLinker = new TypeReferenceLinkerVisitor(module);
            typeReferenceLinker.link();
            
            // Link all node references
            NodeReferenceLinkerVisitor nodeReferenceLinker = new NodeReferenceLinkerVisitor(module);
            nodeReferenceLinker.link();

            SemanticAnalyzer semAn = new SemanticAnalyzer(module);
            semAn.analyze();

            GeneratorVisitor generator = new GeneratorVisitor(module, features);
            String result = generator.generate();
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
