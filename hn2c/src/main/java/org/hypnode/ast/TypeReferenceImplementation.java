package org.hypnode.ast;

import org.hypnode.Visitor;

public class TypeReferenceImplementation extends ITypeImplementation {
    private String typeName;
    
    public TypeReferenceImplementation(String typeName) {
        this.typeName = typeName;
    }

    public String getReferenceTypeName() {
        return typeName;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
