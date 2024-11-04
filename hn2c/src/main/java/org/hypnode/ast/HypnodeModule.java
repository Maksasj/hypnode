package org.hypnode.ast;

import java.util.List;

public class HypnodeModule {
    private List<IDefinition> definitions;

    public HypnodeModule(List<IDefinition> definitions) {
        this.definitions = definitions;
    }
}
