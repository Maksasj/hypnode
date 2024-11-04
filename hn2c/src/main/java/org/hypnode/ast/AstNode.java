package org.hypnode.ast;

import org.hypnode.Visitor;

public abstract class AstNode {
    public abstract <T> T accept(Visitor<T> visitor);
}
