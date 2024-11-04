package org.hypnode.ast;

import java.util.List;

public class NodeDefinition implements IDefinition {
    private List<INodeAttribute> attributes;
    private NodeDeclaration declaration;
    private INodeImplementation implementation;

    public NodeDefinition(List<INodeAttribute> attributes, NodeDeclaration declaration, INodeImplementation implementation) {
        this.attributes = attributes;
        this.declaration = declaration;
        this.implementation = implementation;
    }
}
