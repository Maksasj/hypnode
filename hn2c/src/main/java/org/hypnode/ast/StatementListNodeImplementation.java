package org.hypnode.ast;

import java.util.List;

public class StatementListNodeImplementation implements INodeImplementation {
    private List<IStatement> statements;

    public StatementListNodeImplementation(List<IStatement> statements) {
        this.statements = statements;
    }
}
