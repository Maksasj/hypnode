package org.hypnode;

import org.hypnode.ast.NodeConnectionStatement;
import org.hypnode.ast.NodeDefinition;
import org.hypnode.ast.NodeInstanceStatement;
import org.hypnode.ast.PortDefinition;

public class NodeConnection {
    private NodeConnectionStatement connection;
 
    private NodeDefinition sourceNode;
    private NodeInstanceStatement sourceNodeInstance;
    private PortDefinition sourcePort;
    
    private NodeDefinition sinkNode;
    private NodeInstanceStatement sinkNodeInstance;
    private PortDefinition sinkPort;

    public NodeConnection(
        NodeConnectionStatement connection, 
        
        PortDefinition source, 
        NodeDefinition sourceNode, 
        NodeInstanceStatement sourceNodeInstance, 
    
        PortDefinition sink, 
        NodeDefinition sinkNode,
        NodeInstanceStatement sinkNodeInstance
    ) {
        this.connection = connection;

        this.sourceNode = sourceNode;
        this.sourceNodeInstance = sourceNodeInstance;
        this.sourcePort = source;
        
        this.sinkPort = sink;
        this.sinkNodeInstance = sinkNodeInstance;
        this.sinkNode = sinkNode;
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

    public NodeDefinition getSourceNode() {
        return sourceNode;
    }

    public NodeDefinition getSinkNode() {
        return sinkNode;
    }

    public NodeInstanceStatement getSourceNodeInstance() {
        return sourceNodeInstance;
    }
    public NodeInstanceStatement getSinkNodeInstance() {
        return sinkNodeInstance;
    }
}
