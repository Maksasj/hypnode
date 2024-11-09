package org.hypnode.ast.attributes;

import org.hypnode.Visitor;
import org.hypnode.ast.IPortAttribute;

public class TriggerAttribute extends IPortAttribute {
    private String nodeName;

    public TriggerAttribute(String nodeName) {
        this.nodeName = nodeName;
    }

	@Override
	public <T> T accept(Visitor<T> visitor) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'accept'");
	}
}

