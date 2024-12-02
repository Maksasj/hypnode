package org.hypnode.ast;

import org.hypnode.Visitor;

public class FieldDefinition extends AstNode {
    private String fieldName;
    private ITypeImplementation type;

    public FieldDefinition(String fieldName, ITypeImplementation type) {
        this.fieldName = fieldName; 
        this.type = type; 
    }

    public String getFieldName() {
        return fieldName;
    }

    public ITypeImplementation getTypeImplementation() {
        return type;
    }

    public boolean isTypeReferenceType() {
        return type instanceof TypeReferenceImplementation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
