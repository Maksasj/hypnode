package org.hypnode.ast;

import java.util.List;

import org.hypnode.Visitor;

public class StatementListNodeImplementation extends INodeImplementation {
    private List<IStatement> statements;

    public StatementListNodeImplementation(List<IStatement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
