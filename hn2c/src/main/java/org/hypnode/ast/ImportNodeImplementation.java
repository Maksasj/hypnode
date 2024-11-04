package org.hypnode.ast;

public class ImportNodeImplementation implements INodeImplementation {
    private String symbolName;

    public ImportNodeImplementation(String symbolName) {
        this.symbolName = symbolName;
    }
}
