package org.hypnode.ast.value;

import java.util.List;

import org.hypnode.Visitor;
import org.hypnode.ast.FieldAccess;

public class FieldAccessValueExpression extends ConstantValueExpression {
    private List<FieldAccess> accessList;

    public FieldAccessValueExpression(List<FieldAccess> accessList) {
        this.accessList = accessList;
    }

    public List<FieldAccess> getAccessList() {
        return accessList;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

