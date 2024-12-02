package org.hypnode;

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
import org.utils.StringUtils;

public class FlattenTypesVisitor implements Visitor<Object> {
	private HypnodeModule module;

	public FlattenTypesVisitor(HypnodeModule module) {
		this.module = module;
	}

	public void flatten() {
		visit(module);
	}

	@Override
	public Object visit(HypnodeModule node) {
		for(TypeDefinition definition : node.getTypeDefinitions()) {
			definition.accept(this);
		}

		for(NodeDefinition definition : node.getNodeDefinitions()) {
			definition.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(NodeDefinition node) {
        List<PortDefinition> inputPorts = node.getInputPorts();
		for(PortDefinition port : inputPorts) {
			ITypeImplementation impl = port.getTypeImplementation();

			String typeName = "atsym_" + StringUtils.generateRandomString(16);
			
			if(!(impl instanceof TypeReferenceImplementation)) {
				TypeDefinition def = new TypeDefinition(typeName, impl);
				port.setTypeImplementation(new TypeReferenceImplementation(def.getSymbolName()));
				module.addTypeDefinition(def);
			}
		}

        List<PortDefinition> outputPorts = node.getOutputPorts();
		for(PortDefinition port : outputPorts) {
			ITypeImplementation impl = port.getTypeImplementation();

			String typeName = "atsym_" + StringUtils.generateRandomString(16);

			if(!(impl instanceof TypeReferenceImplementation)) {
				TypeDefinition def = new TypeDefinition(typeName, impl);
				port.setTypeImplementation(new TypeReferenceImplementation(def.getSymbolName()));
				module.addTypeDefinition(def);
			}
		}

		return null;
	}

	@Override
	public Object visit(TypeDefinition node) {
		ITypeImplementation impl = node.getImplementation();

		if(impl instanceof ArrayTypeImplementation) {
            visit((ArrayTypeImplementation) impl);
        } else if(impl instanceof CompositeTypeImplementation) {
            visit((CompositeTypeImplementation) impl);
        } else if(impl instanceof TypeReferenceImplementation) {
            // builder.append("// Type Reference\n");
			throw new UnsupportedOperationException("Type reference flatten '" + node.getTypeName() + "'");
        } else if(impl instanceof UnionTypeImplementation) {
            visit((UnionTypeImplementation) impl);
        } else {
			throw new UnsupportedOperationException("Union type flatten");
        }

		return null;
	}

	@Override
	public Object visit(ArrayTypeImplementation node) {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException("Unimplemented method 'visit'");

		return null;
	}

	@Override
	public Object visit(CompositeTypeImplementation node) {
		for(FieldDefinition field : node.getFields()) {
			if(!field.isTypeReferenceType())
				throw new UnsupportedOperationException("Composite type field needs to be flatten");
		}

		return null;
	}

	@Override
	public Object visit(TypeReferenceImplementation node) {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException("Unimplemented method 'visit'");

		return null;
	}


	@Override
	public Object visit(UnionTypeImplementation node) {
		for(ITypeImplementation field : node.getTypes()) {
			throw new UnsupportedOperationException("UnionTypeImplementation");
			// if(!field.isTypeReferenceType())
			// 	throw new UnsupportedOperationException("Composite type field needs to be flatten");
		}

		return null;
	}

	@Override
	public Object visit(FieldAccess node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(FieldDefinition node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(NodeDeclaration node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(StatementListNodeImplementation node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(ImportNodeImplementation node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(PortDefinition node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(ExportAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(RequiredAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(OptionalAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(TriggerAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(NodeConnectionStatement node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Object visit(NodeInstanceStatement node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}
	
}
