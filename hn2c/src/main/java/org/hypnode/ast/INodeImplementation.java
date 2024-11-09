package org.hypnode.ast;

public abstract class INodeImplementation extends AstNode {
    private NodeDeclaration nodeDeclaration;

    void setNodeDeclaration(NodeDeclaration nodeDeclaration) {
        this.nodeDeclaration = nodeDeclaration;
    }

    NodeDeclaration getNodeDeclaration() {
        return nodeDeclaration;
    }
};
