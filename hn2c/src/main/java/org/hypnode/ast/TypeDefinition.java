package org.hypnode.ast;

import org.hypnode.Visitor;

public class TypeDefinition extends IDefinition {
    private String typeName;
    private ITypeImplementation implementation;

    public TypeDefinition(String typeName, ITypeImplementation implementation) {
        this.typeName = typeName;
        this.implementation = implementation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
