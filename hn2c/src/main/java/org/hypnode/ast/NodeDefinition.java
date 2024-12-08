package org.hypnode.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.sound.sampled.Port;

import org.hypnode.ConnectionPipe;
import org.hypnode.NodeConnection;
import org.hypnode.Visitor;
import org.hypnode.ast.attributes.ExportAttribute;
import org.hypnode.ast.value.FieldAccessValueExpression;
import org.utils.Pair;
import org.utils.StringUtils;

public class NodeDefinition extends IDefinition {
    private String symbolName;

    private List<INodeAttribute> attributes;
    private NodeDeclaration declaration;
    private INodeImplementation implementation;

    public NodeDefinition(List<INodeAttribute> attributes, NodeDeclaration declaration, INodeImplementation implementation) {
        this.attributes = attributes;
        this.declaration = declaration;

        this.implementation = implementation;
        this.implementation.setNodeDeclaration(this.declaration);

        generateSymbolName();
    }

    public PortDefinition getSelfPort(String portName) {
        List<PortDefinition> ports = new ArrayList<>();
        ports.addAll(declaration.getInputPorts());
        ports.addAll(declaration.getInputPorts());
        
        Optional<PortDefinition> result = ports.stream().filter(pt -> pt.getPortName().equals(portName)).findFirst(); 

        if(!result.isPresent())
            return null;

        return result.get();
    } 

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public DefinitionType getType() {
        return DefinitionType.NodeDefinition;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public List<PortDefinition> getInputPorts() {
        return declaration.getInputPorts();
    }

    public List<PortDefinition> getOutputPorts() {
        return declaration.getOutputPorts();
    }

    public List<PortDefinition> getPorts() {
        List<PortDefinition> ports = new ArrayList<>();

        ports.addAll(getInputPorts());
        ports.addAll(getOutputPorts());

        return ports;
    }

    public String getNodeName() {
        return declaration.getNodeName();
    }

    public List<NodeInstanceStatement> getChildNodes() {
        if(implementation instanceof StatementListNodeImplementation)
            return ((StatementListNodeImplementation) implementation).getChildNodes();
            
        return null;
    }

    public List<NodeConnectionStatement> getConnections() {
        if(implementation instanceof StatementListNodeImplementation) 
            return ((StatementListNodeImplementation) implementation).getConnections();

        return null;
    }

    public List<NodeConnectionStatement> getConstantValueConnections() {
        if(implementation instanceof StatementListNodeImplementation) 
            return ((StatementListNodeImplementation) implementation).getConstantValueConnections();

        return null;
    }

    public boolean exported() {
        for(INodeAttribute atr : attributes)
            if(atr instanceof ExportAttribute)
                return true;

        return false;
    }

    public String getExportedName() {
        for(INodeAttribute atr : attributes)
            if(atr instanceof ExportAttribute)
                return ((ExportAttribute) atr).getSymbolName();
        
        return null;
    }

    public String getImportedName() {
        if(imported())
            return ((ImportNodeImplementation) implementation).getSymbolName();

        return null;
    }
    
    public boolean imported() {
        return implementation instanceof ImportNodeImplementation;
    }

    public INodeImplementation getImplementation() {
        return implementation;
    }
    
    public static List<List<PortDefinition>> findGroups(List<NodeConnection> relations) {
        // Build the adjacency list graph
        Map<PortDefinition, Set<PortDefinition>> graph = new HashMap<>();
        for (NodeConnection relation : relations) {
            PortDefinition item1 = relation.getSink();
            PortDefinition item2 = relation.getSource();
            
            graph.putIfAbsent(item1, new HashSet<>());
            graph.putIfAbsent(item2, new HashSet<>());

            graph.get(item1).add(item2);
            graph.get(item2).add(item1);
        }

        // Find connected components using dfs
        List<List<PortDefinition>> groups = new ArrayList<>();
        Set<PortDefinition> visited = new HashSet<>();

        for (PortDefinition node : graph.keySet()) {
            if (!visited.contains(node)) {
                List<PortDefinition> group = new ArrayList<>();
                dfs(node, graph, visited, group);
                groups.add(group);
            }
        }

        return groups;
    }

    private static void dfs(PortDefinition node, Map<PortDefinition, Set<PortDefinition>> graph, Set<PortDefinition> visited, List<PortDefinition> group) {
        visited.add(node);
        group.add(node);

        for (PortDefinition neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, group);
            }
        }
    }

    public List<ConnectionPipe> getDataConnectionPipes() {
        if(!(implementation instanceof StatementListNodeImplementation)) 
            return null;

        List<NodeConnection> connections = new ArrayList<>();

        List<NodeConnectionStatement> statements = ((StatementListNodeImplementation) implementation).getConnections();
        for(NodeConnectionStatement st : statements) {
            if(!(st.getSource() instanceof FieldAccessValueExpression)) 
                continue;

            PortDefinition sink = null;
            NodeDefinition linkedSinkDef = null;
            NodeInstanceStatement sinkInstance = null;

            // We first check is it a self output port
            for(PortDefinition out : getOutputPorts()) {
                if(out.getPortName().equals(st.getSink().get(0).getFieldName())) {
                    sink = out;
                    linkedSinkDef = this;
                    sinkInstance = null;
                    break;
                }
            }

            // If it is not, lets check for child nodes
            for(NodeInstanceStatement node : getChildNodes()) {
                if(!node.getName().equals(st.getSink().get(0).getFieldName())) {
                    continue;
                }
                
                sinkInstance = node;
                linkedSinkDef = node.getLinkedNodeDefinition();

                for(PortDefinition p : linkedSinkDef.getInputPorts()) {
                    if(p.getPortName().equals(st.getSink().get(1).getFieldName())) {
                        sink = p;
                    }
                }
            }
            
            PortDefinition source = null;
            NodeDefinition linkedSourceDef = null;
            NodeInstanceStatement sourceInstance = null;

            List<FieldAccess> access = ((FieldAccessValueExpression) st.getSource()).getAccessList();

            // We first check is it a self input port
            for(PortDefinition input : getInputPorts()) {
                if(input.getPortName().equals(access.get(0).getFieldName())) {
                    source = input;
                    linkedSourceDef = this;
                    sourceInstance = null;
                    break;
                }
            }

            // If it is not, lets check for child nodes
            for(NodeInstanceStatement node : getChildNodes()) {
                if(!node.getName().equals(access.get(0).getFieldName())) {
                    continue;
                }

                sourceInstance = node;
                linkedSourceDef = node.getLinkedNodeDefinition();

                for(PortDefinition p : linkedSourceDef.getOutputPorts()) {
                    if(p.getPortName().equals(access.get(1).getFieldName())) {
                        source = p;
                    }
                }
            }

            if(sink == null)
                throw new UnsupportedOperationException("Invalid sink usage in node '" + getNodeName() + "'");

            if(source == null)
                throw new UnsupportedOperationException("Invalid source usage in node '" + getNodeName() + "'");

            connections.add(new NodeConnection(st, source, linkedSinkDef, sinkInstance, sink, linkedSourceDef, sourceInstance));
        }

        List<List<PortDefinition>> portGroups = findGroups(connections);

        List<ConnectionPipe> connectionPipes = new ArrayList<>();

        for(List<PortDefinition> group : portGroups) {
            List<NodeConnection> toGroup = new ArrayList<>();

            for(NodeConnection con : connections) {
                if(group.stream().anyMatch(port -> port == con.getSink())) {
                    toGroup.add(con);
                } else if(group.stream().anyMatch(port -> port == con.getSource())) {
                    toGroup.add(con);
                }
            }

            connectionPipes.add(new ConnectionPipe(toGroup));
        }

        return connectionPipes;
    }

    public NodeInstanceStatement getNodeInstanceStatement(String name) {
        for(NodeInstanceStatement child : getChildNodes())
            if(child.getName().equals(name))
                return child;
 
        return null;
    }

    private void generateSymbolName() {
        symbolName = "nsym_" + StringUtils.generateRandomString(16);
    };
}
