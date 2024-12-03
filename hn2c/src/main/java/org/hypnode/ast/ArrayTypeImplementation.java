package org.hypnode.ast;

import org.hypnode.Visitor;

public class ArrayTypeImplementation extends ITypeImplementation {
    private ITypeImplementation child;
    
    public ArrayTypeImplementation(ITypeImplementation child) {
        this.child = child;
    }

    public ITypeImplementation getChildTypeImplementation() {
        return child;
    }

    public void setChildTypeImplementation(ITypeImplementation typeImplementation) {
        this.child = typeImplementation;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
