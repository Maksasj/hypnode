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
import org.hypnode.ast.attributes.ExportAttribute;
import org.hypnode.ast.attributes.OptionalAttribute;
import org.hypnode.ast.attributes.RequiredAttribute;
import org.hypnode.ast.attributes.TriggerAttribute;

public class GeneratorVisitor implements Visitor<String> {
    private int scope;

    public GeneratorVisitor() {
        scope = 0;
    }

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
        builder.append("/* ================ meta ================ */\n");
        builder.append("\n");
        builder.append("unsigned long _node_import_symbols_count = 0;\n");
        builder.append("struct {\n");
        builder.append("    char* symbol_name;\n");
        builder.append("    char* implementation_symbol\n");
        builder.append("} _node_import_symbols[] = {\n");
        builder.append("\n");
        builder.append("};\n");
        builder.append("\n");
        builder.append("unsigned long _node_export_symbols_count = 1;\n");
        builder.append("_node_export_symbol _node_export_symbols[] = {\n");
        builder.append("    (_node_export_symbol) {\n");
        builder.append("        ._name = \"std_experimental_log\",\n");
        builder.append("\n");
        builder.append("        ._init = \"_node_log_init\",\n");
        builder.append("        ._dispose = \"_node_log_dispose\",\n");
        builder.append("        ._trigger = \"_node_log_trigger\",\n");
        builder.append("\n");
        builder.append("        ._implementation = \"_node_log_implementation\"\n");
        builder.append("    },\n");
        builder.append("};\n");
        builder.append("\n");
        builder.append("/* ====================================== */\n");

        return builder.toString();
    }
    
    @Override
    public String visit(NodeDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        String symbolName = node.getSymbolName();
        
        builder.append("struct _node_" + symbolName + "_struct {\n");
        builder.append("    // Ports\n");
        builder.append("    // Callback\n");
        builder.append("    // void (*_implementation)(void* self);\n");
        builder.append("};\n");

        // extra new line
        builder.append("\n");

        builder.append("// Node life-cycle functions\n");
        builder.append("void* _node_" + symbolName + "_init();\n");
        builder.append("void _node_" + symbolName + "_dispose(void* _node);\n");
        builder.append("void _node_" + symbolName + "_trigger(void* _node);\n");

        // node implementation
        builder.append("void _node_" + symbolName + "_implementation(void* _self) \n");

        // extra new line
        builder.append("\n");

        return builder.toString();
    }

    @Override
    public String visit(TypeDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        // type definition
        builder.append(node.getImplementation().accept(this));
        builder.append("\n");

        // meta type information
        builder.append("meta type information\n");

        builder.append("\n");

        return builder.toString();
    }

    @Override
    public String visit(FieldAccess node) {
        return null;        
    }

    @Override
    public String visit(FieldDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        builder.append(node.getType().accept(this));
        builder.append(" ");
        builder.append(node.getFieldName());
        builder.append(";");
        
        return builder.toString();
    }

    @Override
    public String visit(ArrayTypeImplementation node) {
        return "array type implementation";
    }

    @Override
    public String visit(CompositeTypeImplementation node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        // type definition
        builder.append("struct { \n");

        scope += 1;
        
        for(FieldDefinition field : node.getFields()) {
            builder.append(field.accept(this));
            builder.append("\n");
        }

        scope -= 1;

        builder.append("};");

        // extra new line
        builder.append("\n");

        return builder.toString();
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
}
