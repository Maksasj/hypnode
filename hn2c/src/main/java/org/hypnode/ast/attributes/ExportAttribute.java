package org.hypnode.ast.attributes;

import org.hypnode.ast.INodeAttribute;

public class ExportAttribute extends INodeAttribute {
    private String symbolName;

    public ExportAttribute(String symbolName) {
        this.symbolName = symbolName;
    }
}
