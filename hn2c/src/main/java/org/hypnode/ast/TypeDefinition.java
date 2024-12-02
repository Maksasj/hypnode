package org.hypnode.ast;

import org.hypnode.Visitor;
import org.utils.StringUtils;

public class TypeDefinition extends IDefinition {
    private String symbolName;
    private String typeName;
    private ITypeImplementation implementation;

    public TypeDefinition(String typeName, ITypeImplementation implementation) {
        this.typeName = typeName;
        this.implementation = implementation;
        
        generateSymbolName();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getTypeName() {
        return typeName;
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
        
    private void generateSymbolName() {
        symbolName = "tsym_" + StringUtils.generateRandomString(16);
    };
}
