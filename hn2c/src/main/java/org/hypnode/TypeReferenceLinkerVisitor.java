package org.hypnode;

import org.hypnode.ast.ArrayTypeImplementation;
import org.hypnode.ast.CompositeTypeImplementation;
import org.hypnode.ast.FieldAccess;
import org.hypnode.ast.FieldDefinition;
import org.hypnode.ast.HypnodeModule;
import org.hypnode.ast.ITypeImplementation;
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

    private TypeDefinition getTypeDefinitionByName(String typeName) {
        for(TypeDefinition definition : module.getTypeDefinitions()) {
            if(definition.getTypeName().equals(typeName))
                return definition;
        }

        return null;
    } 

    @Override
    public Object visit(NodeDefinition node) {
        for(PortDefinition port : node.getInputPorts()) {
            if(!(port.getTypeImplementation() instanceof TypeReferenceImplementation))
                throw new UnsupportedOperationException("Node '" + node.getNodeName() + "'' port '" + port.getPortName() + "' is not type reference implementation port on type reference linker stage");
            
            TypeReferenceImplementation impl = (TypeReferenceImplementation) port.getTypeImplementation();

            if(impl.isPrimitiveType()) {
                impl.setLinkedSymbolName(impl.getReferenceTypeName());
            } else {
                TypeDefinition typeDef = getTypeDefinitionByName(impl.getReferenceTypeName());

                if(typeDef == null)
                    throw new UnsupportedOperationException("Type '" + impl.getReferenceTypeName() + "' is not declared");

                impl.setLinkedSymbolName(typeDef.getSymbolName());
            }
        }

        for(PortDefinition port : node.getOutputPorts()) {
            if(!(port.getTypeImplementation() instanceof TypeReferenceImplementation))
                throw new UnsupportedOperationException("Node '" + node.getNodeName() + "'' port '" + port.getPortName() + "' is not type reference implementation port on type reference linker stage");
        
            TypeReferenceImplementation impl = (TypeReferenceImplementation) port.getTypeImplementation();

            if(impl.isPrimitiveType()) {
                impl.setLinkedSymbolName(impl.getReferenceTypeName());
            } else {
                TypeDefinition typeDef = getTypeDefinitionByName(impl.getReferenceTypeName());

                if(typeDef == null)
                    throw new UnsupportedOperationException("Type '" + impl.getReferenceTypeName() + "' is not declared");

                impl.setLinkedSymbolName(typeDef.getSymbolName());
            }
        }

        return null;
    }

    private String getTypeSymbolName(String typeName) {
        for(TypeDefinition defs : module.getTypeDefinitions()) {
            if(defs.getTypeName().equals(typeName)) {
                return defs.getSymbolName();
            }
        }

        return null;
    }

    @Override
    public Object visit(TypeDefinition node) {
        ITypeImplementation impl = node.getImplementation();

        if(impl instanceof ArrayTypeImplementation) {
            ITypeImplementation child = ((ArrayTypeImplementation)impl).getChildTypeImplementation();
            
            if(!(child instanceof TypeReferenceImplementation))
                throw new UnsupportedOperationException("Unexpected type implementation type");

            TypeReferenceImplementation ref = (TypeReferenceImplementation) child;

            if(!ref.isPrimitiveType()) {
                String sym = getTypeSymbolName(ref.getReferenceTypeName());

                if(sym == null)
                    throw new UnsupportedOperationException("Type '" + ref.getReferenceTypeName() + " ' does not exist");

                ref.setLinkedSymbolName(sym);
            } else {
                ref.setLinkedSymbolName(ref.getReferenceTypeName());
            }
        } else if(impl instanceof CompositeTypeImplementation) {
            CompositeTypeImplementation actuall = (CompositeTypeImplementation) impl;

            for(FieldDefinition def : actuall.getFields()) {
                ITypeImplementation child = def.getTypeImplementation();
            
                if(!(child instanceof TypeReferenceImplementation))
                    throw new UnsupportedOperationException("Unexpected type implementation type");
    
                TypeReferenceImplementation ref = (TypeReferenceImplementation) child;
    
                if(!ref.isPrimitiveType()) {
                    String sym = getTypeSymbolName(ref.getReferenceTypeName());
    
                    if(sym == null)
                        throw new UnsupportedOperationException("Type '" + ref.getReferenceTypeName() + " ' does not exist");
    
                    ref.setLinkedSymbolName(sym);
                } else {
                    ref.setLinkedSymbolName(ref.getReferenceTypeName());
                }
            }
        } else if(impl instanceof UnionTypeImplementation) {
            UnionTypeImplementation actuall = (UnionTypeImplementation) impl;

            for(ITypeImplementation child : actuall.getTypes()) {
                if(!(child instanceof TypeReferenceImplementation))
                    throw new UnsupportedOperationException("Unexpected type implementation type");
    
                TypeReferenceImplementation ref = (TypeReferenceImplementation) child;
    
                if(!ref.isPrimitiveType()) {
                    String sym = getTypeSymbolName(ref.getReferenceTypeName());
    
                    if(sym == null)
                        throw new UnsupportedOperationException("Type '" + ref.getReferenceTypeName() + " ' does not exist");
    
                    ref.setLinkedSymbolName(sym);
                } else {
                    ref.setLinkedSymbolName(ref.getReferenceTypeName());
                }
            }
        } else if(impl instanceof TypeReferenceImplementation) {
            TypeReferenceImplementation actuall = (TypeReferenceImplementation) impl;

            if(!actuall.isPrimitiveType()) {
                String sym = getTypeSymbolName(actuall.getReferenceTypeName());

                if(sym == null)
                    throw new UnsupportedOperationException("Type '" + actuall.getReferenceTypeName() + " ' does not exist");

                actuall.setLinkedSymbolName(sym);
            } else {
                actuall.setLinkedSymbolName(actuall.getReferenceTypeName());
            }
        }
        
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
