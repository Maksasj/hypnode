package org.hypnode.ast;

public class ImportNodeImplementation extends INodeImplementation {
    private String symbolName;

    public ImportNodeImplementation(String symbolName) {
        this.symbolName = symbolName;
    }
}
