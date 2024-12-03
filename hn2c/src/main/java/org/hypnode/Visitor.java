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
import org.hypnode.ast.value.FieldAccessValueExpression;
import org.hypnode.ast.value.StringValueExpression;

public interface Visitor<T> {
    T visit(HypnodeModule node);
    
    T visit(NodeDefinition node);
    T visit(TypeDefinition node);

    T visit(FieldAccess node);
    T visit(FieldDefinition node);

    T visit(ArrayTypeImplementation node);
    T visit(CompositeTypeImplementation node);
    T visit(TypeReferenceImplementation node);
    T visit(UnionTypeImplementation node);

    T visit(NodeDeclaration node);
    T visit(StatementListNodeImplementation node);
    T visit(ImportNodeImplementation node);
    
    T visit(PortDefinition node);

    T visit(ExportAttribute node);
    T visit(RequiredAttribute node);
    T visit(OptionalAttribute node);
    T visit(TriggerAttribute node);

    T visit(NodeConnectionStatement node);
    T visit(NodeInstanceStatement node);

    T visit(StringValueExpression node);
    T visit(FieldAccessValueExpression fieldAccessValueExpression);
};
