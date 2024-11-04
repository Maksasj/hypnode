package org.hypnode.ast;

import org.hypnode.Visitor;

public class ImportNodeImplementation extends INodeImplementation {
    private String symbolName;

    public ImportNodeImplementation(String symbolName) {
        this.symbolName = symbolName;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
