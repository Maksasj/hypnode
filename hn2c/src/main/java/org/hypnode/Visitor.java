package org.hypnode;

import org.hypnode.ast.ArrayTypeImplementation;
import org.hypnode.ast.AstNode;
import org.hypnode.ast.CompositeTypeImplementation;
import org.hypnode.ast.FieldAccess;
import org.hypnode.ast.FieldDefinition;
import org.hypnode.ast.HypnodeModule;
import org.hypnode.ast.IDefinition;
import org.hypnode.ast.INodeAttribute;
import org.hypnode.ast.INodeImplementation;
import org.hypnode.ast.IPortAttribute;
import org.hypnode.ast.IStatement;
import org.hypnode.ast.ImportNodeImplementation;
import org.hypnode.ast.NodeConnectionStatement;
import org.hypnode.ast.NodeDeclaration;
import org.hypnode.ast.NodeDefinition;
import org.hypnode.ast.NodeInstanceStatement;
import org.hypnode.ast.PortDefinition;
import org.hypnode.ast.StatementListNodeImplementation;
import org.hypnode.ast.TypeDefinition;
import org.hypnode.ast.TypeReferenceImplementation;

public interface Visitor<T> {
    T visit(AstNode node);
    T visit(FieldAccess node);
    T visit(FieldDefinition node);
    T visit(HypnodeModule node);
    T visit(IDefinition node);
    T visit(INodeAttribute node);
    T visit(INodeImplementation node);
    T visit(IPortAttribute node);
    T visit(IStatement node);
    
    T visit(ArrayTypeImplementation node);
    T visit(CompositeTypeImplementation node);
    T visit(TypeReferenceImplementation node);

    T visit(ImportNodeImplementation node);
    T visit(NodeConnectionStatement node);
    T visit(NodeDeclaration node);
    T visit(NodeDefinition node);
    T visit(NodeInstanceStatement node);
    T visit(PortDefinition node);
    T visit(StatementListNodeImplementation node);
    T visit(TypeDefinition node);
};
