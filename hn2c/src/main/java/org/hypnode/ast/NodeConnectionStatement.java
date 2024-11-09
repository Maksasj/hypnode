package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;

public class NodeConnectionStatement extends IStatement {
    private List<FieldAccess> sink;
    private List<FieldAccess> source;

    public NodeConnectionStatement(List<FieldAccess> sink, List<FieldAccess> source) {
        this.sink = sink;
        this.source = source;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
    
}
