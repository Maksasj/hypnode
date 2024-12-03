package org.hypnode;

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

public class TypeReferenceLinkerVisitor implements Visitor<Object> {
    private HypnodeModule module;
    
    public TypeReferenceLinkerVisitor(HypnodeModule node) {
        module = node;
    }

    public void link() {
        visit(module);
    }
    
    @Override
    public Object visit(HypnodeModule node) {
        for(TypeDefinition definition : node.getTypeDefinitions())
            definition.accept(this);

        for(NodeDefinition definition : node.getNodeDefinitions())
            definition.accept(this);

        return null;
    }

    @Override
    public Object visit(NodeDefinition node) {
        
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
}