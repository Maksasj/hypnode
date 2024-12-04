package org.hypnode;

import org.hypnode.ast.NodeConnectionStatement;
import org.hypnode.ast.NodeDefinition;
import org.hypnode.ast.PortDefinition;

public class NodeConnection {
    private NodeConnectionStatement connection;
 
    //private NodeDefinition sourceNode;
    private PortDefinition sourcePort;
    
    // private NodeDefinition sinkNode;
    private PortDefinition sinkPort;

    public NodeConnection(NodeConnectionStatement connection, PortDefinition source, PortDefinition sink) {
        this.connection = connection;
        this.sourcePort = source;
        this.sinkPort = sink;
    }

    public NodeConnectionStatement getConnection() {
        return connection;
    }
    
    public PortDefinition getSource() {
        return sourcePort;
    }

    public PortDefinition getSink() {
        return sinkPort;
    }
}
