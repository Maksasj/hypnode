package org.hypnode;

import java.util.ArrayList;
import java.util.List;

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

public class GeneratorVisitor implements Visitor<String> {
    private int scope = 0;
    
    @Override
    public String visit(HypnodeModule node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        // include necessary libraries
        builder.append("#include <stdlib.h>\n");
        builder.append("#include \"native.h\"\n");

        // extra new line
        builder.append("\n"); 

        // type declarations
        for(TypeDefinition definition : node.getTypeDefinitions()) {
            builder.append(definition.accept(this));
        }

        // node delcarations
        for(NodeDefinition definition : node.getNodeDefinitions()) {
            builder.append(definition.accept(this));
        }

        // meta information
        appendTransitiveMetaNodes(node, builder);

        return builder.toString();
    }
    
    @Override
    public String visit(NodeDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        String symbolName = node.getSymbolName();
        
        builder.append("// Node '" + node.getNodeName() +"' declaration\n");
        builder.append("struct _node_" + symbolName + "_struct {\n");

        List<PortDefinition> inputPorts = node.getInputPorts();
        if(!inputPorts.isEmpty()) {
            builder.append("    // Input ports\n");
            for(PortDefinition port : inputPorts) {
                builder.append("    _port_struct* " + port.getSymbolName() + "; // " + port.getPortName() + "\n");
            }
        }

        List<PortDefinition> outputPorts = node.getOutputPorts();
        if(!outputPorts.isEmpty()) {
            if(!inputPorts.isEmpty())
                builder.append("\n");

            builder.append("    // Output ports\n");
            for(PortDefinition port : outputPorts) {
                builder.append("    _port_struct* " + port.getSymbolName() + "; // " + port.getPortName() + "\n");
            }
        }

        builder.append("};\n");
        
        // Extra new line
        builder.append("\n");

        // Declare all C functions
        builder.append("void* _node_" + symbolName + "_init();\n");
        builder.append("void _node_" + symbolName + "_dispose(void* _node);\n");
        builder.append("int _node_" + symbolName + "_trigger(void* _node);\n");

        // Node implementation
        if(node.imported()) {
            builder.append("_node_implementation _node_" + symbolName + "_implementation; // Implementation is provided by environment\n");
        } else {
            builder.append("void _node_" + symbolName + "_implementation(void* _self);\n");
        }

        // Extra new line
        builder.append("\n");

        appendNodeInitCallbackImplementation(node, builder);
        appendNodeDisposeCallbackImplementation(node, builder);
        appendNodeTriggerCallbackImplementation(node, builder);
 
        // If node is not imported we need to have implementation
        if(!node.imported())
            appendNodeImplementationCallbackImplementation(node, builder);

        return builder.toString();
    }

    @Override
    public String visit(TypeDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        ITypeImplementation impl = node.getImplementation();

        if(impl instanceof ArrayTypeImplementation) {
            appendArrayTypeImplementation(node, (ArrayTypeImplementation) impl, builder);
        } else if(impl instanceof CompositeTypeImplementation) {
            appendCompositeTypeImplementation(node, (CompositeTypeImplementation) impl, builder);
        } else if(impl instanceof TypeReferenceImplementation) {
            builder.append("// Type Reference\n");
        } else if(impl instanceof UnionTypeImplementation) {
            appendUnionTypeImplementation(node, (UnionTypeImplementation) impl, builder);
        } else {
            builder.append("// Unreachable\n");
        }

        builder.append("\n");

        return builder.toString();
    }

    private void appendArrayTypeImplementation(TypeDefinition node, ArrayTypeImplementation implementation, StringBuilder builder) {
        builder.append("// Type '" + node.getTypeName() + "' declaration\n");
        builder.append("typedef struct { \n");

        ITypeImplementation impl = implementation.getChildTypeImplementation();

        if(impl instanceof TypeReferenceImplementation) {
            builder.append("    " + ((TypeReferenceImplementation) impl).getReferenceTypeName() + "* value;\n");
        } else {
            throw new UnsupportedOperationException("Not type reference implementation found in array type implementation");
        }

        builder.append("} " + node.getSymbolName() + ";\n");

        builder.append("\n");
        
        builder.append("// Type '" + node.getTypeName() + "' meta information\n");
        builder.append("static _type_info _" + node.getSymbolName() + "_type_info = (_type_info) {\n");
        builder.append("    .type_name = \"" + node.getTypeName() + "\",\n");
        builder.append("    .category = Array,\n");
        builder.append("    .compound_fields = NULL,\n");
        builder.append("    .union_fields = NULL\n");
        builder.append("};");

        builder.append("\n");
    }

