package org.hypnode.ast;

import java.util.ArrayList;
import java.util.List;

import org.hypnode.Visitor;
import org.hypnode.ast.value.FieldAccessValueExpression;

public class StatementListNodeImplementation extends INodeImplementation {
    private List<IStatement> statements;

    public StatementListNodeImplementation(List<IStatement> statements) {
        this.statements = statements;
    }

    public List<NodeInstanceStatement> getChildNodes() {
        List<NodeInstanceStatement> childs = new ArrayList<>();

        for(IStatement child : statements)
            if(child instanceof NodeInstanceStatement)
                childs.add((NodeInstanceStatement) child);

        return childs;
    }

    public List<NodeConnectionStatement> getConnections() {
        List<NodeConnectionStatement> childs = new ArrayList<>();

        for(IStatement child : statements)
            if(child instanceof NodeConnectionStatement)
                childs.add((NodeConnectionStatement) child);

        return childs;
    }

    public List<NodeConnectionStatement> getConstantValueConnections() {
        List<NodeConnectionStatement> childs = new ArrayList<>();

        for(IStatement child : statements)
            if(child instanceof NodeConnectionStatement 
                && ((NodeConnectionStatement) child).getSource() instanceof gasgasgasgasgasgasgas)
                    childs.add((NodeConnectionStatement) child);

        return childs;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
