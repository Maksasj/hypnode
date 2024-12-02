package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;
import org.hypnode.ast.attributes.ExportAttribute;
import org.utils.StringUtils;

public class NodeDefinition extends IDefinition {
    private String symbolName;

    private List<INodeAttribute> attributes;
    private NodeDeclaration declaration;
    private INodeImplementation implementation;

    public NodeDefinition(List<INodeAttribute> attributes, NodeDeclaration declaration, INodeImplementation implementation) {
        this.attributes = attributes;
        this.declaration = declaration;

        this.implementation = implementation;
        this.implementation.setNodeDeclaration(this.declaration);

        generateSymbolName();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public DefinitionType getType() {
        return DefinitionType.NodeDefinition;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getNodeName() {
        return declaration.getNodeName();
    }

    public boolean exported() {
        for(INodeAttribute atr : attributes)
            if(atr instanceof ExportAttribute)
                return true;

        return false;
    }

    public String getExportedName() {
        for(INodeAttribute atr : attributes)
            if(atr instanceof ExportAttribute)
                return ((ExportAttribute) atr).getSymbolName();
        
        return null;
    }

    public String getImportedName() {
        if(imported())
            return ((ImportNodeImplementation) implementation).getSymbolName();

        return null;
    }
    
    public boolean imported() {
        return implementation instanceof ImportNodeImplementation;
    }

    private void generateSymbolName() {
        symbolName = "sym_" + StringUtils.generateRandomString(16);
    };
}
