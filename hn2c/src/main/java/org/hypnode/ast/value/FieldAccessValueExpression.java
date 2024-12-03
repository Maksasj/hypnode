package org.hypnode.ast.value;

import org.hypnode.Visitor;

public class FieldAccessValueExpression extends ConstantValueExpression {
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

