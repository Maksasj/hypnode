package org.hypnode.ast;

import java.util.List;

public class HypnodeModule extends AstNode {
    private List<IDefinition> definitions;

    public HypnodeModule(List<IDefinition> definitions) {
        this.definitions = definitions;
    }
}