    private void appendCompositeTypeImplementation(TypeDefinition node, CompositeTypeImplementation implementation, StringBuilder builder) {
        builder.append("// Type '" + node.getTypeName() + "' declaration\n");
        builder.append("typedef struct { \n");

        for(FieldDefinition field : implementation.getFields()) {
            if(field.getTypeImplementation() instanceof TypeReferenceImplementation) {
                builder.append("    " + ((TypeReferenceImplementation) field.getTypeImplementation()).getReferenceTypeName() + "* " + field.getFieldName() + ";\n");
            } else {
                throw new UnsupportedOperationException("Not type reference implementation found in compisote type implementation");
            }
        }

        builder.append("} " + node.getSymbolName() + ";\n");

        builder.append("\n");
        
        builder.append("// Type '" + node.getTypeName() + "' meta information\n");
        builder.append("static _type_info _" + node.getSymbolName() + "_type_info = (_type_info) {\n");
        builder.append("    .type_name = \"" + node.getTypeName() + "\",\n");
        builder.append("    .category = Compound,\n");
        builder.append("    .compound_fields = NULL,\n");
        builder.append("    .union_fields = NULL\n");
        builder.append("};");

        builder.append("\n");
    }

    private void appendUnionTypeImplementation(TypeDefinition node, UnionTypeImplementation implementation, StringBuilder builder) {
        builder.append("// Type '" + node.getTypeName() + "' declaration\n");
        builder.append("typedef struct { \n");

        builder.append("    union {\n");

        for(ITypeImplementation impl : implementation.getTypes()) {
            if(impl instanceof TypeReferenceImplementation) {
                builder.append("        " + ((TypeReferenceImplementation) impl).getReferenceTypeName() + " A" + ";\n");
            } else {
                throw new UnsupportedOperationException("Not type reference implementation found in union type implementation");
            }
        }

        builder.append("    } as;\n");

        builder.append("} " + node.getSymbolName() + ";\n");

        builder.append("\n");
        
        builder.append("// Type '" + node.getTypeName() + "' meta information\n");
        builder.append("static _type_info _" + node.getSymbolName() + "_type_info = (_type_info) {\n");
        builder.append("    .type_name = \"" + node.getTypeName() + "\",\n");
        builder.append("    .category = Union,\n");
        builder.append("    .compound_fields = NULL,\n");
        builder.append("    .union_fields = NULL\n");
        builder.append("};");

        builder.append("\n");
    }

    @Override
    public String visit(FieldAccess node) {
        return null;        
    }

    @Override
    public String visit(FieldDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        builder.append(node.getTypeImplementation().accept(this));
        builder.append(" ");
        builder.append(node.getFieldName());
        builder.append(";");
        
        return builder.toString();
    }

    @Override
    public String visit(ArrayTypeImplementation node) {
        return null;
    }

    @Override
    public String visit(CompositeTypeImplementation node) {
        return null;
    }

    @Override
	public String visit(UnionTypeImplementation node) {
        return null;
    }
    
    @Override
    public String visit(TypeReferenceImplementation node) {
        return node.getReferenceTypeName();
    }
    
    @Override
    public String visit(NodeDeclaration node) {
        return null;
    }

    @Override
    public String visit(StatementListNodeImplementation node) {
        return null;
    }

    @Override
    public String visit(ImportNodeImplementation node) {
        return null;
    }

    @Override
    public String visit(PortDefinition node) {
        return null;
    }

