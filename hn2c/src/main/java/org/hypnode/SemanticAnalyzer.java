package org.hypnode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hypnode.ast.CompositeTypeImplementation;
import org.hypnode.ast.FieldDefinition;
import org.hypnode.ast.HypnodeModule;
import org.hypnode.ast.INodeImplementation;
import org.hypnode.ast.NodeDefinition;
import org.hypnode.ast.NodeInstanceStatement;
import org.hypnode.ast.PortDefinition;
import org.hypnode.ast.StatementListNodeImplementation;
import org.hypnode.ast.TypeDefinition;

public class SemanticAnalyzer {
    private HypnodeModule module;

    public SemanticAnalyzer(HypnodeModule module) {
        this.module = module;
    }

    public void analyze() {
        // Check if type is declared only once
        checkIsTypeDeclaredOnlyOnce();

        // Check if node is declared only once
        checkIsNodeDeclaredOnlyOnce();

        // Check is node ports are declared only once
        checkIsNodePortsAreDeclaredOnlyOnce();

        // Check is type fields are unique
        checkIsTypeFieldsAreUnique();

        // Check if node exported name is used only once
        checkIsNodeExportedNameIsUsedOnlyOnce();

        // Check if node instance name is used only once
        checkIsNodeInstanceNameIsUsedOnlyOnce();
    }

    public void checkIsTypeDeclaredOnlyOnce() {
        List<TypeDefinition> defs = module.getTypeDefinitions();

        Set<String> typeNames = new HashSet<>();

        for(TypeDefinition def : defs) {
            if(typeNames.contains(def.getTypeName()))
                throw new UnsupportedOperationException("Type '" + def.getTypeName() + "' is declared multiple times");
            else
                typeNames.add(def.getTypeName());
        }
    }

    public void checkIsNodeDeclaredOnlyOnce() {
        List<NodeDefinition> defs = module.getNodeDefinitions();

        Set<String> typeNames = new HashSet<>();

        for(NodeDefinition def : defs) {
            if(typeNames.contains(def.getNodeName()))
                throw new UnsupportedOperationException("Node '" + def.getNodeName() + "' is declared multiple times");
            else
                typeNames.add(def.getNodeName());
        }
    }

    public void checkIsNodePortsAreDeclaredOnlyOnce() {
        List<NodeDefinition> defs = module.getNodeDefinitions();

        for(NodeDefinition def : defs) {
            List<PortDefinition> ports = def.getPorts();
            Set<String> portNames = new HashSet<>();

            for(PortDefinition port : ports) {
                if(portNames.contains(port.getPortName()))
                    throw new UnsupportedOperationException("Port '" + port.getPortName() + "' is declared multiple times in '" + def.getNodeName() + "' node");
                else
                    portNames.add(port.getPortName());
            }
        }
    }

    public void checkIsTypeFieldsAreUnique() {
        List<TypeDefinition> defs = module.getTypeDefinitions();

        for(TypeDefinition def : defs) {
            if(def.getImplementation() instanceof CompositeTypeImplementation) {
                CompositeTypeImplementation impl = (CompositeTypeImplementation) def.getImplementation();

                List<FieldDefinition> fields = impl.getFields();
                Set<String> fieldNames = new HashSet<>();
    
                for(FieldDefinition field : fields) {
                    if(fieldNames.contains(field.getFieldName()))
                        throw new UnsupportedOperationException("Field '" + field.getFieldName() + "' is declared multiple times in '" + def.getTypeName() + "' type");
                    else
                        fieldNames.add(field.getFieldName());
                }
            }
        }
    }

    public void checkIsNodeExportedNameIsUsedOnlyOnce() {
        List<NodeDefinition> defs = module.getNodeDefinitions();

        Set<String> exportedNames = new HashSet<>();

        for(NodeDefinition def : defs) {
            String exportedName = def.getExportedName();

            if(exportedName != null) {
                if(exportedNames.contains(exportedName))
                    throw new UnsupportedOperationException("Export name '" + exportedName + "' is used multiple times");
                else
                    exportedNames.add(exportedName);
            }
        }
    }

    public void checkIsNodeInstanceNameIsUsedOnlyOnce() {
        for(NodeDefinition def : module.getNodeDefinitions()) {
            INodeImplementation implementation = def.getImplementation();

            if(implementation instanceof StatementListNodeImplementation) {
                List<NodeInstanceStatement> childNodes = def.getChildNodes();
                Set<String> nodeNames = new HashSet<>();

                for(NodeInstanceStatement childNode : childNodes) {
                    if(nodeNames.contains(childNode.getName()))
                        throw new UnsupportedOperationException("Node instance name '" + childNode.getName() + "' is used multiple times");
                    else
                        nodeNames.add(childNode.getName());
                }
            }
        }
    }
}
