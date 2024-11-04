package org.hypnode.ast;

import javax.print.attribute.SetOfIntegerSyntax;

public class NodeInstanceStatement implements IStatement {
    private String name;
    private String nodeName;

    public NodeInstanceStatement(String name, String nodeName) {
        this.name = name;
        this.nodeName = nodeName;
    }
}
