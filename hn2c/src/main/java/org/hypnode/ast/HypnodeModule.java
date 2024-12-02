package org.hypnode.ast;

import java.util.ArrayList;
import java.util.List;

import org.hypnode.Visitor;

public class HypnodeModule extends AstNode {
    private List<TypeDefinition> typeDefinitions;
    private List<NodeDefinition> nodeDefinitions;

    public HypnodeModule(List<IDefinition> definitions) {
        this.typeDefinitions = new ArrayList<TypeDefinition>(); 
        this.nodeDefinitions = new ArrayList<NodeDefinition>(); 

        for (IDefinition iDefinition : definitions) {
            if(iDefinition.getType() == DefinitionType.TypeDefinition) {
                typeDefinitions.add((TypeDefinition) iDefinition);
            } else {
                nodeDefinitions.add((NodeDefinition) iDefinition);
            }
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public List<TypeDefinition> getTypeDefinitions() {
        return typeDefinitions;
    }

    public List<NodeDefinition> getNodeDefinitions() {
        return nodeDefinitions;
    }

    public void addTypeDefinition(TypeDefinition definition) {
        typeDefinitions.add(definition);
    }
}
