package org.hypnode;

import java.util.ArrayList;
import java.util.List;

import org.hypnode.ast.ArrayTypeImplementation;
import org.hypnode.ast.CompositeTypeImplementation;
import org.hypnode.ast.FieldAccess;
import org.hypnode.ast.FieldDefinition;
import org.hypnode.ast.HypnodeModule;
import org.hypnode.ast.IDefinition;
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
    private int scope;
    private HypnodeModule module;

    public GeneratorVisitor(HypnodeModule module) {
        this.scope = 0;
        this.module = module;
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
        appendTransitiveMetaNodes(node, builder);

        return builder.toString();
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

    @Override
    public String visit(NodeDefinition node) {
        StringBuilder builder = new StringBuilder();
        appendScopeTabs(builder);

        String symbolName = node.getSymbolName();
        
        builder.append("// Node '" + node.getNodeName() +"' declaration\n");
        builder.append("struct _node_" + symbolName + "_struct {\n");
        builder.append("    // Ports\n");
        builder.append("};\n");
        
        // Extra new line
        builder.append("\n");

        // Declare all C functions
        builder.append("void* _node_" + symbolName + "_init();\n");
        builder.append("void _node_" + symbolName + "_dispose(void* _node);\n");
        builder.append("void _node_" + symbolName + "_trigger(void* _node);\n");

        // Node implementation
        if(node.imported()) {
            builder.append("_node_implementation _node_" + symbolName + "_implementation; // implementation is provided by environment\n");
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

        builder.append("void* _node_" + symbolName + "_init() {\n");
        builder.append("\n");
        builder.append("}\n");

        builder.append("\n");
    }
    
    private void appendNodeDisposeCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();

        builder.append("void _node_" + symbolName + "_dispose(void* _node) {\n");
        builder.append("\n");
        builder.append("}\n");

        builder.append("\n");
    }

    private void appendNodeTriggerCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();

        builder.append("void _node_" + symbolName + "_trigger(void* _node) {\n");
        builder.append("\n");
        builder.append("}\n");

        builder.append("\n");
    }

    private void appendNodeImplementationCallbackImplementation(NodeDefinition node, StringBuilder builder) {
        String symbolName = node.getSymbolName();
        
        builder.append("void _node_" + symbolName + "_implementation(void* _self) {\n");
        builder.append("\n");
        builder.append("}\n");

        builder.append("\n");
    }
}
