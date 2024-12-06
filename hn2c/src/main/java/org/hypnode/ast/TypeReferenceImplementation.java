package org.hypnode.ast;

import org.hypnode.Visitor;

public class TypeReferenceImplementation extends ITypeImplementation {
    private String typeName;
    private String linkedSymbolName;

    public TypeReferenceImplementation(String typeName) {
        this.typeName = typeName;
    }

    public String getReferenceTypeName() {
        return typeName;
    }

    public String getLinkedSymbolName() {
        return linkedSymbolName;
    }

    public void setLinkedSymbolName(String linkedSymbolName) {
        this.linkedSymbolName = linkedSymbolName;
    }

    public boolean isPrimitiveType() {
        switch (typeName) {
            case "i8": return true;
            case "i16": return true;
            case "i32": return true;
            case "i64": return true;

            case "u8": return true;
            case "u16": return true;
            case "u32": return true;
            case "u64": return true;

            case "f32": return true;
            case "f64": return true;

            case "string": return true;
            case "bool": return true;

            case "any": return true;
        }
        
        return false;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
