package org.hypnode.ast;

import java.util.List;

public class NodeDeclaration {
    private String nodeName;
    private List<PortDefinition> in;
    private List<PortDefinition> out;

    public NodeDeclaration(String nodeName, List<PortDefinition> in, List<PortDefinition> out) {
        this.nodeName = nodeName;
        this.in = in;
        this.out = out;
    }
 
    public String getNodeName() {
        return nodeName;
    }
}
