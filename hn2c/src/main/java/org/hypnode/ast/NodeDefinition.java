package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;

public class NodeDefinition extends IDefinition {
    private List<INodeAttribute> attributes;
    private NodeDeclaration declaration;
    private INodeImplementation implementation;

    public NodeDefinition(List<INodeAttribute> attributes, NodeDeclaration declaration, INodeImplementation implementation) {
        this.attributes = attributes;
        this.declaration = declaration;
        this.implementation = implementation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