    @Override
	public String visit(ExportAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public String visit(RequiredAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public String visit(OptionalAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public String visit(TriggerAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

    @Override
    public String visit(NodeConnectionStatement node) {
        return null;
    }
    
    @Override
    public String visit(NodeInstanceStatement node) {
        return null;
    }

    private void appendScopeTabs(StringBuilder builder) {
        for(int i = 0; i < scope; ++i) {
            builder.append("    ");
        }
    }

    static private List<NodeDefinition> collectImports(HypnodeModule module) {
        List<NodeDefinition> exports = new ArrayList<>();

        for(NodeDefinition def : module.getNodeDefinitions())
            if(def.imported())
                exports.add(def);

        return exports;
    }

    static private List<NodeDefinition> collectExports(HypnodeModule module) {
        List<NodeDefinition> exports = new ArrayList<>();

        for(NodeDefinition def : module.getNodeDefinitions())
            if(def.exported())
                exports.add(def);

        return exports;
    }

    private void appendNodeInitCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();
        String structName = "_node_" + symbolName + "_struct";
        
        builder.append("void* _node_" + symbolName + "_init() {\n");
        builder.append("    struct " + structName + "* node = malloc(sizeof(struct " + structName + "));\n");

        builder.append("\n");

        builder.append("    // Initial trigger callback call;\n");
        builder.append("    _node_" + symbolName + "_trigger(node);\n");
        builder.append("\n");

        builder.append("    return node;\n");
        builder.append("}\n");
        builder.append("\n");
    }
    
    private void appendNodeDisposeCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();

        builder.append("void _node_" + symbolName + "_dispose(void* _node) {\n");
        builder.append("    // Dispose port values\n");
        builder.append("    // Todo\n");
        builder.append("\n");
        builder.append("    // Dispose node structure\n");
        builder.append("    free(_node);\n");
        builder.append("}\n");

        builder.append("\n");
    }

    private void appendNodeTriggerCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();

        builder.append("int _node_" + symbolName + "_trigger(void* _node) {\n");
        builder.append("    // Check all requirements\n");
        builder.append("    // Todo\n");
        builder.append("\n");
        builder.append("    // Call node implementation callback\n");
        builder.append("    _node_" + symbolName + "_implementation(_node);\n");
        builder.append("\n");
        builder.append("    return 1;\n");
        builder.append("}\n");

        builder.append("\n");
    }

    private void appendNodeImplementationCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();

        builder.append("void _node_" + symbolName + "_implementation(void* _self) {\n");

        List<NodeInstanceStatement> childNodes = node.getChildNodes();

        if(!childNodes.isEmpty()) {
            builder.append("    // Initialize all child nodes\n");

            for(NodeInstanceStatement child : childNodes) {
                String childSymbolName = child.getSymbolName();
                String nodeSymbolName = child.getNodeSymbolName();

                builder.append("    " + nodeSymbolName + " " + childSymbolName + " = _node_" + nodeSymbolName + "_init();\n"); 
            }

            builder.append("\n");
        }
    
        // Get all raw node connections
        List<NodeConnectionStatement> connectionStatements = node.getConnections();
        
        List<Object> pipes = new ArrayList<>();

        if(!pipes.isEmpty()) {
            builder.append("    // Initialize all connections\n");
            
            builder.append("\n");
        }

        builder.append("    // Running flag\n");
        builder.append("    int running;\n");

        builder.append("    do {\n");
        builder.append("        running = 0;\n");


        for(Object pipe : pipes) {
            builder.append("    // pipe");
        }

        builder.append("    } while(running > 0);\n");

        /*
        List<PortDefinition> inputPorts = node.getInputPorts();
        if(!inputPorts.isEmpty())
            builder.append("    // Initialize input ports\n");

        for(PortDefinition port : inputPorts) {
            builder.append("    node->" + port.getSymbolName() + " = (_port_struct) {\n");
            builder.append("        .port_name = \"" + port.getPortName() + "\",\n");
            builder.append("        .value = NULL, // Initial value\n");
            builder.append("        .value_type_info = _" + ((TypeReferenceImplementation) port.getTypeImplementation()).getReferenceTypeName() + "_type_info\n");
            builder.append("    };\n");
        }
        builder.append("\n");
        */

        /*
        List<PortDefinition> outputPorts = node.getOutputPorts();
        if(!outputPorts.isEmpty())
            builder.append("    // Initialize output ports\n");
        
        for(PortDefinition port : outputPorts) {
            builder.append("    node->" + port.getSymbolName() + " = (_port_struct) {\n");
            builder.append("        .port_name = \"" + port.getPortName() + "\",\n");
            builder.append("        .value = NULL, // Initial value\n");
            builder.append("        .value_type_info = _" + ((TypeReferenceImplementation) port.getTypeImplementation()).getReferenceTypeName() + "_type_info\n");
            builder.append("    };\n");
        }
        builder.append("\n");
        */

        builder.append("}\n");

        builder.append("\n");
    }

    private void appendTransitiveMetaNodes(HypnodeModule node, StringBuilder builder) {
        builder.append("/* ================ meta ================ */\n");

        List<NodeDefinition> imported = collectImports(node);
        builder.append("\n");
        builder.append("unsigned long _node_import_symbols_count = " + imported.size() + ";\n");
        builder.append("struct {\n");
        builder.append("    char* symbol_name;\n");
        builder.append("    char* implementation_symbol\n");
        builder.append("} _node_import_symbols[] = {\n");

        for(NodeDefinition imp : imported) {
            builder.append("    { \"" + imp.getImportedName() + "\", \"_node_" + imp.getSymbolName() + "_implementation\" }\n");
        }

        builder.append("};\n");
        builder.append("\n");

        List<NodeDefinition> exported = collectExports(node);
        builder.append("unsigned long _node_export_symbols_count = " + exported.size() + ";\n");
        builder.append("_node_export_symbol _node_export_symbols[] = {\n");

        for(NodeDefinition exp : exported) {
            builder.append("    (_node_export_symbol) {\n");
            builder.append("        ._name = \"" + exp.getExportedName() + "\",\n");
            builder.append("\n");
            builder.append("        ._init = \"_node_"+ exp.getSymbolName() +"_init\",\n");
            builder.append("        ._dispose = \"_node_"+ exp.getSymbolName() +"_dispose\",\n");
            builder.append("        ._trigger = \"_node_"+ exp.getSymbolName() +"_trigger\",\n");
            builder.append("        ._implementation = \"_node_" + exp.getSymbolName() + "_implementation\"\n");
            builder.append("    },\n");
        }

        builder.append("};\n");
        builder.append("\n");

        builder.append("/* ====================================== */\n");
    } 
}
