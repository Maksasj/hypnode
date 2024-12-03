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
import org.utils.StringUtils;

public class FlattenTypesVisitor implements Visitor<Integer> {
	private HypnodeModule module;

	public FlattenTypesVisitor(HypnodeModule module) {
		this.module = module;
	}

	public Integer flatten() {
		return visit(module);
	}

	@Override
	public Integer visit(HypnodeModule node) {
		Integer counter = 0;

		List<TypeDefinition> listTypeDefs = node.getTypeDefinitions();
		int typeDefCount = listTypeDefs.size();
        for (int i = 0; i < typeDefCount; i++) {
            TypeDefinition current = listTypeDefs.get(i);
			counter += current.accept(this);
        }

		List<TypeDefinition> listNodeDefs = node.getTypeDefinitions();
		int nodeDefCount = listNodeDefs.size();
        for (int i = 0; i < nodeDefCount; i++) {
            TypeDefinition current = listNodeDefs.get(i);
			counter += current.accept(this);
        }

		return counter;
	}

	@Override
	public Integer visit(NodeDefinition node) {
		Integer counter = 0;

        List<PortDefinition> inputPorts = node.getInputPorts();
		for(PortDefinition port : inputPorts) {
			ITypeImplementation impl = port.getTypeImplementation();

			String typeName = "atsym_" + StringUtils.generateRandomString(16);
			
			if(!(impl instanceof TypeReferenceImplementation)) {
				TypeDefinition def = new TypeDefinition(typeName, impl);
				port.setTypeImplementation(new TypeReferenceImplementation(def.getSymbolName()));
				module.addTypeDefinition(def);
				
				counter += 1;
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

				counter += 1;
			}
		}

		return counter;
	}

	@Override
	public Integer visit(TypeDefinition node) {
		Integer counter = 0;

		ITypeImplementation impl = node.getImplementation();

		if(impl instanceof ArrayTypeImplementation) {
            counter += visit((ArrayTypeImplementation) impl);
        } else if(impl instanceof CompositeTypeImplementation) {
            counter += visit((CompositeTypeImplementation) impl);
        } else if(impl instanceof TypeReferenceImplementation) {
            // builder.append("// Type Reference\n");
			// throw new UnsupportedOperationException("Type reference flatten '" + node.getTypeName() + "'");
        } else if(impl instanceof UnionTypeImplementation) {
            counter += visit((UnionTypeImplementation) impl);
        } else {
			throw new UnsupportedOperationException("Union type flatten");
        }

		return counter;
	}

	@Override
	public Integer visit(ArrayTypeImplementation node) {
		Integer counter = 0;

		ITypeImplementation impl = node.getChildTypeImplementation();
		if(!(impl instanceof TypeReferenceImplementation)) {
			String typeName = "atsym_" + StringUtils.generateRandomString(16);

			TypeDefinition def = new TypeDefinition(typeName, impl);
			node.setChildTypeImplementation(new TypeReferenceImplementation(def.getSymbolName()));
			module.addTypeDefinition(def);
			
			counter += 1;
		}
		
		return counter;
	}

	@Override
	public Integer visit(CompositeTypeImplementation node) {
		Integer counter = 0;

		for(FieldDefinition field : node.getFields()) {
			ITypeImplementation impl = field.getTypeImplementation();
			
			if(!(impl instanceof TypeReferenceImplementation)) {
				String typeName = "atsym_" + StringUtils.generateRandomString(16);

				TypeDefinition def = new TypeDefinition(typeName, impl);
				field.setTypeImplementation(new TypeReferenceImplementation(def.getSymbolName()));
				module.addTypeDefinition(def);
				
				counter += 1;
			}
		}

		return counter;
	}

	@Override
	public Integer visit(TypeReferenceImplementation node) {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException("Unimplemented method 'visit'");

		return 0;
	}


	@Override
	public Integer visit(UnionTypeImplementation node) {
		Integer counter = 0;

		List<ITypeImplementation> implementations = new ArrayList<>();

		for(ITypeImplementation impl : node.getTypes()) {
			if(impl instanceof TypeReferenceImplementation)
				implementations.add(impl);

			if(impl instanceof CompositeTypeImplementation) {
				String typeName = "atsym_" + StringUtils.generateRandomString(16);

				TypeDefinition def = new TypeDefinition(typeName, impl);
				implementations.add(new TypeReferenceImplementation(def.getSymbolName()));
				module.addTypeDefinition(def);
				
				counter += 1;
			}

			if(impl instanceof ArrayTypeImplementation) {
				String typeName = "atsym_" + StringUtils.generateRandomString(16);

				TypeDefinition def = new TypeDefinition(typeName, impl);
				implementations.add(new TypeReferenceImplementation(def.getSymbolName()));
				module.addTypeDefinition(def);
				
				counter += 1;
			}

			if(impl instanceof UnionTypeImplementation) {
				UnionTypeImplementation childUnion = (UnionTypeImplementation) impl;

				for(ITypeImplementation child : childUnion.getTypes())
					implementations.add(child);

				counter += 1;
			}
		}

		node.setImplementations(implementations);

		return counter;
	}

	@Override
	public Integer visit(FieldAccess node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(FieldDefinition node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(NodeDeclaration node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(StatementListNodeImplementation node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(ImportNodeImplementation node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(PortDefinition node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(ExportAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(RequiredAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(OptionalAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(TriggerAttribute node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(NodeConnectionStatement node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}

	@Override
	public Integer visit(NodeInstanceStatement node) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'visit'");
	}
	
}
