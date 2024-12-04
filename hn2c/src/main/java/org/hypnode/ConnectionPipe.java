package org.hypnode;

import java.util.List;

import org.hypnode.ast.NodeDefinition;
import org.utils.StringUtils;

public class ConnectionPipe {
    private List<NodeConnection> connections;
    private String symbolName;

    public ConnectionPipe(List<NodeConnection> connections) {
        this.connections = connections;

        generateSymbolName();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public boolean haveConnectionNodeSink(NodeDefinition node) {
        return connections.stream().anyMatch((con) -> con.getSinkNode() == node);
    }

    public List<NodeConnection> getConnections() {
        return connections;
    }

    private void generateSymbolName() {
        symbolName = "cpsym_" + StringUtils.generateRandomString(16);
    };
}
