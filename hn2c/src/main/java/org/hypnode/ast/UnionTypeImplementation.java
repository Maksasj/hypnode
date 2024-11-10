package org.hypnode.ast;

import org.hypnode.Visitor;

public class UnionTypeImplementation extends ITypeImplementation {
    private ITypeImplementation left;
    private ITypeImplementation right;
    
    public UnionTypeImplementation(ITypeImplementation left, ITypeImplementation right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
