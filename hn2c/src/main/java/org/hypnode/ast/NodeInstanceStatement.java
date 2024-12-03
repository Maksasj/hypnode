package org.hypnode.ast;

import org.hypnode.Visitor;
import org.utils.StringUtils;

public class NodeInstanceStatement extends IStatement {
    private String symbolName;
    private String nodeSymbolName;

    private String name;
    private String nodeName;

    public NodeInstanceStatement(String name, String nodeName) {
        this.name = name;
        this.nodeName = nodeName;

        generateSymbolName();

        nodeSymbolName = "PLACE HOLDER";
    }

    public String getSymbolName() {
        return symbolName;
    }

    public String getNodeSymbolName() {
        return nodeSymbolName;
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
