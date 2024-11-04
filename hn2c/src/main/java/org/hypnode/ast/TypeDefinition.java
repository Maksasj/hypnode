package org.hypnode.ast;

public class TypeDefinition implements IDefinition {
    private String typeName;
    private ITypeImplementation implementation;

    public TypeDefinition(String typeName, ITypeImplementation implementation) {
        this.typeName = typeName;
        this.implementation = implementation;
    }
}
