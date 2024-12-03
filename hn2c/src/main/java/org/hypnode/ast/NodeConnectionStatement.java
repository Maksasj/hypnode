package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;
import org.hypnode.ast.value.IValueExpression;

public class NodeConnectionStatement extends IStatement {
    private List<FieldAccess> sink;
    private IValueExpression source;

    public NodeConnectionStatement(List<FieldAccess> sink, IValueExpression source) {
        this.sink = sink;
        this.source = source;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
    
}
