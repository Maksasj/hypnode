package org.hypnode.ast;

import org.hypnode.Visitor;

public class FieldAccess extends AstNode {
    private String fieldName;

    public FieldAccess(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
    
}
