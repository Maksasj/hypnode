package org.hypnode.ast.attributes;

import org.hypnode.ast.INodeAttribute;

public class ExportAttribute implements INodeAttribute {
    private String symbolName;

    public ExportAttribute(String symbolName) {
        this.symbolName = symbolName;
    }
}
