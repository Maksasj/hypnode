package org.hypnode;

import java.util.List;

import org.hypnode.ast.ArrayTypeImplementation;
import org.hypnode.ast.CompositeTypeImplementation;
import org.hypnode.ast.FieldAccess;
import org.hypnode.ast.FieldDefinition;
import org.hypnode.ast.HypnodeModule;
import org.hypnode.ast.ImportNodeImplementation;
import org.hypnode.ast.NodeConnectionStatement;
import org.hypnode.ast.NodeDeclaration;
import org.hypnode.ast.NodeDefinition;
import org.hypnode.ast.NodeInstanceStatement;
import org.hypnode.ast.PortDefinition;
import org.hypnode.ast.StatementListNodeImplementation;
import org.hypnode.ast.TypeDefinition;
import org.hypnode.ast.TypeReferenceImplementation;
import org.hypnode.ast.UnionTypeImplementation;
import org.hypnode.ast.attributes.ExportAttribute;
import org.hypnode.ast.attributes.OptionalAttribute;
import org.hypnode.ast.attributes.RequiredAttribute;
import org.hypnode.ast.attributes.TriggerAttribute;
import org.hypnode.ast.value.FieldAccessValueExpression;
import org.hypnode.ast.value.StringValueExpression;

public class NodeReferenceLinkerVisitor implements Visitor<Object> {
    private HypnodeModule module;
    
    public NodeReferenceLinkerVisitor(HypnodeModule node) {
        module = node;
    }

    public void link() {
        visit(module);
    }
    
    @Override
    public Object visit(HypnodeModule node) {
        for(NodeDefinition definition : node.getNodeDefinitions())
            definition.accept(this);

        return null;
    }

    private NodeDefinition getNodeDefinitionByName(String nodeName) {
        for(NodeDefinition definition : module.getNodeDefinitions())
            if(definition.getNodeName().equals(nodeName))
                return definition;

        return null;
    } 

    @Override
    public Object visit(NodeDefinition node) {
        List<NodeInstanceStatement> childNodes = node.getChildNodes();
        
        if(childNodes == null)
            return null;

        for(NodeInstanceStatement instance : childNodes) {
            NodeDefinition def = getNodeDefinitionByName(instance.getNodeName());

            if(def == null)
                throw new UnsupportedOperationException("Node '" + instance.getNodeName() + "' is not defined");

            instance.linkNodeDefinition(def);
        }

        return null;
    }

    @Override
    public Object visit(TypeDefinition node) {
        
        return null;
    }

    @Override
    public Object visit(FieldAccess node) {
        
        return null;
    }

    @Override
    public Object visit(FieldDefinition node) {
        
        return null;
    }

    @Override
    public Object visit(ArrayTypeImplementation node) {
        
        return null;
    }

    @Override
    public Object visit(CompositeTypeImplementation node) {
        
        return null;
    }

    @Override
    public Object visit(TypeReferenceImplementation node) {
        
        return null;
    }

    @Override
    public Object visit(UnionTypeImplementation node) {
        
        return null;
    }

    @Override
    public Object visit(NodeDeclaration node) {
        
        return null;
    }

    @Override
    public Object visit(StatementListNodeImplementation node) {
        
        return null;
    }

    @Override
    public Object visit(ImportNodeImplementation node) {
        
        return null;
    }

    @Override
    public Object visit(PortDefinition node) {
        
        return null;
    }

    @Override
    public Object visit(ExportAttribute node) {
        
        return null;
    }

    @Override
    public Object visit(RequiredAttribute node) {
        
        return null;
    }

    @Override
    public Object visit(OptionalAttribute node) {
        
        return null;
    }

    @Override
    public Object visit(TriggerAttribute node) {
        
        return null;
    }

    @Override
    public Object visit(NodeConnectionStatement node) {
        
        return null;
    }

    @Override
    public Object visit(NodeInstanceStatement node) {
        
        return null;
    }

    @Override
    public Object visit(StringValueExpression stringValueExpression) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public Object visit(FieldAccessValueExpression fieldAccessValueExpression) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
}
