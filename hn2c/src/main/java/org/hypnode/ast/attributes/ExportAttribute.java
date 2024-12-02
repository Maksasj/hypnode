package org.hypnode.ast.attributes;

import org.hypnode.Visitor;
import org.hypnode.ast.INodeAttribute;

import gen.sym;

public class ExportAttribute extends INodeAttribute {
    private String symbolName;

    public ExportAttribute(String symbolName) {
        this.symbolName = symbolName;
    }
    
    public String getSymbolName() {
        return symbolName;
    }
    
    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
