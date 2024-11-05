package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;

public class CompositeTypeImplementation extends ITypeImplementation {
    private List<FieldDefinition> fields;
    
    public CompositeTypeImplementation(List<FieldDefinition> fields) {
        this.fields = fields;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
