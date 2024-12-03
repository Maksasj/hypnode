package org.hypnode.ast;

import org.hypnode.Visitor;
import org.utils.StringUtils;

public class NodeInstanceStatement extends IStatement {
    private String symbolName;

    private String name;
    private String nodeName;

    private NodeDefinition linkedDefinition;

    public NodeInstanceStatement(String name, String nodeName) {
        this.name = name;
        this.nodeName = nodeName;

        generateSymbolName();

        linkedDefinition = null;
    }

    public String getName() {
        return name;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public NodeDefinition getLinkedNodeDefinition() {
        return linkedDefinition;
    }

    public void linkNodeDefinition(NodeDefinition linkedDefinition) {
        this.linkedDefinition = linkedDefinition;
    }

    private void generateSymbolName() {
        symbolName = "nisym_" + StringUtils.generateRandomString(16);
    };

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
