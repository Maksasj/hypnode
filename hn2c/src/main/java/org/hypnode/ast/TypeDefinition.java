package org.hypnode.ast;

import org.hypnode.Visitor;

public class TypeDefinition extends IDefinition {
    private String typeName;
    private ITypeImplementation implementation;

    public TypeDefinition(String typeName, ITypeImplementation implementation) {
        this.typeName = typeName;
        this.implementation = implementation;
    }

    public ITypeImplementation getImplementation() {
        return implementation; 
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public DefinitionType getType() {
        return DefinitionType.TypeDefinition;
    }
}
