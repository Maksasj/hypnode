package org.hypnode.ast;

import java.util.ArrayList;
import java.util.List;

import org.hypnode.Visitor;

public class UnionTypeImplementation extends ITypeImplementation {
    private List<ITypeImplementation> types;
    
    public UnionTypeImplementation(ITypeImplementation left, ITypeImplementation right) {
        this.types = new ArrayList<>();
        
        this.types.add(left);
        this.types.add(right);
    }

    public List<ITypeImplementation> getTypes() {
        return types;
    }

    public void setImplementations(List<ITypeImplementation> types) {
        this.types = types;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
