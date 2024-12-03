package org.hypnode.ast.value;

import org.hypnode.Visitor;

public class StringValueExpression extends ConstantValueExpression {
    private String value;

    public StringValueExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
