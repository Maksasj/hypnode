package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;

public class HypnodeModule extends AstNode {
    private List<IDefinition> definitions;

    public HypnodeModule(List<IDefinition> definitions) {
        this.definitions = definitions;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
