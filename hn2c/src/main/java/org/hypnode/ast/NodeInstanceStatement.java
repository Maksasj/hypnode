package org.hypnode.ast;

import javax.print.attribute.SetOfIntegerSyntax;

import org.hypnode.Visitor;

public class NodeInstanceStatement extends IStatement {
    private String name;
    private String nodeName;

    public NodeInstanceStatement(String name, String nodeName) {
        this.name = name;
        this.nodeName = nodeName;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
}
